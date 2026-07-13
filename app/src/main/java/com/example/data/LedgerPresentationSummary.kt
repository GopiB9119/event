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
    val memberIdsByName = members.groupBy { it.name.caseInsensitiveKey() }.mapValues { entry -> entry.value.map { it.id } }
    val memberIdsByPhone = members.filter { it.phone.isNotBlank() }.groupBy { it.phone }.mapValues { entry -> entry.value.map { it.id } }
    val memberIdsByEmail = members.filter { it.email.isNotBlank() }.groupBy { it.email }.mapValues { entry -> entry.value.map { it.id } }

    var totalCollected = 0.0
    var totalSpent = 0.0

    transactions.forEach { transaction ->
        when (transaction.type) {
            "Donated", "Credit" -> totalCollected += transaction.amount
            "Debit", "Expense" -> totalSpent += transaction.amount
        }

        val matchingMemberIds = if (transaction.memberId != null) {
            listOf(transaction.memberId)
        } else {
            buildSet {
                addAll(memberIdsByName[transaction.personName.caseInsensitiveKey()].orEmpty())
                if (transaction.personPhone.isNotBlank()) {
                    addAll(memberIdsByPhone[transaction.personPhone].orEmpty())
                }
                if (transaction.personEmail.isNotBlank()) {
                    addAll(memberIdsByEmail[transaction.personEmail].orEmpty())
                }
            }
        }

        matchingMemberIds.forEach { memberId ->
            builders[memberId]?.add(transaction)
        }
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
        if (transaction.notes?.trim()?.startsWith("{") == true) {
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

private fun String.caseInsensitiveKey(): String = lowercase(Locale.ROOT)