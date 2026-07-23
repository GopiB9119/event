package com.communityledger.app.recovery

import com.communityledger.app.data.TransactionEntity
import com.communityledger.app.receipt.ReceiptEvidenceStore
import java.io.File
import java.security.MessageDigest
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class CanonicalReceiptEvidenceResolverTest {
    @get:Rule
    val temporaryFolder = TemporaryFolder()

    @Test
    fun derivesDeterministicPortableBytesHashAndCount() {
        val store = ReceiptEvidenceStore(temporaryFolder.root)
        val transaction = transaction()
        val firstNotes = writeStoredReceipt(store, transaction, productionReceiptJson())
        val secondNotes = writeStoredReceipt(store, transaction, productionReceiptJson(reverseOrder = true))
        val resolver = CanonicalReceiptEvidenceResolver(store)

        val first = resolver.resolve(transaction.copy(notes = firstNotes))
        val second = resolver.resolve(transaction.copy(notes = secondNotes))
        val text = first.bytes.toString(Charsets.UTF_8)
        val expectedHash = MessageDigest.getInstance("SHA-256")
            .digest(first.bytes)
            .joinToString("") { byte -> "%02x".format(byte) }

        assertArrayEquals(first.bytes, second.bytes)
        assertEquals(expectedHash, first.descriptor.sha256)
        assertEquals(expectedHash.take(32), first.descriptor.id)
        assertEquals(first.bytes.size.toLong(), first.descriptor.byteCount)
        assertFalse(text.contains("receiptFilePath"))
        assertFalse(text.contains("storedAt"))
        assertFalse(text.contains("eventId"))
        assertFalse(text.contains("matchedTransactionRowId"))
        assertTrue(text.contains("\"amountEvidenceSource\":\"AMOUNT_LABEL\""))
    }

    @Test
    fun rejectsFinancialIdentityAndDuplicateMismatches() {
        val store = ReceiptEvidenceStore(temporaryFolder.root)
        val transaction = transaction()
        val resolver = CanonicalReceiptEvidenceResolver(store)
        val mismatches = listOf(
            productionReceiptJson().put("amount", 501.0),
            productionReceiptJson().put("calculationAmount", 501.0),
            productionReceiptJson().put("ledgerType", "Expense"),
            productionReceiptJson().put("uploaderEmail", "other@example.com"),
            productionReceiptJson().put("amountEvidenceSource", "USER_ENTERED"),
            productionReceiptJson().put("amountEvidenceConfidence", 64),
            productionReceiptJson().put("amountEvidenceConfidence", 2_147_483_648L),
            productionReceiptJson().put("amountChangedDuringReview", true),
            productionReceiptJson().put("duplicateCheck", JSONObject().apply {
                put("status", "possible_duplicate")
                put("matchedTransactionRowId", 1)
                put("reason", "duplicate")
            })
        )

        mismatches.forEach { receipt ->
            val notes = writeStoredReceipt(store, transaction, receipt)
            assertThrows(InvalidCanonicalReceiptEvidenceException::class.java) {
                resolver.resolve(transaction.copy(notes = notes))
            }
        }
    }

    @Test
    fun rejectsDuplicateUnknownMalformedAndInvalidUtf8Json() {
        val store = ReceiptEvidenceStore(temporaryFolder.root)
        val transaction = transaction()
        val resolver = CanonicalReceiptEvidenceResolver(store)

        val duplicate = productionReceiptJson().toString().replaceFirst(
            "\"amount\":500",
            "\"amount\":500,\"amount\":500"
        )
        val unknown = productionReceiptJson().put("unexpected", "value").toString()
        val malformed = "{\"amount\":500"

        listOf(duplicate.toByteArray(), unknown.toByteArray(), malformed.toByteArray()).forEachIndexed { index, bytes ->
            val notes = writeRawLinkedFile(transaction.eventId, "invalid_$index.json", bytes)
            assertThrows(InvalidCanonicalReceiptEvidenceException::class.java) {
                resolver.resolve(transaction.copy(notes = notes))
            }
        }

        val invalidUtf8Notes = writeRawLinkedFile(
            transaction.eventId,
            "invalid_utf8.json",
            byteArrayOf(0xC3.toByte(), 0x28)
        )
        assertThrows(InvalidCanonicalReceiptEvidenceException::class.java) {
            resolver.resolve(transaction.copy(notes = invalidUtf8Notes))
        }
    }

    @Test
    fun rejectsOversizedAndCrossEventEvidence() {
        val store = ReceiptEvidenceStore(temporaryFolder.root)
        val transaction = transaction()
        val resolver = CanonicalReceiptEvidenceResolver(store)
        val oversizedNotes = writeRawLinkedFile(
            transaction.eventId,
            "oversized.json",
            ByteArray(512 * 1024 + 1) { 'x'.code.toByte() }
        )

        assertThrows(InvalidCanonicalReceiptEvidenceException::class.java) {
            resolver.resolve(transaction.copy(notes = oversizedNotes))
        }

        val otherEventNotes = writeRawLinkedFile(99, "other_event.json", productionReceiptJson().toString().toByteArray())
        assertThrows(InvalidCanonicalReceiptEvidenceException::class.java) {
            resolver.resolve(transaction.copy(notes = otherEventNotes))
        }
    }

    private fun writeStoredReceipt(
        store: ReceiptEvidenceStore,
        transaction: TransactionEntity,
        receipt: JSONObject
    ): String {
        val path = requireNotNull(
            store.writeReceipt(
                eventId = transaction.eventId,
                personName = transaction.personName,
                uploaderEmail = transaction.uploaderEmail,
                receiptJsonText = receipt.toString()
            )
        )
        return JSONObject().put("receiptFilePath", path).toString()
    }

    private fun writeRawLinkedFile(eventId: Int, name: String, bytes: ByteArray): String {
        val directory = File(temporaryFolder.root, "receipts/event_$eventId/manual").apply { mkdirs() }
        val file = File(directory, name).canonicalFile
        file.writeBytes(bytes)
        return JSONObject().put("receiptFilePath", file.absolutePath).toString()
    }

    private fun productionReceiptJson(reverseOrder: Boolean = false): JSONObject {
        val values = linkedMapOf<String, Any?>(
            "amount" to 500.0,
            "currency" to "INR",
            "calculationAmount" to 500.0,
            "calculationBucket" to "Total Collected",
            "calculationOperation" to "add",
            "ocrDetectedAmount" to 500.0,
            "amountEvidenceSource" to "AMOUNT_LABEL",
            "amountEvidenceConfidence" to 80,
            "amountChangedDuringReview" to false,
            "amountConfirmedDuringReview" to false,
            "counterpartyName" to "Community Hall",
            "upiId" to "hall@example",
            "upiReferenceOrTransactionId" to "UPI-101",
            "paymentApp" to "PhonePe",
            "date" to "15 July 2026",
            "phone" to JSONObject.NULL,
            "email" to JSONObject.NULL,
            "ledgerType" to "Donated",
            "ledgerPerson" to "Asha",
            "ledgerPersonSource" to "User-entered during review",
            "uploaderEmail" to "organizer@example.com",
            "lastEditedBy" to JSONObject.NULL,
            "extractionMethod" to "ML Kit Latin",
            "confidence" to 90,
            "warnings" to JSONArray(),
            "duplicateCheck" to JSONObject().apply {
                put("status", "clear")
                put("matchedTransactionRowId", JSONObject.NULL)
                put("reason", JSONObject.NULL)
            }
        )
        val entries = if (reverseOrder) values.entries.reversed() else values.entries
        return JSONObject().apply { entries.forEach { (key, value) -> put(key, value) } }
    }

    private fun transaction() = TransactionEntity(
        id = 101,
        eventId = 7,
        memberId = 11,
        personName = "Asha",
        personPhone = "",
        personEmail = "",
        amount = 500.0,
        type = "Donated",
        transactionId = "UPI-101",
        uploaderEmail = "organizer@example.com"
    )
}