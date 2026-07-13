package com.example.data

import java.util.Locale

data class MemberLedgerSummary(
    val transactions: List<TransactionEntity> = emptyList(),
    val creditedCount: Int = 0,
    val debitedCount: Int = 0,
    val totalCredited: Double = 0.0,
    val totalDebited: Double = 0.0,
    val receiptUploadCount: Int = 0
)

data class EventLedgerSummary(
    val totalCollected: Double,
    val totalSpent: Double,
    val memberSummaries: Map<Int, MemberLedgerSummary>
) {
    val availableBalance: Double = totalCollected - totalSpent
}

fun calculateLedgerPresentationSummary(
    members: List<MemberEntity>,
    transactions: List<TransactionEntity>
): EventLedgerSummary {
    val builders = members.associate { it.id to MutableMemberLedgerSummary() }.toMutableMap()
    val memberIdByName = members
        .groupBy { it.normalizedName.ifBlank { it.name }.normalizedMemberKey() }
        .mapValues { entry -> entry.value.minOf { it.id } }
    val memberIdByPhone = members
        .filter { it.phone.isNotBlank() }
        .groupBy { it.phone }
        .mapValues { entry -> entry.value.minOf { it.id } }
    val memberIdByEmail = members
        .filter { it.email.isNotBlank() }
        .groupBy { it.email }
        .mapValues { entry -> entry.value.minOf { it.id } }

    var totalCollected = 0.0
    var totalSpent = 0.0

    transactions.forEach { transaction ->
        when (transaction.type) {
            "Donated", "Credit" -> totalCollected += transaction.amount
            "Debit", "Expense" -> totalSpent += transaction.amount
        }

        val matchingMemberId = if (transaction.memberId != null) {
            transaction.memberId
        } else {
            transaction.personPhone.takeIf { it.isNotBlank() }?.let(memberIdByPhone::get)
                ?: transaction.personEmail.takeIf { it.isNotBlank() }?.let(memberIdByEmail::get)
                ?: memberIdByName[transaction.personName.normalizedMemberKey()]
        }

        matchingMemberId?.let { builders[it]?.add(transaction) }
    }

    return EventLedgerSummary(
        totalCollected = totalCollected,
        totalSpent = totalSpent,
        memberSummaries = builders.mapValues { it.value.build() }
    )
}

private class MutableMemberLedgerSummary {
    private val transactions = mutableListOf<TransactionEntity>()
    private var creditedCount = 0
    private var debitedCount = 0
    private var totalCredited = 0.0
    private var totalDebited = 0.0
    private var receiptUploadCount = 0

    fun add(transaction: TransactionEntity) {
        transactions += transaction
        when (transaction.type) {
            "Donated", "Credit" -> {
                creditedCount++
                totalCredited += transaction.amount
            }
            "Debit", "Expense" -> {
                debitedCount++
                totalDebited += transaction.amount
            }
        }
        if (transaction.notes.hasPersistedReceiptEvidence()) {
            receiptUploadCount++
        }
    }

    fun build(): MemberLedgerSummary = MemberLedgerSummary(
        transactions = transactions.toList(),
        creditedCount = creditedCount,
        debitedCount = debitedCount,
        totalCredited = totalCredited,
        totalDebited = totalDebited,
        receiptUploadCount = receiptUploadCount
    )
}

private val receiptFilePathPattern = Regex("\"receiptFilePath\"\\s*:\\s*\"[^\"]+\"")

private fun String?.hasPersistedReceiptEvidence(): Boolean {
    val value = this?.trim() ?: return false
    return value.startsWith("{") && value.endsWith("}") && receiptFilePathPattern.containsMatchIn(value)
}

private fun String.normalizedMemberKey(): String = trim()
    .lowercase(Locale.ROOT)
    .replace(Regex("\\s+"), " ")