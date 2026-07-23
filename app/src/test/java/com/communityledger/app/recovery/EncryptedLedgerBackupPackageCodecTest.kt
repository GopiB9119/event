package com.communityledger.app.recovery

import android.content.Context
import android.content.ContextWrapper
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.security.MessageDigest
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import androidx.test.core.app.ApplicationProvider
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class EncryptedLedgerBackupPackageCodecTest {

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    private val passphrase = "correct horse battery staple".toCharArray()
    private val context: Context by lazy {
        object : ContextWrapper(ApplicationProvider.getApplicationContext()) {
            override fun getCacheDir() = temporaryFolder.root
        }
    }

    @Test
    fun encryptedPackageRoundTripsWithoutExposingPlaintextOrClosingCallerStreams() {
        val backupPackage = validPackage()
        val output = TrackingOutputStream()

        EncryptedLedgerBackupPackageCodec.encrypt(
            backupPackage = backupPackage,
            output = output,
            passphrase = passphrase,
            context = context
        )

        assertFalse(output.closed)
        assertFalse(output.toByteArray().toString(Charsets.UTF_8).contains("Community Festival"))
        assertTrue(temporaryFolder.root.listFiles().orEmpty().isEmpty())

        val input = TrackingInputStream(output.toByteArray())
        val decoded = EncryptedLedgerBackupPackageCodec.decrypt(
            input = input,
            passphrase = passphrase,
            context = context
        )

        assertFalse(input.closed)
        assertEquals(backupPackage.payload, decoded.payload)
        assertEquals(backupPackage.receiptEvidence.single().descriptor, decoded.receiptEvidence.single().descriptor)
        assertArrayEquals(backupPackage.receiptEvidence.single().bytes, decoded.receiptEvidence.single().bytes)
        assertTrue(temporaryFolder.root.listFiles().orEmpty().isEmpty())
    }

    @Test
    fun tamperedArchiveAndAuthenticatedInvalidFrameLeaveNoPlaintextStage() {
        val backupPackage = validPackage()
        val encrypted = ByteArrayOutputStream().also { output ->
            EncryptedLedgerBackupPackageCodec.encrypt(
                backupPackage,
                output,
                passphrase,
                context
            )
        }.toByteArray()
        encrypted[encrypted.lastIndex - 4] = (encrypted[encrypted.lastIndex - 4].toInt() xor 1).toByte()

        assertThrows(InvalidBackupException::class.java) {
            EncryptedLedgerBackupPackageCodec.decrypt(
                ByteArrayInputStream(encrypted),
                passphrase,
                context
            )
        }
        assertTrue(temporaryFolder.root.listFiles().orEmpty().isEmpty())

        val invalidFrame = LedgerBackupPackageCodec.encode(backupPackage) + byteArrayOf(1)
        val authenticatedInvalidFrame = EncryptedBackupCodec.encryptBytes(invalidFrame, passphrase)
        assertThrows(InvalidLedgerBackupPackageException::class.java) {
            EncryptedLedgerBackupPackageCodec.decrypt(
                ByteArrayInputStream(authenticatedInvalidFrame),
                passphrase,
                context
            )
        }
        assertTrue(temporaryFolder.root.listFiles().orEmpty().isEmpty())
    }

    @Test
    fun invalidPackageIsRejectedBeforeEncryptedOutputIsWritten() {
        val backupPackage = validPackage()
        val invalidEvidence = backupPackage.receiptEvidence.single().copy(
            bytes = "tampered evidence".toByteArray()
        )
        val output = ByteArrayOutputStream()

        assertThrows(InvalidLedgerBackupPackageException::class.java) {
            EncryptedLedgerBackupPackageCodec.encrypt(
                backupPackage.copy(receiptEvidence = listOf(invalidEvidence)),
                output,
                passphrase,
                context
            )
        }

        assertEquals(0, output.size())
        assertTrue(temporaryFolder.root.listFiles().orEmpty().isEmpty())
    }

    @Test
    fun invalidPrivateCacheRootIsRejectedBeforeOutput() {
        val invalidRoot = temporaryFolder.newFile("not-a-directory")
        val invalidContext = object : ContextWrapper(ApplicationProvider.getApplicationContext()) {
            override fun getCacheDir() = invalidRoot
        }
        val output = ByteArrayOutputStream()

        assertThrows(IllegalArgumentException::class.java) {
            EncryptedLedgerBackupPackageCodec.encrypt(
                validPackage(),
                output,
                passphrase,
                invalidContext
            )
        }

        assertEquals(0, output.size())
    }

    private fun validPackage(): LedgerBackupPackage {
        val bytes = "{\"receipt\":\"verified\"}".toByteArray()
        val hash = MessageDigest.getInstance("SHA-256")
            .digest(bytes)
            .joinToString("") { byte -> "%02x".format(byte) }
        val descriptor = LedgerBackupReceiptEvidence(
            id = hash.take(32),
            sourceTransactionId = 101,
            sha256 = hash,
            byteCount = bytes.size.toLong()
        )
        val payload = LedgerBackupPayload(
            backupId = "11111111111111111111111111111111",
            createdAtEpochMillis = 1_789_000_000_000L,
            sourcePreferences = LedgerBackupSourcePreferences("organizer@example.com", "System"),
            events = listOf(
                LedgerBackupEvent(
                    sourceId = 1,
                    eventKey = "0123456789abcdef0123456789abcdef",
                    title = "Community Festival",
                    duration = null,
                    createdDate = 1_788_000_000_000L,
                    isPrivate = true,
                    customFieldsJson = "{}",
                    members = emptyList(),
                    transactions = listOf(
                        LedgerBackupTransaction(
                            sourceId = 101,
                            sourceMemberId = null,
                            personName = "Asha",
                            personPhone = "",
                            personEmail = "",
                            amount = 500.0,
                            type = "Donated",
                            date = 1_788_000_000_200L,
                            paymentReference = "UPI-101",
                            isVerified = true,
                            manualNotes = null,
                            uploaderEmail = "organizer@example.com",
                            receiptEvidenceId = descriptor.id
                        )
                    ),
                    receiptEvidence = listOf(descriptor)
                )
            )
        )
        return LedgerBackupPackage(payload, listOf(CanonicalReceiptEvidence(descriptor, bytes)))
    }

    private class TrackingOutputStream : ByteArrayOutputStream() {
        var closed = false

        override fun close() {
            closed = true
            super.close()
        }
    }

    private class TrackingInputStream(bytes: ByteArray) : ByteArrayInputStream(bytes) {
        var closed = false

        override fun close() {
            closed = true
            super.close()
        }
    }
}