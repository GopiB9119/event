package com.example.receipt

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ReceiptAttributionPolicyTest {
    @Test
    fun `default ledger person comes from normalized local identity`() {
        assertEquals("Member Name", defaultReceiptLedgerPersonName(" Member.Name@Example.COM "))
    }

    @Test
    fun `confirmed uploader keeps uploader email separate from OCR counterparty`() {
        val identity = resolveNewReceiptLedgerIdentity(
            personName = "Gopi Banoth",
            uploaderEmail = "Gopi@Example.com",
            isUploaderThePerson = true
        )

        assertEquals("Gopi Banoth", identity?.personName)
        assertEquals("gopi@example.com", identity?.personEmail)
        assertEquals("Confirmed local identity", identity?.source)
    }

    @Test
    fun `another ledger person does not inherit uploader email`() {
        val identity = resolveNewReceiptLedgerIdentity(
            personName = "Another Member",
            uploaderEmail = "organizer@example.com",
            isUploaderThePerson = false
        )

        assertEquals("Another Member", identity?.personName)
        assertEquals("", identity?.personEmail)
        assertEquals("User-entered during review", identity?.source)
    }

    @Test
    fun `blank ledger person is rejected`() {
        assertNull(
            resolveNewReceiptLedgerIdentity(
                personName = "   ",
                uploaderEmail = "organizer@example.com",
                isUploaderThePerson = false
            )
        )
    }
}