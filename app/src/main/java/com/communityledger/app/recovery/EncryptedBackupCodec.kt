package com.communityledger.app.recovery

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FilterOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.security.GeneralSecurityException
import java.security.SecureRandom
import java.util.Arrays
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import javax.crypto.Cipher
import javax.crypto.CipherOutputStream
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class InvalidBackupException(message: String, cause: Throwable? = null) : IOException(message, cause)

object EncryptedBackupCodec {
    const val FORMAT_VERSION = 1
    const val PBKDF2_ITERATIONS = 600_000
    const val MIN_PASSPHRASE_LENGTH = 12
    const val DEFAULT_MAX_PLAINTEXT_BYTES = 64L * 1024L * 1024L

    private const val KDF_ID_PBKDF2_HMAC_SHA256 = 1
    private const val MIN_ACCEPTED_ITERATIONS = 600_000
    private const val MAX_ACCEPTED_ITERATIONS = 2_000_000
    private const val KEY_LENGTH_BITS = 256
    private const val SALT_LENGTH = 16
    private const val NONCE_LENGTH = 12
    private const val GCM_TAG_LENGTH_BITS = 128
    private const val MAX_ARCHIVE_OVERHEAD_BYTES = 1024L * 1024L
    private val magic = byteArrayOf('C'.code.toByte(), 'L'.code.toByte(), 'B'.code.toByte(), 'K'.code.toByte())

    fun isSupported(): Boolean = runCatching {
        SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        Cipher.getInstance("AES/GCM/NoPadding")
    }.isSuccess

    /**
     * Writes a version-1 AES-256-GCM archive using PBKDF2-HMAC-SHA256 and GZIP.
     * The caller owns and must close both streams and clear [passphrase] after this call.
     * Write to a temporary file and rename it only after this method succeeds.
     */
    fun encrypt(
        input: InputStream,
        output: OutputStream,
        passphrase: CharArray,
        maxPlaintextBytes: Long = DEFAULT_MAX_PLAINTEXT_BYTES,
        secureRandom: SecureRandom = SecureRandom()
    ) {
        requireValidPassphrase(passphrase)
        require(maxPlaintextBytes > 0) { "Maximum plaintext size must be positive." }

        val salt = ByteArray(SALT_LENGTH).also(secureRandom::nextBytes)
        val nonce = ByteArray(NONCE_LENGTH).also(secureRandom::nextBytes)
        val header = createHeader(PBKDF2_ITERATIONS, salt, nonce)
        val keyBytes = deriveKey(passphrase, salt, PBKDF2_ITERATIONS)

        try {
            val cipher = Cipher.getInstance("AES/GCM/NoPadding").apply {
                init(
                    Cipher.ENCRYPT_MODE,
                    SecretKeySpec(keyBytes, "AES"),
                    GCMParameterSpec(GCM_TAG_LENGTH_BITS, nonce)
                )
                updateAAD(header)
            }

            output.write(header)
            CipherOutputStream(NonClosingOutputStream(output), cipher).use { encryptedOutput ->
                GZIPOutputStream(encryptedOutput).use { compressedOutput ->
                    copyBounded(input, compressedOutput, maxPlaintextBytes)
                }
            }
            output.flush()
        } finally {
            Arrays.fill(keyBytes, 0)
            Arrays.fill(salt, 0)
            Arrays.fill(nonce, 0)
        }
    }

    /**
     * Authenticates the archive and validates compression and size limits before writing
     * plaintext to [output]. The caller must separately validate the decrypted payload schema.
     * [stagingDirectory] must be an app-private cache directory in Android production code.
     * The caller owns and must close both streams and clear [passphrase] after this call.
     * This method does not make Room restore atomic; integration must restore into temporary
     * storage, validate it, and replace durable state only after the complete operation succeeds.
     */
    fun decrypt(
        input: InputStream,
        output: OutputStream,
        passphrase: CharArray,
        stagingDirectory: File,
        maxPlaintextBytes: Long = DEFAULT_MAX_PLAINTEXT_BYTES
    ) {
        requireValidPassphrase(passphrase)
        require(maxPlaintextBytes > 0) { "Maximum plaintext size must be positive." }
        require(stagingDirectory.isDirectory) { "Backup staging directory must exist and be a directory." }

        val dataInput = DataInputStream(input)
        val parsedHeader = readHeader(dataInput)
        val keyBytes = deriveKey(passphrase, parsedHeader.salt, parsedHeader.iterations)
        val stagingFiles = mutableListOf<File>()

        try {
            val compressedStage = createPrivateTempFile(stagingDirectory, "compressed")
                .also(stagingFiles::add)
            val plaintextStage = createPrivateTempFile(stagingDirectory, "plaintext")
                .also(stagingFiles::add)
            val cipher = Cipher.getInstance("AES/GCM/NoPadding").apply {
                init(
                    Cipher.DECRYPT_MODE,
                    SecretKeySpec(keyBytes, "AES"),
                    GCMParameterSpec(GCM_TAG_LENGTH_BITS, parsedHeader.nonce)
                )
                updateAAD(parsedHeader.encoded)
            }

            try {
                FileOutputStream(compressedStage).use { stagedOutput ->
                    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                    var encryptedBytes = 0L
                    val maxEncryptedBytes = safeAdd(maxPlaintextBytes, MAX_ARCHIVE_OVERHEAD_BYTES)
                    while (true) {
                        val count = dataInput.read(buffer)
                        if (count < 0) break
                        encryptedBytes += count
                        if (encryptedBytes > maxEncryptedBytes) {
                            throw InvalidBackupException("Backup archive exceeds the supported size limit.")
                        }
                        cipher.update(buffer, 0, count)?.let(stagedOutput::write)
                    }
                    cipher.doFinal()?.let(stagedOutput::write)
                }
            } catch (error: GeneralSecurityException) {
                throw InvalidBackupException("Backup password is incorrect or the archive is damaged.", error)
            }

            try {
                FileInputStream(compressedStage).use { compressedInput ->
                    GZIPInputStream(compressedInput).use { decompressedInput ->
                        FileOutputStream(plaintextStage).use { stagedOutput ->
                            copyBounded(decompressedInput, stagedOutput, maxPlaintextBytes)
                        }
                    }
                }
            } catch (error: InvalidBackupException) {
                throw error
            } catch (error: IOException) {
                throw InvalidBackupException("Backup payload is damaged or uses an unsupported compression format.", error)
            }

            FileInputStream(plaintextStage).use { stagedInput ->
                stagedInput.copyTo(output)
            }
            output.flush()
        } catch (error: GeneralSecurityException) {
            throw InvalidBackupException("This device cannot decrypt the backup archive.", error)
        } finally {
            Arrays.fill(keyBytes, 0)
            Arrays.fill(parsedHeader.salt, 0)
            Arrays.fill(parsedHeader.nonce, 0)
            stagingFiles.forEach(::deleteStagingFile)
        }
    }

    fun encryptBytes(plaintext: ByteArray, passphrase: CharArray): ByteArray {
        val output = ByteArrayOutputStream()
        encrypt(ByteArrayInputStream(plaintext), output, passphrase, maxPlaintextBytes = plaintext.size.toLong().coerceAtLeast(1L))
        return output.toByteArray()
    }

    fun decryptBytes(
        archive: ByteArray,
        passphrase: CharArray,
        stagingDirectory: File,
        maxPlaintextBytes: Long = DEFAULT_MAX_PLAINTEXT_BYTES
    ): ByteArray {
        val output = ByteArrayOutputStream()
        decrypt(ByteArrayInputStream(archive), output, passphrase, stagingDirectory, maxPlaintextBytes)
        return output.toByteArray()
    }

    private fun createHeader(iterations: Int, salt: ByteArray, nonce: ByteArray): ByteArray {
        return ByteArrayOutputStream().use { buffer ->
            DataOutputStream(buffer).use { output ->
                output.write(magic)
                output.writeInt(FORMAT_VERSION)
                output.writeByte(KDF_ID_PBKDF2_HMAC_SHA256)
                output.writeInt(iterations)
                output.writeByte(salt.size)
                output.writeByte(nonce.size)
                output.write(salt)
                output.write(nonce)
            }
            buffer.toByteArray()
        }
    }

    private fun readHeader(input: DataInputStream): ParsedHeader {
        try {
            val actualMagic = ByteArray(magic.size).also(input::readFully)
            if (!actualMagic.contentEquals(magic)) {
                throw InvalidBackupException("This is not a Community Ledger backup archive.")
            }

            val version = input.readInt()
            if (version != FORMAT_VERSION) {
                throw InvalidBackupException("Backup format version $version is not supported.")
            }

            val kdfId = input.readUnsignedByte()
            if (kdfId != KDF_ID_PBKDF2_HMAC_SHA256) {
                throw InvalidBackupException("Backup key-derivation format is not supported.")
            }

            val iterations = input.readInt()
            if (iterations !in MIN_ACCEPTED_ITERATIONS..MAX_ACCEPTED_ITERATIONS) {
                throw InvalidBackupException("Backup key-derivation work factor is outside the supported range.")
            }

            val saltLength = input.readUnsignedByte()
            val nonceLength = input.readUnsignedByte()
            if (saltLength != SALT_LENGTH || nonceLength != NONCE_LENGTH) {
                throw InvalidBackupException("Backup cryptographic parameters are invalid.")
            }

            val salt = ByteArray(saltLength).also(input::readFully)
            val nonce = ByteArray(nonceLength).also(input::readFully)
            return ParsedHeader(
                iterations = iterations,
                salt = salt,
                nonce = nonce,
                encoded = createHeader(iterations, salt, nonce)
            )
        } catch (error: InvalidBackupException) {
            throw error
        } catch (error: IOException) {
            throw InvalidBackupException("Backup header is incomplete or damaged.", error)
        }
    }

    private fun deriveKey(passphrase: CharArray, salt: ByteArray, iterations: Int): ByteArray {
        val specification = PBEKeySpec(passphrase, salt, iterations, KEY_LENGTH_BITS)
        return try {
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                .generateSecret(specification)
                .encoded
        } catch (error: GeneralSecurityException) {
            throw InvalidBackupException("Secure backup encryption is not supported on this device.", error)
        } finally {
            specification.clearPassword()
        }
    }

    private fun requireValidPassphrase(passphrase: CharArray) {
        require(passphrase.size >= MIN_PASSPHRASE_LENGTH) {
            "Backup password must contain at least $MIN_PASSPHRASE_LENGTH characters; use a strong, unique passphrase."
        }
    }

    private fun copyBounded(input: InputStream, output: OutputStream, maxBytes: Long) {
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var total = 0L
        while (true) {
            val count = input.read(buffer)
            if (count < 0) break
            total += count
            if (total > maxBytes) {
                throw InvalidBackupException("Backup data exceeds the supported size limit.")
            }
            output.write(buffer, 0, count)
        }
    }

    private fun createPrivateTempFile(directory: File, purpose: String): File {
        return File.createTempFile("community-ledger-$purpose-", ".tmp", directory).apply {
            setReadable(false, false)
            setWritable(false, false)
            setExecutable(false, false)
            setReadable(true, true)
            setWritable(true, true)
        }
    }

    private fun deleteStagingFile(file: File) {
        if (file.delete() || !file.exists()) return

        runCatching {
            FileOutputStream(file, false).use { output ->
                output.fd.sync()
            }
        }
        file.delete()
    }

    private fun safeAdd(left: Long, right: Long): Long {
        return if (left > Long.MAX_VALUE - right) Long.MAX_VALUE else left + right
    }

    private data class ParsedHeader(
        val iterations: Int,
        val salt: ByteArray,
        val nonce: ByteArray,
        val encoded: ByteArray
    )

    private class NonClosingOutputStream(output: OutputStream) : FilterOutputStream(output) {
        override fun close() {
            flush()
        }
    }

}