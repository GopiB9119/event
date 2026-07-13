package com.example.data

import org.junit.Assert.assertEquals
import org.junit.Test

class LedgerPresentationSummaryTest {

    @Test
    fun calculatesEventAndMemberTotalsInOneSummary() {
        val member = member(id = 1, name = "Asha")
        val transactions = listOf(
            transaction(id = 1, memberId = 1, type = "Donated", amount = 500.0, notes = "{}"),
            transaction(id = 2, memberId = 1, type = "Expense", amount = 120.0),
            transaction(id = 3, memberId = null, personName = "Unmatched", type = "Credit", amount = 80.0)
        )

        val summary = calculateLedgerPresentationSummary(listOf(member), transactions)
        val memberSummary = summary.memberSummaries.getValue(1)

        assertEquals(580.0, summary.totalCollected, 0.0)
        assertEquals(120.0, summary.totalSpent, 0.0)
        assertEquals(460.0, summary.availableBalance, 0.0)
        assertEquals(1, memberSummary.creditedCount)
        assertEquals(1, memberSummary.debitedCount)
        assertEquals(500.0, memberSummary.totalCredited, 0.0)
        assertEquals(120.0, memberSummary.totalDebited, 0.0)
        assertEquals(1, memberSummary.receiptUploadCount)
        assertEquals(listOf(1, 2), memberSummary.transactions.map { it.id })
    }

    @Test
    fun linkedTransactionDoesNotFallBackToAnotherMatchingMember() {
        val members = listOf(
            member(id = 1, name = "Asha", phone = "111"),
            member(id = 2, name = "Asha", phone = "222")
        )
        val transaction = transaction(id = 1, memberId = 1, personName = "Asha", personPhone = "222")

        val summary = calculateLedgerPresentationSummary(members, listOf(transaction))

        assertEquals(1, summary.memberSummaries.getValue(1).transactions.size)
        assertEquals(0, summary.memberSummaries.getValue(2).transactions.size)
    }

    @Test
    fun unlinkedTransactionUsesNamePhoneOrEmailWithoutDoubleCountingOneMember() {
        val members = listOf(
            member(id = 1, name = "Asha", phone = "111", email = "asha@example.com"),
            member(id = 2, name = "Other", phone = "222", email = "other@example.com")
        )
        val transaction = transaction(
            id = 1,
            memberId = null,
            personName = "ASHA",
            personPhone = "111",
            personEmail = "asha@example.com"
        )

        val summary = calculateLedgerPresentationSummary(members, listOf(transaction))

        assertEquals(1, summary.memberSummaries.getValue(1).transactions.size)
        assertEquals(1, summary.memberSummaries.getValue(1).creditedCount)
        assertEquals(0, summary.memberSummaries.getValue(2).transactions.size)
    }

    @Test
    fun aggregatesTenThousandLinkedTransactionsWithoutLosingCounts() {
        val members = (1..100).map { member(id = it, name = "Member $it") }
        val transactions = (1..10_000).map { index ->
            transaction(
                id = index,
                memberId = (index % members.size) + 1,
                type = if (index % 2 == 0) "Credit" else "Expense",
                amount = 1.0
            )
        }

        val summary = calculateLedgerPresentationSummary(members, transactions)

        assertEquals(5_000.0, summary.totalCollected, 0.0)
        assertEquals(5_000.0, summary.totalSpent, 0.0)
        assertEquals(10_000, summary.memberSummaries.values.sumOf { it.transactions.size })
        assertEquals(5_000, summary.memberSummaries.values.sumOf { it.creditedCount })
        assertEquals(5_000, summary.memberSummaries.values.sumOf { it.debitedCount })
    }

    private fun member(
        id: Int,
        name: String,
        phone: String = "",
        email: String = ""
    ) = MemberEntity(
        id = id,
        eventId = 1,
        name = name,
        normalizedName = name.lowercase(),
        phone = phone,
        email = email
    )

    private fun transaction(
        id: Int,
        memberId: Int?,
        type: String = "Donated",
        amount: Double = 10.0,
        notes: String? = null,
        personName: String = "Asha",
        personPhone: String = "",
        personEmail: String = ""
    ) = TransactionEntity(
        id = id,
        eventId = 1,
        memberId = memberId,
        personName = personName,
        personPhone = personPhone,
        personEmail = personEmail,
        amount = amount,
        type = type,
        notes = notes
    )
}