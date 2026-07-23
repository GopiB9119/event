package com.communityledger.app.data

data class MemberLedgerSummary(
    val creditedCount: Int = 0,
    val debitedCount: Int = 0,
    val totalCredited: Double = 0.0,
    val totalDebited: Double = 0.0,
    val receiptUploadCount: Int = 0,
    val hasValidTotals: Boolean = true
)

data class EventLedgerSummary(
    val totalCollected: Double,
    val totalSpent: Double,
    val memberSummaries: Map<Int, MemberLedgerSummary>,
    val hasValidTotals: Boolean
) {
    val availableBalance: Double = if (hasValidTotals) totalCollected - totalSpent else Double.NaN
}

fun calculateLedgerPresentationSummary(
    members: List<MemberEntity>,
    transactions: List<TransactionEntity>
): EventLedgerSummary {
    val builders = members.associate { it.id to MutableMemberLedgerSummary() }.toMutableMap()
    val memberResolver = LedgerMemberResolver(members)

    var totalCollected = 0.0
    var totalSpent = 0.0
    var hasValidTotals = true

    transactions.forEach { transaction ->
        if (!isValidLedgerTransaction(transaction.eventId, transaction.amount, transaction.type)) {
            hasValidTotals = false
        }
        when (transaction.type) {
            "Donated", "Credit" -> {
                totalCollected += transaction.amount
                if (!totalCollected.isFinite()) hasValidTotals = false
            }
            "Debit", "Expense" -> {
                totalSpent += transaction.amount
                if (!totalSpent.isFinite()) hasValidTotals = false
            }
        }

        memberResolver.resolve(transaction)?.let { builders[it]?.add(transaction) }
    }

    hasValidTotals = hasValidTotals && (totalCollected - totalSpent).isFinite()
    return EventLedgerSummary(
        totalCollected = totalCollected.takeIf { hasValidTotals } ?: Double.NaN,
        totalSpent = totalSpent.takeIf { hasValidTotals } ?: Double.NaN,
        memberSummaries = builders.mapValues { it.value.build() },
        hasValidTotals = hasValidTotals
    )
}

fun transactionsForLedgerMember(
    members: List<MemberEntity>,
    transactions: List<TransactionEntity>,
    memberId: Int
): List<TransactionEntity> {
    val memberResolver = LedgerMemberResolver(members)
    return transactions.filter { memberResolver.resolve(it) == memberId }
}

private class LedgerMemberResolver(members: List<MemberEntity>) {
    private val memberIdByName = members
        .groupBy { normalizeMemberName(it.normalizedName.ifBlank { it.name }) }
        .mapValues { entry -> entry.value.minOf { it.id } }
    private val memberIdByPhone = members
        .filter { it.phone.isNotBlank() }
        .groupBy { it.phone }
        .mapValues { entry -> entry.value.minOf { it.id } }
    private val memberIdByEmail = members
        .filter { it.email.isNotBlank() }
        .groupBy { it.email }
        .mapValues { entry -> entry.value.minOf { it.id } }

    fun resolve(transaction: TransactionEntity): Int? = transaction.memberId
        ?: transaction.personPhone.takeIf { it.isNotBlank() }?.let(memberIdByPhone::get)
        ?: transaction.personEmail.takeIf { it.isNotBlank() }?.let(memberIdByEmail::get)
        ?: memberIdByName[normalizeMemberName(transaction.personName)]
}

private class MutableMemberLedgerSummary {
    private var creditedCount = 0
    private var debitedCount = 0
    private var totalCredited = 0.0
    private var totalDebited = 0.0
    private var receiptUploadCount = 0
    private var hasValidTotals = true

    fun add(transaction: TransactionEntity) {
        if (!isValidLedgerTransaction(transaction.eventId, transaction.amount, transaction.type)) {
            hasValidTotals = false
        }
        when (transaction.type) {
            "Donated", "Credit" -> {
                creditedCount++
                totalCredited += transaction.amount
                if (!totalCredited.isFinite()) hasValidTotals = false
            }
            "Debit", "Expense" -> {
                debitedCount++
                totalDebited += transaction.amount
                if (!totalDebited.isFinite()) hasValidTotals = false
            }
        }
        if (transaction.notes.hasPersistedReceiptEvidence()) {
            receiptUploadCount++
        }
    }

    fun build(): MemberLedgerSummary = MemberLedgerSummary(
        creditedCount = creditedCount,
        debitedCount = debitedCount,
        totalCredited = totalCredited.takeIf { hasValidTotals } ?: Double.NaN,
        totalDebited = totalDebited.takeIf { hasValidTotals } ?: Double.NaN,
        receiptUploadCount = receiptUploadCount,
        hasValidTotals = hasValidTotals
    )
}
