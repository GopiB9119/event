package com.communityledger.app.shared

import com.communityledger.app.receipt.AmountEvidenceSource
import com.communityledger.app.receipt.ParsedReceipt
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SharedReceiptSubmissionTest {
    @Test
    fun reliableReceiptBuildsExactMinorUnitRequest() {
        val preparation = prepareSharedReceiptSubmission(
            receipt = validReceipt(),
            eventId = "AbCdEfGhIjKlMnOpQrSt",
            expectedRevision = 7,
            ledgerPersonUid = "member-1",
            ledgerType = "Donated",
            idempotencyKey = "11111111111111111111111111111111"
        )

        assertTrue(preparation.blockingReasons.toString(), preparation.canSubmit)
        assertEquals(12_345L, preparation.request?.amountMinor)
        assertEquals("2026-07-15", preparation.request?.paymentDate)
        assertEquals("AMOUNT_LABEL", preparation.request?.amountEvidenceSource)
    }

    @Test
    fun fractionalPaiseAndUnsupportedEvidenceFailClosed() {
        val preparation = prepareSharedReceiptSubmission(
            receipt = validReceipt().copy(
                amount = 123.456,
                amountEvidenceSource = AmountEvidenceSource.UNLABELLED_NUMBER
            ),
            eventId = "AbCdEfGhIjKlMnOpQrSt",
            expectedRevision = 7,
            ledgerPersonUid = "member-1",
            ledgerType = "Donated"
        )

        assertFalse(preparation.canSubmit)
        assertNull(preparation.request)
        assertTrue(preparation.blockingReasons.any { it.contains("paise") })
        assertTrue(preparation.blockingReasons.any { it.contains("not supported") })
    }

    @Test
    fun noReferenceRequiresTwoFallbackEvidenceFields() {
        val preparation = prepareSharedReceiptSubmission(
            receipt = validReceipt().copy(
                transactionId = "",
                date = "",
                counterpartyName = ""
            ),
            eventId = "AbCdEfGhIjKlMnOpQrSt",
            expectedRevision = 7,
            ledgerPersonUid = "member-1",
            ledgerType = "Expense"
        )

        assertFalse(preparation.canSubmit)
        assertTrue(preparation.blockingReasons.any { it.contains("fallback evidence") })
    }

    @Test
    fun dateNormalizationIsStrictAndLocaleIndependent() {
        assertEquals("2026-07-15", normalizeReceiptDate("15 July 2026"))
        assertEquals("2026-07-15", normalizeReceiptDate("15/07/2026"))
        assertNull(normalizeReceiptDate("31/02/2026"))
        assertNull(normalizeReceiptDate("July sometime"))
    }

    private fun validReceipt() = ParsedReceipt(
        amount = 123.45,
        amountEvidenceConfidence = 85,
        amountEvidenceSource = AmountEvidenceSource.AMOUNT_LABEL,
        transactionId = "UPI123456789",
        counterpartyName = "Community Hall",
        date = "15 July 2026",
        paymentApp = "PhonePe",
        extractionMethod = "ML Kit Latin",
        confidence = 90,
        isReceiptLike = true
    )
}