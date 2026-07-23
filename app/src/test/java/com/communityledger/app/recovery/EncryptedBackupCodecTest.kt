package com.communityledger.app.recovery

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class EncryptedBackupCodecTest {

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    private val passphrase = "correct horse battery staple".toCharArray()

    @Test
    fun roundTripPreservesUnicodePayload() {
        val payload = "Community Ledger | తెలుగు | हिन्दी | ₹12,345.67".toByteArray(StandardCharsets.UTF_8)

        val archive = EncryptedBackupCodec.encryptBytes(payload, passphrase)
        val restored = decryptBytes(archive, passphrase)

        assertArrayEquals(payload, restored)
        assertFalse(archive.toString(StandardCharsets.UTF_8).contains("Community Ledger"))
    }

    @Test
    fun repeatedEncryptionUsesDifferentSaltAndNonce() {
        val payload = "same ledger".toByteArray()

        val first = EncryptedBackupCodec.encryptBytes(payload, passphrase)
        val second = EncryptedBackupCodec.encryptBytes(payload, passphrase)

        assertFalse(first.contentEquals(second))
        assertArrayEquals(payload, decryptBytes(first, passphrase))
        assertArrayEquals(payload, decryptBytes(second, passphrase))
    }

    @Test
    fun wrongPassphraseFailsClosed() {
        val archive = EncryptedBackupCodec.encryptBytes("ledger".toByteArray(), passphrase)

        assertThrows(InvalidBackupException::class.java) {
            decryptBytes(archive, "wrong password value".toCharArray())
        }
    }

    @Test
    fun tamperedCiphertextFailsClosed() {
        val archive = EncryptedBackupCodec.encryptBytes("ledger".toByteArray(), passphrase)
        archive[archive.lastIndex - 4] = (archive[archive.lastIndex - 4].toInt() xor 0x01).toByte()

        assertThrows(InvalidBackupException::class.java) {
            decryptBytes(archive, passphrase)
        }
    }

    @Test
    fun tamperedArchiveLeavesCallerOutputEmpty() {
        val archive = EncryptedBackupCodec.encryptBytes("private ledger".toByteArray(), passphrase)
        archive[archive.lastIndex - 4] = (archive[archive.lastIndex - 4].toInt() xor 0x01).toByte()
        val output = ByteArrayOutputStream()

        assertThrows(InvalidBackupException::class.java) {
            EncryptedBackupCodec.decrypt(
                input = ByteArrayInputStream(archive),
                output = output,
                passphrase = passphrase,
                stagingDirectory = temporaryFolder.root
            )
        }

        assertArrayEquals(ByteArray(0), output.toByteArray())
        assertTrue(temporaryFolder.root.listFiles().orEmpty().isEmpty())
    }

    @Test
    fun appendedDataIsRejectedAndLeavesOutputEmpty() {
        val archive = EncryptedBackupCodec.encryptBytes("private ledger".toByteArray(), passphrase)
        val appended = archive + byteArrayOf(1, 2, 3, 4)
        val output = ByteArrayOutputStream()

        assertThrows(InvalidBackupException::class.java) {
            EncryptedBackupCodec.decrypt(
                input = ByteArrayInputStream(appended),
                output = output,
                passphrase = passphrase,
                stagingDirectory = temporaryFolder.root
            )
        }

        assertArrayEquals(ByteArray(0), output.toByteArray())
    }

    @Test
    fun tamperedHeaderFailsBeforeKeyDerivationWorkCanEscalate() {
        val archive = EncryptedBackupCodec.encryptBytes("ledger".toByteArray(), passphrase)
        archive[9] = 0
        archive[10] = 0
        archive[11] = 0
        archive[12] = 1

        val error = assertThrows(InvalidBackupException::class.java) {
            decryptBytes(archive, passphrase)
        }

        assertTrue(error.message.orEmpty().contains("work factor"))
    }

    @Test
    fun truncatedArchiveFailsClosed() {
        val archive = EncryptedBackupCodec.encryptBytes("ledger".toByteArray(), passphrase)

        assertThrows(InvalidBackupException::class.java) {
            decryptBytes(archive.copyOf(archive.size / 2), passphrase)
        }
    }

    @Test
    fun decompressionLimitRejectsOversizedPayload() {
        val payload = ByteArray(16_384) { 7 }
        val archive = EncryptedBackupCodec.encryptBytes(payload, passphrase)

        assertThrows(InvalidBackupException::class.java) {
            decryptBytes(archive, passphrase, maxPlaintextBytes = 1_024)
        }
    }

    @Test
    fun arbitraryBinaryPayloadRoundTrips() {
        val payload = ByteArray(4_096) { index -> (index % 256).toByte() }

        val archive = EncryptedBackupCodec.encryptBytes(payload, passphrase)
        val restored = decryptBytes(archive, passphrase)

        assertArrayEquals(payload, restored)
    }

    @Test
    fun exactPlaintextLimitIsAccepted() {
        val payload = ByteArray(1_024) { 42 }
        val archive = EncryptedBackupCodec.encryptBytes(payload, passphrase)
        val restored = decryptBytes(
            archive,
            passphrase,
            maxPlaintextBytes = payload.size.toLong()
        )

        assertArrayEquals(payload, restored)
    }

    @Test
    fun unsupportedVersionAndKdfAreRejectedBeforeDecryption() {
        val versionArchive = EncryptedBackupCodec.encryptBytes("ledger".toByteArray(), passphrase)
        versionArchive[7] = 2
        val kdfArchive = EncryptedBackupCodec.encryptBytes("ledger".toByteArray(), passphrase)
        kdfArchive[8] = 2

        assertThrows(InvalidBackupException::class.java) {
            decryptBytes(versionArchive, passphrase)
        }
        assertThrows(InvalidBackupException::class.java) {
            decryptBytes(kdfArchive, passphrase)
        }
    }

    @Test
    fun invalidSaltAndNonceLengthsAreRejected() {
        val saltArchive = EncryptedBackupCodec.encryptBytes("ledger".toByteArray(), passphrase)
        saltArchive[13] = 17
        val nonceArchive = EncryptedBackupCodec.encryptBytes("ledger".toByteArray(), passphrase)
        nonceArchive[14] = 13

        assertThrows(InvalidBackupException::class.java) {
            decryptBytes(saltArchive, passphrase)
        }
        assertThrows(InvalidBackupException::class.java) {
            decryptBytes(nonceArchive, passphrase)
        }
    }

    @Test
    fun emptyPayloadAndMinimumPassphraseRoundTrip() {
        val minimumPassphrase = "12345678901X".toCharArray()

        val archive = EncryptedBackupCodec.encryptBytes(ByteArray(0), minimumPassphrase)
        val restored = decryptBytes(archive, minimumPassphrase)

        assertArrayEquals(ByteArray(0), restored)
    }

    @Test
    fun weakPassphraseIsRejected() {
        assertThrows(IllegalArgumentException::class.java) {
            EncryptedBackupCodec.encryptBytes("ledger".toByteArray(), "too short".toCharArray())
        }
    }

    @Test
    fun authenticatedInvalidCompressionLeavesCallerOutputEmpty() {
        val archive = authenticatedArchive("not a gzip payload".toByteArray(), passphrase)
        val output = ByteArrayOutputStream()

        assertThrows(InvalidBackupException::class.java) {
            EncryptedBackupCodec.decrypt(
                input = ByteArrayInputStream(archive),
                output = output,
                passphrase = passphrase,
                stagingDirectory = temporaryFolder.root
            )
        }

        assertArrayEquals(ByteArray(0), output.toByteArray())
        assertTrue(temporaryFolder.root.listFiles().orEmpty().isEmpty())
    }

    @Test
    fun nonDirectoryStagingPathIsRejectedBeforeOutput() {
        val archive = EncryptedBackupCodec.encryptBytes("ledger".toByteArray(), passphrase)
        val output = ByteArrayOutputStream()

        assertThrows(IllegalArgumentException::class.java) {
            EncryptedBackupCodec.decrypt(
                input = ByteArrayInputStream(archive),
                output = output,
                passphrase = passphrase,
                stagingDirectory = temporaryFolder.newFile("not-a-directory")
            )
        }

        assertArrayEquals(ByteArray(0), output.toByteArray())
    }

    private fun decryptBytes(
        archive: ByteArray,
        password: CharArray,
        maxPlaintextBytes: Long = EncryptedBackupCodec.DEFAULT_MAX_PLAINTEXT_BYTES
    ): ByteArray = EncryptedBackupCodec.decryptBytes(
        archive = archive,
        passphrase = password,
        stagingDirectory = temporaryFolder.root,
        maxPlaintextBytes = maxPlaintextBytes
    )

    private fun authenticatedArchive(payload: ByteArray, password: CharArray): ByteArray {
        val salt = ByteArray(16) { index -> (index + 1).toByte() }
        val nonce = ByteArray(12) { index -> (index + 17).toByte() }
        val header = ByteArrayOutputStream().use { buffer ->
            DataOutputStream(buffer).use { output ->
                output.write(byteArrayOf('C'.code.toByte(), 'L'.code.toByte(), 'B'.code.toByte(), 'K'.code.toByte()))
                output.writeInt(EncryptedBackupCodec.FORMAT_VERSION)
                output.writeByte(1)
                output.writeInt(EncryptedBackupCodec.PBKDF2_ITERATIONS)
                output.writeByte(salt.size)
                output.writeByte(nonce.size)
                output.write(salt)
                output.write(nonce)
            }
            buffer.toByteArray()
        }
        val specification = PBEKeySpec(password, salt, EncryptedBackupCodec.PBKDF2_ITERATIONS, 256)
        val key = try {
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                .generateSecret(specification)
                .encoded
        } finally {
            specification.clearPassword()
        }
        val encryptedPayload = try {
            Cipher.getInstance("AES/GCM/NoPadding").run {
                init(Cipher.ENCRYPT_MODE, SecretKeySpec(key, "AES"), GCMParameterSpec(128, nonce))
                updateAAD(header)
                doFinal(payload)
            }
        } finally {
            key.fill(0)
        }
        return header + encryptedPayload
    }
}