package com.communityledger.app.recovery

import android.content.Context
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.security.SecureRandom

object EncryptedLedgerBackupPackageCodec {

    /**
     * Validates and stages the plaintext package before encryption. The caller owns [output],
     * must clear [passphrase], and must publish the destination atomically only after success.
     * Plaintext staging is derived internally from [Context.getCacheDir].
     */
    fun encrypt(
        backupPackage: LedgerBackupPackage,
        output: OutputStream,
        passphrase: CharArray,
        context: Context,
        secureRandom: SecureRandom = SecureRandom()
    ) {
        val stagingDirectory = createPrivateStagingDirectory(context)
        try {
            val plaintextStage = createPrivateTempFile(stagingDirectory)
            try {
                FileOutputStream(plaintextStage).use { stagedOutput ->
                    LedgerBackupPackageCodec.encode(backupPackage, stagedOutput)
                    stagedOutput.fd.sync()
                }
                FileInputStream(plaintextStage).use { stagedInput ->
                    EncryptedBackupCodec.encrypt(
                        input = stagedInput,
                        output = output,
                        passphrase = passphrase,
                        maxPlaintextBytes = LedgerBackupPackageCodec.MAX_PACKAGE_BYTES.toLong(),
                        secureRandom = secureRandom
                    )
                }
            } finally {
                eraseAndDelete(plaintextStage)
            }
        } finally {
            deleteOwnedStagingDirectory(stagingDirectory)
        }
    }

    /**
     * Authenticates, decrypts, and fully validates one package before returning it. The caller
     * owns [input] and must clear [passphrase]. No Room state or receipt files are changed.
     */
    fun decrypt(
        input: InputStream,
        passphrase: CharArray,
        context: Context
    ): LedgerBackupPackage {
        val stagingDirectory = createPrivateStagingDirectory(context)
        try {
            val plaintextStage = createPrivateTempFile(stagingDirectory)
            try {
                FileOutputStream(plaintextStage).use { stagedOutput ->
                    EncryptedBackupCodec.decrypt(
                        input = input,
                        output = stagedOutput,
                        passphrase = passphrase,
                        stagingDirectory = stagingDirectory,
                        maxPlaintextBytes = LedgerBackupPackageCodec.MAX_PACKAGE_BYTES.toLong()
                    )
                    stagedOutput.fd.sync()
                }
                return FileInputStream(plaintextStage).use(LedgerBackupPackageCodec::decode)
            } finally {
                eraseAndDelete(plaintextStage)
            }
        } finally {
            deleteOwnedStagingDirectory(stagingDirectory)
        }
    }

    private fun createPrivateStagingDirectory(context: Context): File {
        val cacheRoot = context.cacheDir.canonicalFile
        require(cacheRoot.isDirectory) {
            "Application cache directory must exist before recovery staging."
        }
        val marker = File.createTempFile("community-ledger-recovery-", ".tmp", cacheRoot)
        if (!marker.delete() || !marker.mkdir()) {
            marker.delete()
            throw IllegalStateException("Could not create private recovery staging.")
        }
        val directory = marker.canonicalFile
        if (directory.parentFile != cacheRoot) {
            directory.delete()
            throw IllegalStateException("Recovery staging escaped the application cache directory.")
        }
        directory.setReadable(false, false)
        directory.setWritable(false, false)
        directory.setExecutable(false, false)
        directory.setReadable(true, true)
        directory.setWritable(true, true)
        directory.setExecutable(true, true)
        return directory
    }

    private fun createPrivateTempFile(directory: File): File {
        return File.createTempFile("community-ledger-package-", ".tmp", directory).apply {
            setReadable(false, false)
            setWritable(false, false)
            setExecutable(false, false)
            setReadable(true, true)
            setWritable(true, true)
        }
    }

    private fun eraseAndDelete(file: File) {
        if (!file.exists()) return
        runCatching {
            FileOutputStream(file, false).use { output -> output.fd.sync() }
        }
        file.delete()
    }

    private fun deleteOwnedStagingDirectory(directory: File) {
        directory.listFiles().orEmpty().forEach(::eraseAndDelete)
        directory.delete()
    }
}