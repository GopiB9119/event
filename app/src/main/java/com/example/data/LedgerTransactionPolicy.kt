package com.example.data

private val supportedLedgerTypes = setOf("Donated", "Credit", "Debit", "Expense")

fun isValidLedgerTransaction(eventId: Int, amount: Double, type: String): Boolean {
    return eventId > 0 &&
        amount.isFinite() &&
        amount > 0.0 &&
        type in supportedLedgerTypes
}