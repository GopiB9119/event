package com.communityledger.app.recovery

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class LedgerBackupManifestCodecTest {

    @Test
    fun deterministicEncodingSortsSourceRowsAndRoundTrips() {
        val payload = validPayload().copy(events = validPayload().events.reversed())

        val first = LedgerBackupManifestCodec.encode(payload)
        val second = LedgerBackupManifestCodec.encode(payload.copy(events = payload.events.reversed()))
        val decoded = LedgerBackupManifestCodec.decode(first)

        assertArrayEquals(first, second)
        assertEquals(listOf(1, 2), decoded.events.map { it.sourceId })
        assertEquals(payload.copy(events = payload.events.sortedBy { it.sourceId }), decoded)
    }

    @Test
    fun duplicateUnknownAndMissingFieldsFailClosed() {
        val valid = LedgerBackupManifestCodec.encode(validPayload()).toString(Charsets.UTF_8)
        val duplicate = valid.replaceFirst("\"payloadVersion\":1", "\"payloadVersion\":1,\"payloadVersion\":1")
        val unknown = valid.replaceFirst("\"payloadVersion\":1", "\"unknown\":1,\"payloadVersion\":1")
        val missing = valid.replaceFirst("\"backupId\":\"$BACKUP_ID\",", "")

        listOf(duplicate, unknown, missing).forEach { text ->
            assertThrows(InvalidLedgerBackupManifestException::class.java) {
                LedgerBackupManifestCodec.decode(text.toByteArray())
            }
        }
    }

    @Test
    fun malformedUtf8TrailingJsonAndOversizedInputFailClosed() {
        val valid = LedgerBackupManifestCodec.encode(validPayload())

        assertThrows(InvalidLedgerBackupManifestException::class.java) {
            LedgerBackupManifestCodec.decode(byteArrayOf(0xC3.toByte(), 0x28))
        }
        assertThrows(InvalidLedgerBackupManifestException::class.java) {
            LedgerBackupManifestCodec.decode(valid + "{}".toByteArray())
        }
        assertThrows(InvalidLedgerBackupManifestException::class.java) {
            LedgerBackupManifestCodec.decode(ByteArray(LedgerBackupManifestCodec.MAX_MANIFEST_BYTES + 1))
        }
    }

    @Test
    fun integerFieldsUseExactPlainDecimalParsing() {
        val valid = LedgerBackupManifestCodec.encode(validPayload()).toString(Charsets.UTF_8)
        val maximum = valid.replaceFirst("\"byteCount\":512", "\"byteCount\":${Long.MAX_VALUE}")
        val minimum = valid.replaceFirst(
            "\"createdAtEpochMillis\":1789000000000",
            "\"createdAtEpochMillis\":${Long.MIN_VALUE}"
        )
        val overflow = valid.replaceFirst("\"byteCount\":512", "\"byteCount\":9223372036854775808")
        val fractional = valid.replaceFirst("\"byteCount\":512", "\"byteCount\":512.0")
        val exponent = valid.replaceFirst("\"byteCount\":512", "\"byteCount\":5e2")

        assertEquals(Long.MAX_VALUE, LedgerBackupManifestCodec.decode(maximum.toByteArray()).events.first().receiptEvidence.single().byteCount)
        listOf(minimum, overflow, fractional, exponent).forEach { text ->
            assertThrows(InvalidLedgerBackupManifestException::class.java) {
                LedgerBackupManifestCodec.decode(text.toByteArray())
            }
        }
    }

    @Test
    fun semanticAndStringBoundFailuresAreRejectedBeforePackaging() {
        val invalid = validPayload().copy(backupId = "not-a-backup-id")
        val oversized = validPayload().copy(
            events = validPayload().events.mapIndexed { index, event ->
                if (index == 0) event.copy(title = "x".repeat(4 * 1024 + 1)) else event
            }
        )

        assertThrows(InvalidLedgerBackupManifestException::class.java) {
            LedgerBackupManifestCodec.encode(invalid)
        }
        assertThrows(InvalidLedgerBackupManifestException::class.java) {
            LedgerBackupManifestCodec.encode(oversized)
        }
    }

    @Test
    fun canonicalEncodingContainsNoSourceDeviceReceiptPath() {
        val text = LedgerBackupManifestCodec.encode(validPayload()).toString(Charsets.UTF_8)

        assertTrue(!text.contains("receiptFilePath"))
        assertTrue(!text.contains("/data/user/"))
    }

    @Test
    fun aggregateManifestLimitFailsDuringBoundedEncoding() {
        val payload = validPayload()
        val event = payload.events.first()
        val largeManualNote = "x".repeat(256 * 1024)
        val transactions = (1..65).map { index ->
            event.transactions.single().copy(
                sourceId = 1_000 + index,
                paymentReference = "",
                manualNotes = largeManualNote,
                receiptEvidenceId = null
            )
        }
        val oversized = payload.copy(
            events = listOf(
                event.copy(
                    transactions = transactions,
                    receiptEvidence = emptyList()
                )
            )
        )

        assertThrows(InvalidLedgerBackupManifestException::class.java) {
            LedgerBackupManifestCodec.encode(oversized)
        }
    }

    @Test
    fun streamingApisRoundTripWithoutClosingCallerOutput() {
        val output = TrackingOutputStream()

        LedgerBackupManifestCodec.encode(validPayload(), output)
        val decoded = LedgerBackupManifestCodec.decode(ByteArrayInputStream(output.toByteArray()))

        assertEquals(validPayload(), decoded)
        assertTrue(!output.closed)
    }

    @Test
    fun streamedInputOverByteLimitFailsClosed() {
        val bytes = ByteArray(LedgerBackupManifestCodec.MAX_MANIFEST_BYTES + 1) { ' '.code.toByte() }

        assertThrows(InvalidLedgerBackupManifestException::class.java) {
            LedgerBackupManifestCodec.decode(ByteArrayInputStream(bytes))
        }
    }

    @Test
    fun loneUnicodeSurrogatesAreRejectedInsteadOfReplaced() {
        val highSurrogate = validPayload().copy(
            events = validPayload().events.mapIndexed { index, event ->
                if (index == 0) event.copy(title = "Broken \uD800 title") else event
            }
        )
        val lowSurrogate = validPayload().copy(
            events = validPayload().events.mapIndexed { index, event ->
                if (index == 0) event.copy(title = "Broken \uDC00 title") else event
            }
        )

        listOf(highSurrogate, lowSurrogate).forEach { payload ->
            assertThrows(InvalidLedgerBackupManifestException::class.java) {
                LedgerBackupManifestCodec.encode(payload)
            }
        }
    }

    private class TrackingOutputStream : ByteArrayOutputStream() {
        var closed = false

        override fun close() {
            closed = true
            super.close()
        }
    }

    private fun validPayload(): LedgerBackupPayload {
        val evidenceId = "22222222222222222222222222222222"
        return LedgerBackupPayload(
            backupId = BACKUP_ID,
            createdAtEpochMillis = 1_789_000_000_000L,
            sourcePreferences = LedgerBackupSourcePreferences(
                userEmail = "organizer@example.com",
                themeMode = "System"
            ),
            events = listOf(
                LedgerBackupEvent(
                    sourceId = 1,
                    eventKey = "0123456789abcdef0123456789abcdef",
                    title = "Community Festival",
                    duration = "2 days",
                    createdDate = 1_788_000_000_000L,
                    isPrivate = true,
                    customFieldsJson = "{}",
                    members = listOf(
                        LedgerBackupMember(
                            sourceId = 11,
                            name = "Asha",
                            normalizedName = "asha",
                            phone = "",
                            email = "asha@example.com",
                            role = "Donor",
                            createdAt = 1_788_000_000_100L
                        )
                    ),
                    transactions = listOf(
                        LedgerBackupTransaction(
                            sourceId = 101,
                            sourceMemberId = 11,
                            personName = "Asha",
                            personPhone = "",
                            personEmail = "asha@example.com",
                            amount = 500.0,
                            type = "Donated",
                            date = 1_788_000_000_200L,
                            paymentReference = "UPI-101",
                            isVerified = true,
                            manualNotes = null,
                            uploaderEmail = "organizer@example.com",
                            receiptEvidenceId = evidenceId
                        )
                    ),
                    receiptEvidence = listOf(
                        LedgerBackupReceiptEvidence(
                            id = evidenceId,
                            sourceTransactionId = 101,
                            sha256 = "a".repeat(64),
                            byteCount = 512
                        )
                    )
                ),
                LedgerBackupEvent(
                    sourceId = 2,
                    eventKey = "fedcba9876543210fedcba9876543210",
                    title = "Second Event",
                    duration = null,
                    createdDate = 1_788_000_001_000L,
                    isPrivate = false,
                    customFieldsJson = "{}",
                    members = emptyList(),
                    transactions = emptyList(),
                    receiptEvidence = emptyList()
                )
            )
        )
    }

    private companion object {
        const val BACKUP_ID = "11111111111111111111111111111111"
    }
}