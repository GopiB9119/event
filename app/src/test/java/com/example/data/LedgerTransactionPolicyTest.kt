package com.example.data

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
}