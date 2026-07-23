package com.communityledger.app.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ReceiptEvidencePolicyTest {

    @Test
    fun sourceWriterShapeIsPersistedEvidence() {
        val result = """{"amount":500,"receiptFilePath":"/private/receipt.json"}"""
            .classifyReceiptEvidenceNotes()

        assertEquals(
            ReceiptEvidenceNotes.Persisted("/private/receipt.json"),
            result
        )
    }

    @Test
    fun nestedOnlyPathIsInvalid() {
        val result = """{"metadata":{"receiptFilePath":"not evidence"}}"""
            .classifyReceiptEvidenceNotes()

        assertTrue(result is ReceiptEvidenceNotes.Invalid)
    }

    @Test
    fun duplicateTopLevelPathsAreInvalid() {
        val result = """{"receiptFilePath":"/first","receiptFilePath":"/second"}"""
            .classifyReceiptEvidenceNotes()

        assertTrue(result is ReceiptEvidenceNotes.Invalid)
    }

    @Test
    fun topLevelAndNestedPathsAreInvalid() {
        val result = """{"receiptFilePath":"/first","copy":{"receiptFilePath":"/second"}}"""
            .classifyReceiptEvidenceNotes()

        assertTrue(result is ReceiptEvidenceNotes.Invalid)
    }

    @Test
    fun blankAndNonStringTopLevelPathsAreInvalid() {
        val blank = """{"receiptFilePath":"  "}""".classifyReceiptEvidenceNotes()
        val number = """{"receiptFilePath":42}""".classifyReceiptEvidenceNotes()

        assertTrue(blank is ReceiptEvidenceNotes.Invalid)
        assertTrue(number is ReceiptEvidenceNotes.Invalid)
    }

    @Test
    fun malformedJsonWithReceiptMarkerIsInvalid() {
        val result = """{"receiptFilePath":"/private/receipt.json""".classifyReceiptEvidenceNotes()

        assertTrue(result is ReceiptEvidenceNotes.Invalid)
    }

    @Test
    fun escapedNestedAndArrayPathMarkersAreInvalid() {
        val escaped = """{"metadata":{"receiptFile\u0050ath":"/private/receipt.json"}}"""
        val array = """[{"receiptFilePath":"/private/receipt.json"}]"""

        assertTrue(escaped.classifyReceiptEvidenceNotes() is ReceiptEvidenceNotes.Invalid)
        assertTrue(array.classifyReceiptEvidenceNotes() is ReceiptEvidenceNotes.Invalid)
    }

    @Test
    fun malformedJsonWithoutLiteralMarkerIsInvalid() {
        val result = """{"note":"unfinished""".classifyReceiptEvidenceNotes()

        assertTrue(result is ReceiptEvidenceNotes.Invalid)
    }

    @Test
    fun oversizedAndDeepJsonAreInvalidWithoutUnboundedTraversal() {
        val oversized = " ".repeat(MAX_RECEIPT_NOTES_CHARS + 1)
        val padded = " ".repeat(MAX_RECEIPT_NOTES_CHARS) + "{}"
        val deep = "[".repeat(MAX_RECEIPT_JSON_DEPTH + 1) + "0" + "]".repeat(MAX_RECEIPT_JSON_DEPTH + 1)

        assertTrue(oversized.classifyReceiptEvidenceNotes() is ReceiptEvidenceNotes.Invalid)
        assertTrue(padded.classifyReceiptEvidenceNotes() is ReceiptEvidenceNotes.Invalid)
        assertTrue(deep.classifyReceiptEvidenceNotes() is ReceiptEvidenceNotes.Invalid)
    }

    @Test
    fun freeTextMarkerRemainsManualNotes() {
        val notes = "Support mentioned \"receiptFilePath\":\"example\" in plain text."

        assertEquals(ReceiptEvidenceNotes.Manual, notes.classifyReceiptEvidenceNotes())
    }
}