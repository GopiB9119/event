package com.communityledger.app.data

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LedgerTransactionPolicyTest {
    @Test
    fun `accepts positive finite amounts for supported ledger types`() {
        listOf("Donated", "Credit", "Debit", "Expense").forEach { type ->
            assertTrue(isValidLedgerTransaction(eventId = 1, amount = 1.0, type = type))
        }
    }

    @Test
    fun `rejects non positive and non finite amounts`() {
        assertFalse(isValidLedgerTransaction(eventId = 1, amount = 0.0, type = "Donated"))
        assertFalse(isValidLedgerTransaction(eventId = 1, amount = -1.0, type = "Donated"))
        assertFalse(isValidLedgerTransaction(eventId = 1, amount = Double.NaN, type = "Donated"))
        assertFalse(isValidLedgerTransaction(eventId = 1, amount = Double.POSITIVE_INFINITY, type = "Donated"))
    }

    @Test
    fun `rejects invalid event and unsupported transaction type`() {
        assertFalse(isValidLedgerTransaction(eventId = 0, amount = 100.0, type = "Donated"))
        assertFalse(isValidLedgerTransaction(eventId = 1, amount = 100.0, type = "Refund"))
    }

    @Test
    fun `rejects candidate when prospective event totals overflow`() {
        val existing = listOf(transaction(id = 1, amount = Double.MAX_VALUE, type = "Donated"))
        val candidate = transaction(id = 0, amount = Double.MAX_VALUE, type = "Credit")

        assertFalse(wouldLedgerTotalsRemainFinite(existing, candidate))
    }

    @Test
    fun `replacement excludes the prior row before checking totals`() {
        val existing = listOf(
            transaction(id = 1, amount = Double.MAX_VALUE, type = "Donated"),
            transaction(id = 2, amount = 5.0, type = "Expense")
        )
        val replacement = transaction(id = 1, amount = 10.0, type = "Credit")

        assertTrue(wouldLedgerTotalsRemainFinite(existing, replacement))
    }

    @Test
    fun `projected aggregate guard rejects overflow and invalid source rows`() {
        val candidate = transaction(id = 0, amount = Double.MAX_VALUE, type = "Credit")
        assertFalse(
            wouldProjectedLedgerTotalsRemainFinite(
                LedgerAggregateProjection(Double.MAX_VALUE, 0.0, 0),
                candidate
            )
        )
        assertFalse(
            wouldProjectedLedgerTotalsRemainFinite(
                LedgerAggregateProjection(0.0, 0.0, 1),
                transaction(id = 0, amount = 10.0, type = "Credit")
            )
        )
    }

    @Test
    fun `projected aggregate guard accepts finite money in and out totals`() {
        assertTrue(
            wouldProjectedLedgerTotalsRemainFinite(
                LedgerAggregateProjection(500.0, 125.0, 0),
                transaction(id = 0, amount = 25.0, type = "Expense")
            )
        )
    }

    private fun transaction(id: Int, amount: Double, type: String) = TransactionEntity(
        id = id,
        eventId = 1,
        personName = "Test Person",
        personPhone = "",
        personEmail = "",
        amount = amount,
        type = type
    )
}