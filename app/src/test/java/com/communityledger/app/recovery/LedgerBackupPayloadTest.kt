package com.communityledger.app.recovery

import com.communityledger.app.data.APP_DATABASE_SCHEMA_VERSION
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class LedgerBackupPayloadTest {

    @Test
    fun payloadV1SchemaMappingRequiresAnExplicitDecisionWhenRoomChanges() {
        assertEquals(LEDGER_BACKUP_SOURCE_SCHEMA_VERSION, APP_DATABASE_SCHEMA_VERSION)
    }

    @Test
    fun validPayloadPreservesLocalReferencesWithoutValidationErrors() {
        val errors = validateLedgerBackupPayload(validPayload())

        assertTrue(errors.toString(), errors.isEmpty())
    }

    @Test
    fun transactionCannotReferenceMemberOutsideItsEvent() {
        val payload = validPayload().copy(
            events = validPayload().events.mapIndexed { index, event ->
                if (index == 0) {
                    event.copy(
                        transactions = event.transactions.map { transaction ->
                            transaction.copy(sourceMemberId = 22)
                        }
                    )
                } else {
                    event
                }
            }
        )

        val errors = validateLedgerBackupPayload(payload)

        assertEquals(
            listOf("events[0].transactions[0].sourceMemberId"),
            errors.map(LedgerBackupValidationError::path)
        )
    }

    @Test
    fun receiptEvidenceMustBeLinkedExactlyOnceWithinItsEvent() {
        val event = validPayload().events.first()
        val payload = validPayload().copy(
            events = listOf(
                event.copy(
                    transactions = event.transactions + event.transactions.first().copy(sourceId = 102)
                )
            )
        )

        val errors = validateLedgerBackupPayload(payload)

        assertTrue(errors.any { it.path.endsWith("receiptEvidenceId") })
    }

    @Test
    fun receiptEvidenceCannotNameAnotherExistingTransaction() {
        val event = validPayload().events.first()
        val payload = validPayload().copy(
            events = listOf(
                event.copy(
                    transactions = event.transactions + event.transactions.first().copy(
                        sourceId = 102,
                        receiptEvidenceId = null
                    ),
                    receiptEvidence = event.receiptEvidence.map { evidence ->
                        evidence.copy(sourceTransactionId = 102)
                    }
                )
            )
        )

        val errors = validateLedgerBackupPayload(payload)

        assertEquals(
            listOf("events[0].receiptEvidence[0].sourceTransactionId"),
            errors.map(LedgerBackupValidationError::path)
        )
    }

    @Test
    fun receiptTransactionCannotCarrySourceDeviceNotes() {
        val event = validPayload().events.first()
        val payload = validPayload().copy(
            events = listOf(
                event.copy(
                    transactions = event.transactions.map { transaction ->
                        transaction.copy(manualNotes = "{\"receiptFilePath\":\"/old/private/path.json\"}")
                    }
                )
            )
        )

        val errors = validateLedgerBackupPayload(payload)

        assertEquals(
            listOf("events[0].transactions[0].manualNotes"),
            errors.map(LedgerBackupValidationError::path)
        )
    }

    @Test
    fun manualNotesCannotHideSourceDeviceReceiptPath() {
        val event = validPayload().events.first()
        val payload = validPayload().copy(
            events = listOf(
                event.copy(
                    transactions = event.transactions.map { transaction ->
                        transaction.copy(
                            manualNotes = "{\"receiptFilePath\":\"/old/private/path.json\"}",
                            receiptEvidenceId = null
                        )
                    },
                    receiptEvidence = emptyList()
                )
            )
        )

        val errors = validateLedgerBackupPayload(payload)

        assertEquals(
            listOf("events[0].transactions[0].manualNotes"),
            errors.map(LedgerBackupValidationError::path)
        )
    }

    @Test
    fun roomPrimaryKeysMustRemainUniqueAcrossEvents() {
        val payload = validPayload()
        val firstTransaction = payload.events.first().transactions.single()
        val secondEvent = payload.events.last().copy(
            transactions = listOf(
                firstTransaction.copy(
                    sourceMemberId = null,
                    receiptEvidenceId = null
                )
            )
        )

        val errors = validateLedgerBackupPayload(
            payload.copy(events = listOf(payload.events.first(), secondEvent))
        )

        assertEquals(
            listOf("events[1].transactions[0].sourceId"),
            errors.map(LedgerBackupValidationError::path)
        )
    }

    @Test
    fun aggregateOverflowIsRejectedEvenWhenIndividualAmountsAreFinite() {
        val payload = validPayload()
        val event = payload.events.first()
        val transaction = event.transactions.single().copy(
            amount = Double.MAX_VALUE,
            receiptEvidenceId = null
        )
        val overflowingEvent = event.copy(
            transactions = listOf(
                transaction,
                transaction.copy(sourceId = 102)
            ),
            receiptEvidence = emptyList()
        )

        val paths = validateLedgerBackupPayload(
            payload.copy(events = listOf(overflowingEvent))
        ).map(LedgerBackupValidationError::path)

        assertTrue("events[0].totalCollected" in paths)
        assertTrue("events[0].availableBalance" in paths)
    }

    @Test
    fun normalizedMemberNameMustMatchCanonicalName() {
        val payload = validPayload()
        val event = payload.events.first()

        val errors = validateLedgerBackupPayload(
            payload.copy(
                events = listOf(
                    event.copy(
                        members = event.members.map { member ->
                            member.copy(normalizedName = "another person")
                        }
                    )
                )
            )
        )

        assertTrue(errors.any { it.path == "events[0].members[0].normalizedName" })
    }

    @Test
    fun identicalReceiptEvidenceIdsAreAllowedAcrossEventsWhenTransactionsDiffer() {
        val payload = validPayload()
        val firstEvent = payload.events.first()
        val secondEvent = payload.events.last()
        val reusedEvidenceId = firstEvent.receiptEvidence.single().id
        val secondTransaction = firstEvent.transactions.single().copy(
            sourceId = 202,
            sourceMemberId = 22,
            receiptEvidenceId = reusedEvidenceId
        )
        val secondEvidence = firstEvent.receiptEvidence.single().copy(
            sourceTransactionId = 202
        )

        val errors = validateLedgerBackupPayload(
            payload.copy(
                events = listOf(
                    firstEvent,
                    secondEvent.copy(
                        transactions = listOf(secondTransaction),
                        receiptEvidence = listOf(secondEvidence)
                    )
                )
            )
        )

        assertTrue(errors.toString(), errors.isEmpty())
    }

    @Test
    fun invalidFinancialAndIdentityValuesAreRejected() {
        val event = validPayload().events.first()
        val payload = validPayload().copy(
            sourcePreferences = LedgerBackupSourcePreferences(
                userEmail = "Not Normalized@Example.COM",
                themeMode = "Purple"
            ),
            events = listOf(
                event.copy(
                    eventKey = "not-a-key",
                    transactions = event.transactions.map { transaction ->
                        transaction.copy(amount = Double.NaN, type = "Refund")
                    }
                )
            )
        )

        val paths = validateLedgerBackupPayload(payload).map(LedgerBackupValidationError::path)

        assertTrue("sourcePreferences.userEmail" in paths)
        assertTrue("sourcePreferences.themeMode" in paths)
        assertTrue("events[0].eventKey" in paths)
        assertTrue("events[0].transactions[0]" in paths)
    }

    @Test
    fun emptyBackupIsNotEligibleAsAValidPayload() {
        val errors = validateLedgerBackupPayload(validPayload().copy(events = emptyList()))

        assertTrue(errors.any { it.path == "events" })
    }

    @Test
    fun eventMetadataRolesAndAuditIdentityMustMatchSourceInvariants() {
        val event = validPayload().events.first()
        val payload = validPayload().copy(
            events = listOf(
                event.copy(
                    customFieldsJson = """{"creatorEmail":"Not Normalized@Example.COM","creatorEmail":"other@example.com"}""",
                    members = event.members.map { it.copy(role = "Organizer") },
                    transactions = event.transactions.map {
                        it.copy(personName = " ", uploaderEmail = "Not Normalized@Example.COM")
                    }
                )
            )
        )

        val paths = validateLedgerBackupPayload(payload).map(LedgerBackupValidationError::path)

        assertTrue("events[0].customFieldsJson" in paths)
        assertTrue("events[0].members[0].role" in paths)
        assertTrue("events[0].transactions[0].personName" in paths)
        assertTrue("events[0].transactions[0].uploaderEmail" in paths)
    }

    @Test
    fun whitespaceOnlyCustomFieldsCannotBypassRawSizeLimit() {
        val event = validPayload().events.first().copy(
            customFieldsJson = " ".repeat(64 * 1024 + 1)
        )

        val errors = validateLedgerBackupPayload(validPayload().copy(events = listOf(event)))

        assertTrue(errors.any { it.path == "events[0].customFieldsJson" })
    }

    @Test
    fun receiptReferenceAndEvidenceHashCannotBeReusedWithinEvent() {
        val event = validPayload().events.first()
        val secondEvidenceId = "33333333333333333333333333333333"
        val secondTransaction = event.transactions.single().copy(
            sourceId = 102,
            receiptEvidenceId = secondEvidenceId,
            paymentReference = event.transactions.single().paymentReference.lowercase()
        )
        val secondEvidence = event.receiptEvidence.single().copy(
            id = secondEvidenceId,
            sourceTransactionId = 102
        )

        val paths = validateLedgerBackupPayload(
            validPayload().copy(
                events = listOf(
                    event.copy(
                        transactions = event.transactions + secondTransaction,
                        receiptEvidence = event.receiptEvidence + secondEvidence
                    )
                )
            )
        ).map(LedgerBackupValidationError::path)

        assertTrue("events[0].transactions[1].paymentReference" in paths)
        assertTrue("events[0].receiptEvidence[1].sha256" in paths)
    }

    private fun validPayload(): LedgerBackupPayload {
        val evidenceId = "22222222222222222222222222222222"
        return LedgerBackupPayload(
            backupId = "11111111111111111111111111111111",
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
                    members = listOf(
                        LedgerBackupMember(
                            sourceId = 22,
                            name = "Ravi",
                            normalizedName = "ravi",
                            phone = "",
                            email = "ravi@example.com",
                            role = "Vendor",
                            createdAt = 1_788_000_001_100L
                        )
                    ),
                    transactions = emptyList(),
                    receiptEvidence = emptyList()
                )
            )
        )
    }
}