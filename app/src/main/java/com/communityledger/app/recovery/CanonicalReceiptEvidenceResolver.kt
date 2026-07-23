package com.communityledger.app.recovery

import android.util.JsonReader
import android.util.JsonToken
import android.util.JsonWriter
import com.communityledger.app.data.TransactionEntity
import com.communityledger.app.receipt.ReceiptEvidenceStore
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.OutputStreamWriter
import java.io.StringReader
import java.math.BigDecimal
import java.math.BigInteger
import java.nio.ByteBuffer
import java.nio.charset.CodingErrorAction
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

class InvalidCanonicalReceiptEvidenceException(message: String, cause: Throwable? = null) :
    IllegalArgumentException(message, cause)

data class CanonicalReceiptEvidence(
    val descriptor: LedgerBackupReceiptEvidence,
    val bytes: ByteArray
)

class CanonicalReceiptEvidenceResolver(
    private val store: ReceiptEvidenceStore,
    private val maxSourceBytes: Int = MAX_CANONICAL_RECEIPT_BYTES
) {
    fun resolve(transaction: TransactionEntity): CanonicalReceiptEvidence {
        val sourceFile = store.referencedEvidenceFile(transaction.eventId, transaction.notes)
            ?: fail("Transaction does not reference a confined receipt evidence file.")
        val sourceBytes = readBounded(sourceFile, maxSourceBytes)
        val root = parseObject(decodeUtf8(sourceBytes))
        validateRootFields(root)
        crossCheck(transaction, sourceFile, root)

        val portable = linkedMapOf<String, EvidenceJsonValue>()
        PORTABLE_FIELD_ORDER.forEach { field ->
            val value = root[field] ?: fail("Receipt evidence is missing '$field'.")
            portable[field] = if (field == "duplicateCheck") {
                canonicalDuplicateCheck(value)
            } else {
                value
            }
        }
        val canonicalBytes = writeCanonical(EvidenceJsonValue.ObjectValue(portable))
        val sha256 = MessageDigest.getInstance("SHA-256")
            .digest(canonicalBytes)
            .joinToString("") { byte -> "%02x".format(byte) }
        return CanonicalReceiptEvidence(
            descriptor = LedgerBackupReceiptEvidence(
                id = sha256.take(32),
                sourceTransactionId = transaction.id,
                sha256 = sha256,
                byteCount = canonicalBytes.size.toLong()
            ),
            bytes = canonicalBytes
        )
    }

    private fun validateRootFields(root: Map<String, EvidenceJsonValue>) {
        val unknown = root.keys - SOURCE_FIELDS
        if (unknown.isNotEmpty()) fail("Receipt evidence contains unknown field '${unknown.sorted().first()}'.")
        val missing = SOURCE_FIELDS - root.keys
        if (missing.isNotEmpty()) fail("Receipt evidence is missing '${missing.sorted().first()}'.")
    }

    private fun crossCheck(
        transaction: TransactionEntity,
        sourceFile: File,
        root: Map<String, EvidenceJsonValue>
    ) {
        if (root.requireExactInt("eventId") != transaction.eventId) fail("Receipt event does not match its transaction.")
        if (root.requireString("receiptFilePath") != sourceFile.absolutePath) fail("Receipt path does not match its confined file.")
        if (root.requireDecimal("amount").compareTo(BigDecimal.valueOf(transaction.amount)) != 0) {
            fail("Receipt amount does not match its transaction.")
        }
        if (root.requireDecimal("calculationAmount").compareTo(BigDecimal.valueOf(transaction.amount)) != 0) {
            fail("Receipt calculation amount does not match its transaction.")
        }
        if (root.requireString("currency") != "INR") fail("Receipt currency is not supported.")
        if (root.requireString("ledgerType") != transaction.type) fail("Receipt ledger type does not match its transaction.")
        if (root.requireString("ledgerPerson") != transaction.personName) fail("Receipt ledger person does not match its transaction.")
        if (root.requireString("uploaderEmail") != transaction.uploaderEmail) fail("Receipt uploader does not match its transaction.")

        val collected = transaction.type == "Donated" || transaction.type == "Credit"
        if (root.requireString("calculationBucket") != if (collected) "Total Collected" else "Total Spent") {
            fail("Receipt calculation bucket does not match its transaction.")
        }
        if (root.requireString("calculationOperation") != if (collected) "add" else "subtract") {
            fail("Receipt calculation operation does not match its transaction.")
        }

        val evidenceSource = root.requireString("amountEvidenceSource")
        if (evidenceSource !in RELIABLE_AMOUNT_EVIDENCE_SOURCES) fail("Receipt amount evidence is not reliable.")
        val evidenceConfidence = root.requireExactInt("amountEvidenceConfidence")
        if (evidenceConfidence !in MIN_RELIABLE_AMOUNT_EVIDENCE..100) fail("Receipt amount evidence confidence is not reliable.")
        if (root.requireBoolean("amountChangedDuringReview")) fail("Changed receipt amounts are not portable evidence.")
        if (root.requireBoolean("amountConfirmedDuringReview")) fail("Manually confirmed receipt amounts are not portable evidence.")

        val reference = root.nullableString("upiReferenceOrTransactionId").orEmpty()
        if (!reference.equals(transaction.transactionId, ignoreCase = true)) {
            fail("Receipt payment reference does not match its transaction.")
        }
        val duplicateCheck = root.requireObject("duplicateCheck")
        if (duplicateCheck.requireString("status") != "clear") fail("Receipt duplicate status is not clear.")
        if (duplicateCheck["matchedTransactionRowId"] !is EvidenceJsonValue.NullValue) {
            fail("Receipt duplicate match must be empty before packaging.")
        }
        if (duplicateCheck["reason"] !is EvidenceJsonValue.NullValue) {
            fail("Receipt duplicate reason must be empty before packaging.")
        }
    }

    private fun canonicalDuplicateCheck(value: EvidenceJsonValue): EvidenceJsonValue {
        val source = (value as? EvidenceJsonValue.ObjectValue)?.values
            ?: fail("Receipt duplicateCheck must be an object.")
        if (source.keys != DUPLICATE_CHECK_FIELDS) fail("Receipt duplicateCheck shape is invalid.")
        return EvidenceJsonValue.ObjectValue(
            linkedMapOf(
                "status" to requireNotNull(source["status"]),
                "reason" to requireNotNull(source["reason"])
            )
        )
    }

    private fun readBounded(file: File, maxBytes: Int): ByteArray {
        if (maxBytes <= 0 || file.length() > maxBytes) fail("Receipt evidence exceeds the supported size.")
        val output = ByteArrayOutputStream(minOf(file.length().toInt().coerceAtLeast(32), maxBytes))
        file.inputStream().buffered().use { input ->
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            var total = 0
            while (true) {
                val count = input.read(buffer)
                if (count < 0) break
                total += count
                if (total > maxBytes) fail("Receipt evidence exceeds the supported size.")
                output.write(buffer, 0, count)
            }
        }
        return output.toByteArray()
    }

    private fun decodeUtf8(bytes: ByteArray): String {
        return try {
            StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT)
                .decode(ByteBuffer.wrap(bytes))
                .toString()
        } catch (error: Exception) {
            throw InvalidCanonicalReceiptEvidenceException("Receipt evidence is not valid UTF-8.", error)
        }
    }

    private fun parseObject(text: String): Map<String, EvidenceJsonValue> {
        return try {
            JsonReader(StringReader(text)).use { reader ->
                reader.isLenient = false
                val value = readValue(reader, 0)
                if (reader.peek() != JsonToken.END_DOCUMENT) fail("Receipt evidence contains trailing JSON data.")
                (value as? EvidenceJsonValue.ObjectValue)?.values
                    ?: fail("Receipt evidence must be one JSON object.")
            }
        } catch (error: InvalidCanonicalReceiptEvidenceException) {
            throw error
        } catch (error: Exception) {
            throw InvalidCanonicalReceiptEvidenceException("Receipt evidence contains malformed JSON.", error)
        }
    }

    private fun readValue(reader: JsonReader, depth: Int): EvidenceJsonValue {
        if (depth > MAX_JSON_DEPTH) fail("Receipt evidence exceeds the supported JSON depth.")
        return when (reader.peek()) {
            JsonToken.BEGIN_OBJECT -> {
                val values = linkedMapOf<String, EvidenceJsonValue>()
                reader.beginObject()
                while (reader.hasNext()) {
                    if (values.size >= MAX_OBJECT_FIELDS) fail("Receipt evidence contains too many fields.")
                    val name = reader.nextName()
                    if (name.length > MAX_STRING_CHARS) fail("Receipt evidence field name is too long.")
                    if (name in values) fail("Receipt evidence contains duplicate field '$name'.")
                    values[name] = readValue(reader, depth + 1)
                }
                reader.endObject()
                EvidenceJsonValue.ObjectValue(values)
            }
            JsonToken.BEGIN_ARRAY -> {
                val values = mutableListOf<EvidenceJsonValue>()
                reader.beginArray()
                while (reader.hasNext()) {
                    if (values.size >= MAX_ARRAY_ITEMS) fail("Receipt evidence array is too large.")
                    values += readValue(reader, depth + 1)
                }
                reader.endArray()
                EvidenceJsonValue.ArrayValue(values)
            }
            JsonToken.STRING -> EvidenceJsonValue.StringValue(reader.nextString().also {
                if (it.length > MAX_STRING_CHARS) fail("Receipt evidence string is too long.")
            })
            JsonToken.NUMBER -> EvidenceJsonValue.NumberValue(BigDecimal(reader.nextString()))
            JsonToken.BOOLEAN -> EvidenceJsonValue.BooleanValue(reader.nextBoolean())
            JsonToken.NULL -> {
                reader.nextNull()
                EvidenceJsonValue.NullValue
            }
            else -> fail("Receipt evidence contains an unsupported JSON token.")
        }
    }

    private fun writeCanonical(root: EvidenceJsonValue.ObjectValue): ByteArray {
        val output = ByteArrayOutputStream()
        val encoder = StandardCharsets.UTF_8.newEncoder()
            .onMalformedInput(CodingErrorAction.REPORT)
            .onUnmappableCharacter(CodingErrorAction.REPORT)
        try {
            JsonWriter(OutputStreamWriter(output, encoder)).use { writer -> writeValue(writer, root) }
        } catch (error: Exception) {
            throw InvalidCanonicalReceiptEvidenceException("Receipt evidence cannot be encoded as strict UTF-8.", error)
        }
        val bytes = output.toByteArray()
        if (bytes.size > maxSourceBytes) fail("Canonical receipt evidence exceeds the supported size.")
        return bytes
    }

    private fun writeValue(writer: JsonWriter, value: EvidenceJsonValue) {
        when (value) {
            is EvidenceJsonValue.ObjectValue -> {
                writer.beginObject()
                value.values.forEach { (name, child) ->
                    writer.name(name)
                    writeValue(writer, child)
                }
                writer.endObject()
            }
            is EvidenceJsonValue.ArrayValue -> {
                writer.beginArray()
                value.values.forEach { writeValue(writer, it) }
                writer.endArray()
            }
            is EvidenceJsonValue.StringValue -> writer.value(value.value)
            is EvidenceJsonValue.NumberValue -> writer.value(value.value)
            is EvidenceJsonValue.BooleanValue -> writer.value(value.value)
            EvidenceJsonValue.NullValue -> writer.nullValue()
        }
    }

    private sealed interface EvidenceJsonValue {
        data class ObjectValue(val values: LinkedHashMap<String, EvidenceJsonValue>) : EvidenceJsonValue
        data class ArrayValue(val values: List<EvidenceJsonValue>) : EvidenceJsonValue
        data class StringValue(val value: String) : EvidenceJsonValue
        data class NumberValue(val value: BigDecimal) : EvidenceJsonValue
        data class BooleanValue(val value: Boolean) : EvidenceJsonValue
        data object NullValue : EvidenceJsonValue
    }

    private fun Map<String, EvidenceJsonValue>.requireString(name: String): String {
        return (this[name] as? EvidenceJsonValue.StringValue)?.value ?: fail("Receipt '$name' must be a string.")
    }

    private fun Map<String, EvidenceJsonValue>.nullableString(name: String): String? {
        return when (val value = this[name]) {
            EvidenceJsonValue.NullValue -> null
            is EvidenceJsonValue.StringValue -> value.value
            else -> fail("Receipt '$name' must be a string or null.")
        }
    }

    private fun Map<String, EvidenceJsonValue>.requireDecimal(name: String): BigDecimal {
        return (this[name] as? EvidenceJsonValue.NumberValue)?.value ?: fail("Receipt '$name' must be a number.")
    }

    private fun Map<String, EvidenceJsonValue>.requireExactInt(name: String): Int {
        return try {
            val value = requireDecimal(name).toBigIntegerExact()
            if (value < MIN_INT_BIG_INTEGER || value > MAX_INT_BIG_INTEGER) {
                fail("Receipt '$name' must be an exact integer.")
            }
            value.toInt()
        } catch (error: ArithmeticException) {
            fail("Receipt '$name' must be an exact integer.")
        }
    }

    private fun Map<String, EvidenceJsonValue>.requireBoolean(name: String): Boolean {
        return (this[name] as? EvidenceJsonValue.BooleanValue)?.value ?: fail("Receipt '$name' must be a boolean.")
    }

    private fun Map<String, EvidenceJsonValue>.requireObject(name: String): Map<String, EvidenceJsonValue> {
        return (this[name] as? EvidenceJsonValue.ObjectValue)?.values ?: fail("Receipt '$name' must be an object.")
    }

    private fun fail(message: String): Nothing = throw InvalidCanonicalReceiptEvidenceException(message)

    private companion object {
        const val MAX_CANONICAL_RECEIPT_BYTES = 512 * 1024
        const val MAX_JSON_DEPTH = 16
        const val MAX_OBJECT_FIELDS = 128
        const val MAX_ARRAY_ITEMS = 256
        const val MAX_STRING_CHARS = 64 * 1024
        const val MIN_RELIABLE_AMOUNT_EVIDENCE = 65

        val MIN_INT_BIG_INTEGER: BigInteger = BigInteger.valueOf(Int.MIN_VALUE.toLong())
        val MAX_INT_BIG_INTEGER: BigInteger = BigInteger.valueOf(Int.MAX_VALUE.toLong())

        val RELIABLE_AMOUNT_EVIDENCE_SOURCES = setOf(
            "CURRENCY_MARKED",
            "AMOUNT_LABEL",
            "NEAR_AMOUNT_LABEL",
            "PARTY_LINE_AMOUNT",
            "TOP_RECEIPT_VALUE"
        )
        val DUPLICATE_CHECK_FIELDS = setOf("status", "matchedTransactionRowId", "reason")
        val SOURCE_FIELDS = linkedSetOf(
            "amount",
            "currency",
            "calculationAmount",
            "calculationBucket",
            "calculationOperation",
            "ocrDetectedAmount",
            "amountEvidenceSource",
            "amountEvidenceConfidence",
            "amountChangedDuringReview",
            "amountConfirmedDuringReview",
            "counterpartyName",
            "upiId",
            "upiReferenceOrTransactionId",
            "paymentApp",
            "date",
            "phone",
            "email",
            "ledgerType",
            "ledgerPerson",
            "ledgerPersonSource",
            "uploaderEmail",
            "lastEditedBy",
            "extractionMethod",
            "confidence",
            "warnings",
            "duplicateCheck",
            "eventId",
            "storedAt",
            "receiptFilePath"
        )
        val PORTABLE_FIELD_ORDER = SOURCE_FIELDS.filterNot {
            it == "eventId" || it == "storedAt" || it == "receiptFilePath"
        }
    }
}