package com.communityledger.app.recovery

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.security.MessageDigest
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class LedgerBackupPackageCodecTest {

    @Test
    fun deterministicFrameSortsEvidenceAndRoundTripsWithoutClosingStreams() {
        val backupPackage = validPackage()
        val firstOutput = TrackingOutputStream()
        LedgerBackupPackageCodec.encode(backupPackage, firstOutput)
        val second = LedgerBackupPackageCodec.encode(
            backupPackage.copy(receiptEvidence = backupPackage.receiptEvidence.reversed())
        )

        assertArrayEquals(firstOutput.toByteArray(), second)
        assertFalse(firstOutput.closed)

        val input = TrackingInputStream(firstOutput.toByteArray())
        val decoded = LedgerBackupPackageCodec.decode(input)
        assertFalse(input.closed)
        assertEquals(listOf(101, 202), decoded.receiptEvidence.map { it.descriptor.sourceTransactionId })
        decoded.receiptEvidence.zip(backupPackage.receiptEvidence.sortedBy { it.descriptor.sourceTransactionId })
            .forEach { (actual, expected) ->
                assertEquals(expected.descriptor, actual.descriptor)
                assertArrayEquals(expected.bytes, actual.bytes)
            }
    }

    @Test
    fun versionOneSchemaFiveFrameMatchesCompatibilityHashAndDecodes() {
        val currentPackage = validPackage()
        val schemaFivePackage = currentPackage.copy(
            payload = currentPackage.payload.copy(sourceSchemaVersion = 5)
        )
        val frame = LedgerBackupPackageCodec.encode(schemaFivePackage)
        val hash = MessageDigest.getInstance("SHA-256")
            .digest(frame)
            .joinToString("") { byte -> "%02x".format(byte) }

        assertEquals(VERSION_ONE_PACKAGE_SHA256, hash)
        assertEquals(5, LedgerBackupPackageCodec.decode(frame).payload.sourceSchemaVersion)
    }

    @Test
    fun identicalCanonicalEvidenceInDifferentEventsRoundTripsByTransactionIdentity() {
        val first = canonicalEvidence(101, "{\"receipt\":\"same\"}".toByteArray())
        val second = canonicalEvidence(202, first.bytes.copyOf())
        val firstEvent = eventFor(1, "0123456789abcdef0123456789abcdef", first)
        val secondEvent = eventFor(2, "fedcba9876543210fedcba9876543210", second)
        val payload = basePayload(listOf(firstEvent, secondEvent))

        val decoded = LedgerBackupPackageCodec.decode(
            LedgerBackupPackageCodec.encode(LedgerBackupPackage(payload, listOf(second, first)))
        )

        assertEquals(listOf(101, 202), decoded.receiptEvidence.map { it.descriptor.sourceTransactionId })
        assertEquals(1, decoded.receiptEvidence.map { it.descriptor.id }.distinct().size)
        decoded.receiptEvidence.forEach { assertArrayEquals(first.bytes, it.bytes) }
    }

    @Test
    fun badMagicVersionMissingDuplicateAndTrailingRecordsFailClosed() {
        val valid = LedgerBackupPackageCodec.encode(validPackage())
        val firstRecordOffset = firstRecordOffset(valid)
        val firstRecordLength = readInt(valid, firstRecordOffset + 36)
        val secondRecordOffset = firstRecordOffset + 40 + firstRecordLength

        val badMagic = valid.copyOf().also { it[0] = 'X'.code.toByte() }
        val badVersion = valid.copyOf().also { writeInt(it, 4, 2) }
        val missing = valid.copyOf().also { writeInt(it, 12, 1) }
        val duplicate = valid.copyOf().also {
            System.arraycopy(it, firstRecordOffset, it, secondRecordOffset, 32)
            writeInt(it, secondRecordOffset + 32, readInt(it, firstRecordOffset + 32))
        }
        val trailing = valid + byteArrayOf(1)

        listOf(badMagic, badVersion, missing, duplicate, trailing).forEach { bytes ->
            assertThrows(InvalidLedgerBackupPackageException::class.java) {
                LedgerBackupPackageCodec.decode(bytes)
            }
        }
    }

    @Test
    fun tamperedLengthHashTruncationAndManifestFailClosed() {
        val valid = LedgerBackupPackageCodec.encode(validPackage())
        val firstRecordOffset = firstRecordOffset(valid)
        val lengthOffset = firstRecordOffset + 36
        val dataOffset = firstRecordOffset + 40

        val wrongLength = valid.copyOf().also { writeInt(it, lengthOffset, readInt(it, lengthOffset) + 1) }
        val badHash = valid.copyOf().also { it[dataOffset] = (it[dataOffset].toInt() xor 1).toByte() }
        val truncated = valid.copyOf(valid.size - 1)
        val malformedManifest = valid.copyOf().also { it[16] = '!'.code.toByte() }

        listOf(wrongLength, badHash, truncated, malformedManifest).forEach { bytes ->
            assertThrows(InvalidLedgerBackupPackageException::class.java) {
                LedgerBackupPackageCodec.decode(bytes)
            }
        }
    }

    @Test
    fun encodeRejectsUnknownDuplicateHashMismatchAndPerFileOverflow() {
        val backupPackage = validPackage()
        val first = backupPackage.receiptEvidence.first()
        val unknownBytes = "unknown evidence".toByteArray()
        val unknown = canonicalEvidence(999, unknownBytes)
        val hashMismatch = first.copy(bytes = first.bytes.copyOf().also { it[0] = 1 })
        val oversizedBytes = ByteArray(LedgerBackupPackageCodec.MAX_RECEIPT_EVIDENCE_BYTES + 1)
        val oversized = canonicalEvidence(first.descriptor.sourceTransactionId, oversizedBytes)

        listOf(
            backupPackage.copy(receiptEvidence = listOf(first, unknown)),
            backupPackage.copy(receiptEvidence = listOf(first, first)),
            backupPackage.copy(receiptEvidence = listOf(hashMismatch, backupPackage.receiptEvidence.last())),
            backupPackage.copy(receiptEvidence = listOf(oversized, backupPackage.receiptEvidence.last()))
        ).forEach { invalid ->
            assertThrows(InvalidLedgerBackupPackageException::class.java) {
                LedgerBackupPackageCodec.encode(invalid)
            }
        }
    }

    @Test
    fun aggregateEvidenceLimitIsEnforcedBeforeFrameWrite() {
        val itemSize = LedgerBackupPackageCodec.MAX_RECEIPT_EVIDENCE_BYTES
        val evidence = (1..33).map { index ->
            canonicalEvidence(index, ByteArray(itemSize) { offset -> (offset + index).toByte() })
        }
        val payload = payloadFor(evidence)

        assertThrows(InvalidLedgerBackupPackageException::class.java) {
            LedgerBackupPackageCodec.encode(LedgerBackupPackage(payload, evidence))
        }
    }

    private fun validPackage(): LedgerBackupPackage {
        val first = canonicalEvidence(101, "{\"receipt\":\"first\"}".toByteArray())
        val second = canonicalEvidence(202, "{\"receipt\":\"second\"}".toByteArray())
        val evidence = listOf(second, first)
        return LedgerBackupPackage(payloadFor(evidence), evidence)
    }

    private fun payloadFor(evidence: List<CanonicalReceiptEvidence>): LedgerBackupPayload {
        return basePayload(
            listOf(
                LedgerBackupEvent(
                    sourceId = 1,
                    eventKey = "0123456789abcdef0123456789abcdef",
                    title = "Community Festival",
                    duration = null,
                    createdDate = 1_788_000_000_000L,
                    isPrivate = true,
                    customFieldsJson = "{}",
                    members = emptyList(),
                    transactions = evidence.map(::transactionFor).reversed(),
                    receiptEvidence = evidence.map(CanonicalReceiptEvidence::descriptor).reversed()
                )
            )
        )
    }

    private fun basePayload(events: List<LedgerBackupEvent>): LedgerBackupPayload =
        LedgerBackupPayload(
            backupId = "11111111111111111111111111111111",
            createdAtEpochMillis = 1_789_000_000_000L,
            sourcePreferences = LedgerBackupSourcePreferences("organizer@example.com", "System"),
            events = events
        )

    private fun eventFor(
        sourceId: Int,
        eventKey: String,
        evidence: CanonicalReceiptEvidence
    ) = LedgerBackupEvent(
        sourceId = sourceId,
        eventKey = eventKey,
        title = "Event $sourceId",
        duration = null,
        createdDate = 1_788_000_000_000L + sourceId,
        isPrivate = true,
        customFieldsJson = "{}",
        members = emptyList(),
        transactions = listOf(transactionFor(evidence)),
        receiptEvidence = listOf(evidence.descriptor)
    )

    private fun transactionFor(item: CanonicalReceiptEvidence) = LedgerBackupTransaction(
        sourceId = item.descriptor.sourceTransactionId,
        sourceMemberId = null,
        personName = "Receipt owner ${item.descriptor.sourceTransactionId}",
        personPhone = "",
        personEmail = "",
        amount = 10.0,
        type = "Credit",
        date = 1_788_000_000_000L + item.descriptor.sourceTransactionId,
        paymentReference = "REF-${item.descriptor.sourceTransactionId}",
        isVerified = true,
        manualNotes = null,
        uploaderEmail = "organizer@example.com",
        receiptEvidenceId = item.descriptor.id
    )

    private fun canonicalEvidence(
        sourceTransactionId: Int,
        bytes: ByteArray
    ): CanonicalReceiptEvidence {
        val hash = MessageDigest.getInstance("SHA-256")
            .digest(bytes)
            .joinToString("") { byte -> "%02x".format(byte) }
        return CanonicalReceiptEvidence(
            LedgerBackupReceiptEvidence(
                id = hash.take(32),
                sourceTransactionId = sourceTransactionId,
                sha256 = hash,
                byteCount = bytes.size.toLong()
            ),
            bytes
        )
    }

    private fun firstRecordOffset(bytes: ByteArray): Int = 16 + readInt(bytes, 8)

    private fun readInt(bytes: ByteArray, offset: Int): Int = ByteBuffer.wrap(bytes, offset, 4).int

    private fun writeInt(bytes: ByteArray, offset: Int, value: Int) {
        ByteBuffer.wrap(bytes, offset, 4).putInt(value)
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

    private companion object {
        const val VERSION_ONE_PACKAGE_SHA256 =
            "6a5cdb6bb6a5ccc878be4c965e128364fa9e5977bc2c892706ca7bf4420cb568"
    }
}