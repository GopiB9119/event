package com.communityledger.app.data

private val supportedLedgerTypes = setOf("Donated", "Credit", "Debit", "Expense")

data class LedgerAggregateProjection(
    val totalCollected: Double,
    val totalSpent: Double,
    val invalidRowCount: Long
)

fun isValidLedgerTransaction(eventId: Int, amount: Double, type: String): Boolean {
    return eventId > 0 &&
        amount.isFinite() &&
        amount > 0.0 &&
        type in supportedLedgerTypes
}

fun wouldLedgerTotalsRemainFinite(
    existingTransactions: List<TransactionEntity>,
    candidate: TransactionEntity
): Boolean {
    if (!isValidLedgerTransaction(candidate.eventId, candidate.amount, candidate.type)) return false

    var totalCollected = 0.0
    var totalSpent = 0.0
    val transactions = existingTransactions
        .asSequence()
        .filterNot { candidate.id > 0 && it.id == candidate.id }
        .plus(candidate)

    transactions.forEach { transaction ->
        if (
            transaction.eventId != candidate.eventId ||
            !isValidLedgerTransaction(transaction.eventId, transaction.amount, transaction.type)
        ) {
            return false
        }
        when (transaction.type) {
            "Donated", "Credit" -> {
                totalCollected += transaction.amount
                if (!totalCollected.isFinite()) return false
            }
            "Debit", "Expense" -> {
                totalSpent += transaction.amount
                if (!totalSpent.isFinite()) return false
            }
        }
    }

    return (totalCollected - totalSpent).isFinite()
}

fun wouldProjectedLedgerTotalsRemainFinite(
    projection: LedgerAggregateProjection,
    candidate: TransactionEntity
): Boolean {
    if (!isValidLedgerTransaction(candidate.eventId, candidate.amount, candidate.type)) return false
    if (
        projection.invalidRowCount != 0L ||
        !projection.totalCollected.isFinite() ||
        !projection.totalSpent.isFinite()
    ) {
        return false
    }

    val totalCollected = projection.totalCollected + if (
        candidate.type == "Donated" || candidate.type == "Credit"
    ) {
        candidate.amount
    } else {
        0.0
    }
    val totalSpent = projection.totalSpent + if (
        candidate.type == "Debit" || candidate.type == "Expense"
    ) {
        candidate.amount
    } else {
        0.0
    }
    return totalCollected.isFinite() &&
        totalSpent.isFinite() &&
        (totalCollected - totalSpent).isFinite()
}