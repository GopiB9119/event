package com.example.receipt

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ReceiptSavePolicyTest {
    @Test
    fun `reliable labelled amount can save without optional app or reference`() {
        val receipt = ReceiptParser.parse(
            """
            Amount INR 54,000
            Received from SAMPLE MEMBER
            11 April 2026
            """.trimIndent()
        )

        assertTrue(receipt.confidence < 60)
        assertTrue(receipt.amountEvidenceConfidence >= MIN_RELIABLE_AMOUNT_EVIDENCE)
        assertTrue(evaluateReceiptSaveEligibility(receipt, hasDuplicate = false, hasLedgerIdentity = true).canSave)
    }

    @Test
    fun `unlabelled number stays blocked even when receipt details are complete`() {
        val receipt = ReceiptParser.parse(
            """
            Google Pay
            UPI Ref No: 310725987654
            Paid to SAMPLE MERCHANT
            sample.merchant@okaxis
            11 April 2026
            Completed
            54000
            """.trimIndent()
        )

        assertTrue(receipt.confidence >= 60)
        assertTrue(receipt.amountEvidenceConfidence < MIN_RELIABLE_AMOUNT_EVIDENCE)
        assertFalse(evaluateReceiptSaveEligibility(receipt, hasDuplicate = false, hasLedgerIdentity = true).canSave)
    }

    @Test
    fun `duplicate and missing ledger identity remain blocking`() {
        val receipt = ReceiptParser.parse("PhonePe\nPaid ₹500.00")
        val eligibility = evaluateReceiptSaveEligibility(receipt, hasDuplicate = true, hasLedgerIdentity = false)

        assertFalse(eligibility.canSave)
        assertTrue(eligibility.blockingReasons.any { "already" in it })
        assertTrue(eligibility.blockingReasons.any { "person or vendor" in it })
    }

    @Test
    fun `edited amount requires confirmation before save`() {
        val detected = ReceiptParser.parse("Google Pay\nPaid ₹500.00")
        val entered = detected.copy(
            amount = 750.0,
            amountEvidenceConfidence = 0,
            amountEvidenceSource = AmountEvidenceSource.USER_ENTERED
        )
        val confirmed = entered.copy(
            amountEvidenceConfidence = 100,
            amountEvidenceSource = AmountEvidenceSource.USER_CONFIRMED
        )

        assertFalse(evaluateReceiptSaveEligibility(entered, false, true).canSave)
        assertTrue(evaluateReceiptSaveEligibility(confirmed, false, true).canSave)
    }
}