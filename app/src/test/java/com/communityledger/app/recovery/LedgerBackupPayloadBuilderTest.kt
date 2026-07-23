package com.communityledger.app.recovery

import com.communityledger.app.data.EventEntity
import com.communityledger.app.data.LedgerSourceSnapshot
import com.communityledger.app.data.MemberEntity
import com.communityledger.app.data.TransactionEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class LedgerBackupPayloadBuilderTest {

    @Test
    fun mapsEntitiesDeterministicallyAndStripsReceiptPath() {
        val receiptEvidence = LedgerBackupReceiptEvidence(
            id = EVIDENCE_ID,
            sourceTransactionId = 101,
            sha256 = "a".repeat(64),
            byteCount = 128
        )

        val result = buildLedgerBackupPayload(
            backupId = BACKUP_ID,
            createdAtEpochMillis = 1_789_000_000_000L,
            sourcePreferences = sourcePreferences(),
            events = listOf(event(id = 2, eventKey = SECOND_EVENT_KEY), event(id = 1, eventKey = FIRST_EVENT_KEY)),
            members = listOf(member(id = 11, eventId = 1)),
            transactions = listOf(receiptTransaction()),
            receiptEvidence = listOf(receiptEvidence)
        )

        assertTrue(result.errors.toString(), result.errors.isEmpty())
        val payload = requireNotNull(result.payload)
        assertEquals(listOf(1, 2), payload.events.map(LedgerBackupEvent::sourceId))
        val transaction = payload.events.first().transactions.single()
        assertNull(transaction.manualNotes)
        assertEquals(EVIDENCE_ID, transaction.receiptEvidenceId)
        assertEquals(receiptEvidence, payload.events.first().receiptEvidence.single())
    }

    @Test
    fun mapsConsistentSourceSnapshotThroughTheSameValidator() {
        val result = buildLedgerBackupPayload(
            backupId = BACKUP_ID,
            createdAtEpochMillis = 1_789_000_000_000L,
            sourcePreferences = sourcePreferences(),
            sourceSnapshot = LedgerSourceSnapshot(
                events = listOf(event(id = 1, eventKey = FIRST_EVENT_KEY)),
                members = listOf(member(id = 11, eventId = 1)),
                transactions = listOf(receiptTransaction().copy(notes = "Manual note", transactionId = ""))
            ),
            receiptEvidence = emptyList()
        )

        assertTrue(result.errors.toString(), result.errors.isEmpty())
        assertEquals("Manual note", result.payload?.events?.single()?.transactions?.single()?.manualNotes)
    }

    @Test
    fun preservesNotesForTransactionWithoutReceiptEvidence() {
        val transaction = receiptTransaction().copy(
            notes = "Paid after committee approval",
            transactionId = ""
        )

        val result = buildLedgerBackupPayload(
            backupId = BACKUP_ID,
            createdAtEpochMillis = 1_789_000_000_000L,
            sourcePreferences = sourcePreferences(),
            events = listOf(event(id = 1, eventKey = FIRST_EVENT_KEY)),
            members = listOf(member(id = 11, eventId = 1)),
            transactions = listOf(transaction),
            receiptEvidence = emptyList()
        )

        assertTrue(result.errors.toString(), result.errors.isEmpty())
        assertEquals(
            "Paid after committee approval",
            result.payload?.events?.single()?.transactions?.single()?.manualNotes
        )
    }

    @Test
    fun freeTextReceiptMarkerIsPreservedAsManualNotes() {
        val notes = "Support mentioned \"receiptFilePath\":\"not receipt JSON\" in a troubleshooting note."
        val result = buildLedgerBackupPayload(
            backupId = BACKUP_ID,
            createdAtEpochMillis = 1_789_000_000_000L,
            sourcePreferences = sourcePreferences(),
            events = listOf(event(id = 1, eventKey = FIRST_EVENT_KEY)),
            members = listOf(member(id = 11, eventId = 1)),
            transactions = listOf(receiptTransaction().copy(notes = notes, transactionId = "")),
            receiptEvidence = emptyList()
        )

        assertTrue(result.errors.toString(), result.errors.isEmpty())
        assertEquals(
            notes,
            result.payload?.events?.single()?.transactions?.single()?.manualNotes
        )
    }

    @Test
    fun receiptPathWithoutEvidenceFailsInsteadOfBecomingManualNotes() {
        val result = buildLedgerBackupPayload(
            backupId = BACKUP_ID,
            createdAtEpochMillis = 1_789_000_000_000L,
            sourcePreferences = sourcePreferences(),
            events = listOf(event(id = 1, eventKey = FIRST_EVENT_KEY)),
            members = listOf(member(id = 11, eventId = 1)),
            transactions = listOf(receiptTransaction()),
            receiptEvidence = emptyList()
        )

        assertNull(result.payload)
        assertTrue(result.errors.any { it.path == "transactions[sourceId=101].notes" })
    }

    @Test
    fun ambiguousReceiptNotesFailInsteadOfEnteringManifest() {
        val notes = """{"receiptFilePath":"/first","copy":{"receiptFilePath":"/second"}}"""
        val result = buildLedgerBackupPayload(
            backupId = BACKUP_ID,
            createdAtEpochMillis = 1_789_000_000_000L,
            sourcePreferences = sourcePreferences(),
            events = listOf(event(id = 1, eventKey = FIRST_EVENT_KEY)),
            members = listOf(member(id = 11, eventId = 1)),
            transactions = listOf(receiptTransaction().copy(notes = notes)),
            receiptEvidence = emptyList()
        )

        assertNull(result.payload)
        assertEquals(
            listOf("transactions[sourceId=101].notes"),
            result.errors.map(LedgerBackupValidationError::path)
        )
    }

    @Test
    fun orphanRowsFailInsteadOfBeingDropped() {
        val result = buildLedgerBackupPayload(
            backupId = BACKUP_ID,
            createdAtEpochMillis = 1_789_000_000_000L,
            sourcePreferences = sourcePreferences(),
            events = listOf(event(id = 1, eventKey = FIRST_EVENT_KEY)),
            members = listOf(member(id = 22, eventId = 2)),
            transactions = listOf(receiptTransaction().copy(id = 202, eventId = 2, memberId = 22, notes = null)),
            receiptEvidence = emptyList()
        )

        assertNull(result.payload)
        assertTrue(result.errors.any { it.path == "members[sourceId=22]" })
        assertTrue(result.errors.any { it.path == "transactions[sourceId=202]" })
    }

    @Test
    fun crossEventMemberLinkFailsInsteadOfEnteringManifest() {
        val result = buildLedgerBackupPayload(
            backupId = BACKUP_ID,
            createdAtEpochMillis = 1_789_000_000_000L,
            sourcePreferences = sourcePreferences(),
            events = listOf(
                event(id = 1, eventKey = FIRST_EVENT_KEY),
                event(id = 2, eventKey = SECOND_EVENT_KEY)
            ),
            members = listOf(member(id = 22, eventId = 2)),
            transactions = listOf(receiptTransaction().copy(memberId = 22, notes = null)),
            receiptEvidence = emptyList()
        )

        assertNull(result.payload)
        assertTrue(result.errors.any { it.path == "events[0].transactions[0].sourceMemberId" })
    }

    private fun sourcePreferences() = LedgerBackupSourcePreferences(
        userEmail = "organizer@example.com",
        themeMode = "System"
    )

    private fun event(id: Int, eventKey: String) = EventEntity(
        id = id,
        eventKey = eventKey,
        title = "Event $id",
        createdDate = 1_788_000_000_000L + id,
        customFieldsJson = "{}"
    )

    private fun member(id: Int, eventId: Int) = MemberEntity(
        id = id,
        eventId = eventId,
        name = "Asha",
        normalizedName = "asha",
        email = "asha@example.com",
        createdAt = 1_788_000_000_100L
    )

    private fun receiptTransaction() = TransactionEntity(
        id = 101,
        eventId = 1,
        memberId = 11,
        personName = "Asha",
        personPhone = "",
        personEmail = "asha@example.com",
        amount = 500.0,
        type = "Donated",
        date = 1_788_000_000_200L,
        transactionId = "UPI-101",
        notes = "{\"receiptFilePath\":\"/data/user/0/app/files/receipts/source.json\"}",
        uploaderEmail = "organizer@example.com"
    )

    private companion object {
        const val BACKUP_ID = "11111111111111111111111111111111"
        const val EVIDENCE_ID = "22222222222222222222222222222222"
        const val FIRST_EVENT_KEY = "0123456789abcdef0123456789abcdef"
        const val SECOND_EVENT_KEY = "fedcba9876543210fedcba9876543210"
    }
}