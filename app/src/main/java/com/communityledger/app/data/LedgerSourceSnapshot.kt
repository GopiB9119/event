package com.communityledger.app.data

const val MAX_LEDGER_SOURCE_EVENTS = 10_000
const val MAX_LEDGER_SOURCE_MEMBERS = 100_000
const val MAX_LEDGER_SOURCE_TRANSACTIONS = 250_000

data class LedgerSourceSnapshot(
    val events: List<EventEntity>,
    val members: List<MemberEntity>,
    val transactions: List<TransactionEntity>
)

data class ReceiptEvidenceReference(
    val eventId: Int,
    val notes: String?
)

class LedgerSourceSnapshotLimitException(message: String) : IllegalStateException(message)