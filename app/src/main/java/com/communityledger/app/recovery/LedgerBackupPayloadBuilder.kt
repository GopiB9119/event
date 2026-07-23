package com.communityledger.app.recovery

import com.communityledger.app.data.EventEntity
import com.communityledger.app.data.LedgerSourceSnapshot
import com.communityledger.app.data.MAX_LEDGER_SOURCE_EVENTS
import com.communityledger.app.data.MAX_LEDGER_SOURCE_MEMBERS
import com.communityledger.app.data.MAX_LEDGER_SOURCE_TRANSACTIONS
import com.communityledger.app.data.MemberEntity
import com.communityledger.app.data.ReceiptEvidenceNotes
import com.communityledger.app.data.TransactionEntity
import com.communityledger.app.data.classifyReceiptEvidenceNotes

data class LedgerBackupPayloadBuildResult(
    val payload: LedgerBackupPayload?,
    val errors: List<LedgerBackupValidationError>
)

fun buildLedgerBackupPayload(
    backupId: String,
    createdAtEpochMillis: Long,
    sourcePreferences: LedgerBackupSourcePreferences,
    sourceSnapshot: LedgerSourceSnapshot,
    receiptEvidence: List<LedgerBackupReceiptEvidence>
): LedgerBackupPayloadBuildResult {
    return buildLedgerBackupPayload(
        backupId = backupId,
        createdAtEpochMillis = createdAtEpochMillis,
        sourcePreferences = sourcePreferences,
        events = sourceSnapshot.events,
        members = sourceSnapshot.members,
        transactions = sourceSnapshot.transactions,
        receiptEvidence = receiptEvidence
    )
}

fun buildLedgerBackupPayload(
    backupId: String,
    createdAtEpochMillis: Long,
    sourcePreferences: LedgerBackupSourcePreferences,
    events: List<EventEntity>,
    members: List<MemberEntity>,
    transactions: List<TransactionEntity>,
    receiptEvidence: List<LedgerBackupReceiptEvidence>
): LedgerBackupPayloadBuildResult {
    val errors = mutableListOf<LedgerBackupValidationError>()
    if (events.size > MAX_LEDGER_SOURCE_EVENTS) {
        errors += LedgerBackupValidationError("events", "Backup source contains too many events.")
    }
    if (members.size > MAX_LEDGER_SOURCE_MEMBERS) {
        errors += LedgerBackupValidationError("members", "Backup source contains too many members.")
    }
    if (transactions.size > MAX_LEDGER_SOURCE_TRANSACTIONS) {
        errors += LedgerBackupValidationError("transactions", "Backup source contains too many transactions.")
    }
    if (receiptEvidence.size > LedgerBackupManifestCodec.MAX_RECEIPT_EVIDENCE) {
        errors += LedgerBackupValidationError(
            "receiptEvidence",
            "Backup source contains too many receipt evidence descriptors."
        )
    }
    if (errors.isNotEmpty()) return LedgerBackupPayloadBuildResult(payload = null, errors = errors)

    val eventIds = events.map(EventEntity::id).toSet()
    val transactionIds = transactions.map(TransactionEntity::id).toSet()
    val evidenceByTransactionId = mutableMapOf<Int, LedgerBackupReceiptEvidence>()
    val membersByEventId = members.groupBy(MemberEntity::eventId)
    val transactionsByEventId = transactions.groupBy(TransactionEntity::eventId)

    members.filter { it.eventId !in eventIds }.forEach { member ->
        errors += LedgerBackupValidationError(
            path = "members[sourceId=${member.id}]",
            message = "Member references an event that is not in the backup source."
        )
    }
    transactions.filter { it.eventId !in eventIds }.forEach { transaction ->
        errors += LedgerBackupValidationError(
            path = "transactions[sourceId=${transaction.id}]",
            message = "Transaction references an event that is not in the backup source."
        )
    }
    receiptEvidence.forEach { evidence ->
        if (evidence.sourceTransactionId !in transactionIds) {
            errors += LedgerBackupValidationError(
                path = "receiptEvidence[id=${evidence.id}]",
                message = "Receipt evidence references a transaction that is not in the backup source."
            )
        } else if (evidenceByTransactionId.put(evidence.sourceTransactionId, evidence) != null) {
            errors += LedgerBackupValidationError(
                path = "receiptEvidence[sourceTransactionId=${evidence.sourceTransactionId}]",
                message = "A transaction cannot own more than one receipt evidence record."
            )
        }
    }

    val mappedEvents = events.sortedBy(EventEntity::id).map { event ->
        val eventMembers = membersByEventId[event.id].orEmpty().sortedBy(MemberEntity::id)
        val eventTransactions = transactionsByEventId[event.id].orEmpty().sortedBy(TransactionEntity::id)
        val notesByTransactionId = eventTransactions.associate { transaction ->
            transaction.id to transaction.notes.classifyReceiptEvidenceNotes()
        }
        val mappedTransactions = eventTransactions.map { transaction ->
            val evidence = evidenceByTransactionId[transaction.id]
            val notes = notesByTransactionId.getValue(transaction.id)
            when (notes) {
                is ReceiptEvidenceNotes.Persisted -> if (evidence == null) {
                    errors += LedgerBackupValidationError(
                        path = "transactions[sourceId=${transaction.id}].notes",
                        message = "Receipt transaction is missing its portable evidence descriptor."
                    )
                }
                is ReceiptEvidenceNotes.Manual -> if (evidence != null) {
                    errors += LedgerBackupValidationError(
                        path = "receiptEvidence[id=${evidence.id}]",
                        message = "Receipt evidence is not owned by a transaction with persisted receipt notes."
                    )
                }
                is ReceiptEvidenceNotes.Invalid -> {
                    errors += LedgerBackupValidationError(
                        path = "transactions[sourceId=${transaction.id}].notes",
                        message = notes.reason
                    )
                }
            }

            LedgerBackupTransaction(
                sourceId = transaction.id,
                sourceMemberId = transaction.memberId,
                personName = transaction.personName,
                personPhone = transaction.personPhone,
                personEmail = transaction.personEmail,
                amount = transaction.amount,
                type = transaction.type,
                date = transaction.date,
                paymentReference = transaction.transactionId,
                isVerified = transaction.isVerified,
                manualNotes = transaction.notes.takeIf { notes is ReceiptEvidenceNotes.Manual },
                uploaderEmail = transaction.uploaderEmail,
                receiptEvidenceId = evidence?.id?.takeIf { notes is ReceiptEvidenceNotes.Persisted }
            )
        }

        LedgerBackupEvent(
            sourceId = event.id,
            eventKey = event.eventKey.orEmpty(),
            title = event.title,
            duration = event.duration,
            createdDate = event.createdDate,
            isPrivate = event.isPrivate,
            customFieldsJson = event.customFieldsJson,
            members = eventMembers.map(MemberEntity::toBackupMember),
            transactions = mappedTransactions,
            receiptEvidence = eventTransactions.mapNotNull { transaction ->
                evidenceByTransactionId[transaction.id]
                    ?.takeIf {
                        notesByTransactionId[transaction.id] is ReceiptEvidenceNotes.Persisted
                    }
            }
        )
    }

    val payload = LedgerBackupPayload(
        backupId = backupId,
        createdAtEpochMillis = createdAtEpochMillis,
        sourcePreferences = sourcePreferences,
        events = mappedEvents
    )
    errors += validateLedgerBackupPayload(payload)

    return LedgerBackupPayloadBuildResult(
        payload = payload.takeIf { errors.isEmpty() },
        errors = errors
    )
}

private fun MemberEntity.toBackupMember(): LedgerBackupMember {
    return LedgerBackupMember(
        sourceId = id,
        name = name,
        normalizedName = normalizedName,
        phone = phone,
        email = email,
        role = role,
        createdAt = createdAt
    )
}