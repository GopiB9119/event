package com.communityledger.app.recovery

import android.util.JsonReader
import android.util.JsonToken
import android.util.JsonWriter
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import com.communityledger.app.data.MAX_LEDGER_SOURCE_EVENTS
import com.communityledger.app.data.MAX_LEDGER_SOURCE_MEMBERS
import com.communityledger.app.data.MAX_LEDGER_SOURCE_TRANSACTIONS
import java.nio.charset.CharacterCodingException
import java.nio.charset.CodingErrorAction
import java.nio.charset.StandardCharsets

class InvalidLedgerBackupManifestException(message: String, cause: Throwable? = null) :
    IllegalArgumentException(message, cause)

object LedgerBackupManifestCodec {
    const val MAX_MANIFEST_BYTES = 16 * 1024 * 1024
    const val MAX_EVENTS = MAX_LEDGER_SOURCE_EVENTS
    const val MAX_MEMBERS = MAX_LEDGER_SOURCE_MEMBERS
    const val MAX_TRANSACTIONS = MAX_LEDGER_SOURCE_TRANSACTIONS
    const val MAX_RECEIPT_EVIDENCE = 250_000

    fun encode(payload: LedgerBackupPayload): ByteArray {
        val output = ByteArrayOutputStream()
        encode(payload, output)
        return output.toByteArray()
    }

    fun encode(payload: LedgerBackupPayload, output: OutputStream) {
        requireWithinBounds(payload)
        requireValidPayload(payload)

        val encoder = StandardCharsets.UTF_8.newEncoder()
            .onMalformedInput(CodingErrorAction.REPORT)
            .onUnmappableCharacter(CodingErrorAction.REPORT)
        try {
            JsonWriter(
                OutputStreamWriter(
                    BoundedOutputStream(NonClosingOutputStream(output), MAX_MANIFEST_BYTES),
                    encoder
                )
            ).use { writer ->
                writePayload(writer, payload)
            }
        } catch (error: ManifestByteLimitExceededException) {
            throw InvalidLedgerBackupManifestException("Backup manifest exceeds the supported byte size.", error)
        } catch (error: CharacterCodingException) {
            throw InvalidLedgerBackupManifestException("Backup manifest contains invalid Unicode.", error)
        }
    }

    fun decode(bytes: ByteArray): LedgerBackupPayload {
        return decode(ByteArrayInputStream(bytes))
    }

    fun decode(input: InputStream): LedgerBackupPayload {
        val decoder = StandardCharsets.UTF_8.newDecoder()
            .onMalformedInput(CodingErrorAction.REPORT)
            .onUnmappableCharacter(CodingErrorAction.REPORT)
        val payload = try {
            JsonReader(InputStreamReader(BoundedInputStream(input, MAX_MANIFEST_BYTES), decoder)).use { reader ->
                reader.isLenient = false
                val budget = DecodeBudget()
                val decoded = readPayload(reader, budget)
                if (reader.peek() != JsonToken.END_DOCUMENT) {
                    fail("Backup manifest contains trailing JSON data.")
                }
                decoded
            }
        } catch (error: InvalidLedgerBackupManifestException) {
            throw error
        } catch (error: ManifestByteLimitExceededException) {
            throw InvalidLedgerBackupManifestException("Backup manifest exceeds the supported byte size.", error)
        } catch (error: CharacterCodingException) {
            throw InvalidLedgerBackupManifestException("Backup manifest is not valid UTF-8.", error)
        } catch (error: Exception) {
            throw InvalidLedgerBackupManifestException("Backup manifest contains malformed JSON.", error)
        }

        requireWithinBounds(payload)
        requireValidPayload(payload)
        return payload
    }

    private fun writePayload(writer: JsonWriter, payload: LedgerBackupPayload) {
        writer.beginObject()
        writer.name("payloadVersion").value(payload.payloadVersion.toLong())
        writer.name("sourceSchemaVersion").value(payload.sourceSchemaVersion.toLong())
        writer.name("backupId").value(payload.backupId)
        writer.name("createdAtEpochMillis").value(payload.createdAtEpochMillis)
        writer.name("sourcePreferences")
        writePreferences(writer, payload.sourcePreferences)
        writer.name("events").beginArray()
        payload.events.sortedBy(LedgerBackupEvent::sourceId).forEach { writeEvent(writer, it) }
        writer.endArray()
        writer.endObject()
    }

    private fun writePreferences(writer: JsonWriter, preferences: LedgerBackupSourcePreferences) {
        writer.beginObject()
        writer.name("userEmail").value(preferences.userEmail)
        writer.name("themeMode").value(preferences.themeMode)
        writer.endObject()
    }

    private fun writeEvent(writer: JsonWriter, event: LedgerBackupEvent) {
        writer.beginObject()
        writer.name("sourceId").value(event.sourceId.toLong())
        writer.name("eventKey").value(event.eventKey)
        writer.name("title").value(event.title)
        writer.name("duration").writeNullableString(event.duration)
        writer.name("createdDate").value(event.createdDate)
        writer.name("isPrivate").value(event.isPrivate)
        writer.name("customFieldsJson").value(event.customFieldsJson)
        writer.name("members").beginArray()
        event.members.sortedBy(LedgerBackupMember::sourceId).forEach { writeMember(writer, it) }
        writer.endArray()
        writer.name("transactions").beginArray()
        event.transactions.sortedBy(LedgerBackupTransaction::sourceId).forEach { writeTransaction(writer, it) }
        writer.endArray()
        writer.name("receiptEvidence").beginArray()
        event.receiptEvidence
            .sortedWith(compareBy(LedgerBackupReceiptEvidence::sourceTransactionId, LedgerBackupReceiptEvidence::id))
            .forEach { writeReceiptEvidence(writer, it) }
        writer.endArray()
        writer.endObject()
    }

    private fun writeMember(writer: JsonWriter, member: LedgerBackupMember) {
        writer.beginObject()
        writer.name("sourceId").value(member.sourceId.toLong())
        writer.name("name").value(member.name)
        writer.name("normalizedName").value(member.normalizedName)
        writer.name("phone").value(member.phone)
        writer.name("email").value(member.email)
        writer.name("role").value(member.role)
        writer.name("createdAt").value(member.createdAt)
        writer.endObject()
    }

    private fun writeTransaction(writer: JsonWriter, transaction: LedgerBackupTransaction) {
        writer.beginObject()
        writer.name("sourceId").value(transaction.sourceId.toLong())
        writer.name("sourceMemberId").writeNullableInt(transaction.sourceMemberId)
        writer.name("personName").value(transaction.personName)
        writer.name("personPhone").value(transaction.personPhone)
        writer.name("personEmail").value(transaction.personEmail)
        writer.name("amount").value(transaction.amount)
        writer.name("type").value(transaction.type)
        writer.name("date").value(transaction.date)
        writer.name("paymentReference").value(transaction.paymentReference)
        writer.name("isVerified").value(transaction.isVerified)
        writer.name("manualNotes").writeNullableString(transaction.manualNotes)
        writer.name("uploaderEmail").value(transaction.uploaderEmail)
        writer.name("receiptEvidenceId").writeNullableString(transaction.receiptEvidenceId)
        writer.endObject()
    }

    private fun writeReceiptEvidence(writer: JsonWriter, evidence: LedgerBackupReceiptEvidence) {
        writer.beginObject()
        writer.name("id").value(evidence.id)
        writer.name("sourceTransactionId").value(evidence.sourceTransactionId.toLong())
        writer.name("sha256").value(evidence.sha256)
        writer.name("byteCount").value(evidence.byteCount)
        writer.endObject()
    }

    private fun readPayload(reader: JsonReader, budget: DecodeBudget): LedgerBackupPayload {
        val fields = FieldTracker("manifest")
        var payloadVersion: Int? = null
        var sourceSchemaVersion: Int? = null
        var backupId: String? = null
        var createdAtEpochMillis: Long? = null
        var sourcePreferences: LedgerBackupSourcePreferences? = null
        var events: List<LedgerBackupEvent>? = null

        reader.beginObject()
        while (reader.hasNext()) {
            when (val name = fields.next(reader)) {
                "payloadVersion" -> payloadVersion = reader.readInt("payloadVersion")
                "sourceSchemaVersion" -> sourceSchemaVersion = reader.readInt("sourceSchemaVersion")
                "backupId" -> backupId = reader.readString("backupId", MAX_LOGICAL_ID_CHARS)
                "createdAtEpochMillis" -> createdAtEpochMillis = reader.readLong("createdAtEpochMillis")
                "sourcePreferences" -> sourcePreferences = readPreferences(reader)
                "events" -> events = reader.readArray(MAX_EVENTS, "events") {
                    budget.events++
                    readEvent(reader, budget)
                }
                else -> fail("Unknown field 'manifest.$name'.")
            }
        }
        reader.endObject()
        fields.require(PAYLOAD_FIELDS)

        return LedgerBackupPayload(
            payloadVersion = requireNotNull(payloadVersion),
            sourceSchemaVersion = requireNotNull(sourceSchemaVersion),
            backupId = requireNotNull(backupId),
            createdAtEpochMillis = requireNotNull(createdAtEpochMillis),
            sourcePreferences = requireNotNull(sourcePreferences),
            events = requireNotNull(events)
        )
    }

    private fun readPreferences(reader: JsonReader): LedgerBackupSourcePreferences {
        val fields = FieldTracker("sourcePreferences")
        var userEmail: String? = null
        var themeMode: String? = null
        reader.beginObject()
        while (reader.hasNext()) {
            when (val name = fields.next(reader)) {
                "userEmail" -> userEmail = reader.readString("sourcePreferences.userEmail", MAX_EMAIL_CHARS)
                "themeMode" -> themeMode = reader.readString("sourcePreferences.themeMode", MAX_SHORT_STRING_CHARS)
                else -> fail("Unknown field 'sourcePreferences.$name'.")
            }
        }
        reader.endObject()
        fields.require(PREFERENCE_FIELDS)
        return LedgerBackupSourcePreferences(requireNotNull(userEmail), requireNotNull(themeMode))
    }

    private fun readEvent(reader: JsonReader, budget: DecodeBudget): LedgerBackupEvent {
        val path = "events[${budget.events - 1}]"
        val fields = FieldTracker(path)
        var sourceId: Int? = null
        var eventKey: String? = null
        var title: String? = null
        var durationSeen = false
        var duration: String? = null
        var createdDate: Long? = null
        var isPrivate: Boolean? = null
        var customFieldsJson: String? = null
        var members: List<LedgerBackupMember>? = null
        var transactions: List<LedgerBackupTransaction>? = null
        var receiptEvidence: List<LedgerBackupReceiptEvidence>? = null

        reader.beginObject()
        while (reader.hasNext()) {
            when (val name = fields.next(reader)) {
                "sourceId" -> sourceId = reader.readInt("$path.sourceId")
                "eventKey" -> eventKey = reader.readString("$path.eventKey", MAX_LOGICAL_ID_CHARS)
                "title" -> title = reader.readString("$path.title", MAX_LONG_STRING_CHARS)
                "duration" -> {
                    durationSeen = true
                    duration = reader.readNullableString("$path.duration", MAX_LONG_STRING_CHARS)
                }
                "createdDate" -> createdDate = reader.readLong("$path.createdDate")
                "isPrivate" -> isPrivate = reader.readBoolean("$path.isPrivate")
                "customFieldsJson" -> customFieldsJson = reader.readString(
                    "$path.customFieldsJson",
                    MAX_CUSTOM_FIELDS_CHARS
                )
                "members" -> members = reader.readArray(MAX_MEMBERS - budget.members, "$path.members") {
                    budget.members++
                    readMember(reader, "$path.members[${budget.members - 1}]")
                }
                "transactions" -> transactions = reader.readArray(
                    MAX_TRANSACTIONS - budget.transactions,
                    "$path.transactions"
                ) {
                    budget.transactions++
                    readTransaction(reader, "$path.transactions[${budget.transactions - 1}]")
                }
                "receiptEvidence" -> receiptEvidence = reader.readArray(
                    MAX_RECEIPT_EVIDENCE - budget.receiptEvidence,
                    "$path.receiptEvidence"
                ) {
                    budget.receiptEvidence++
                    readReceiptEvidence(reader, "$path.receiptEvidence[${budget.receiptEvidence - 1}]")
                }
                else -> fail("Unknown field '$path.$name'.")
            }
        }
        reader.endObject()
        fields.require(EVENT_FIELDS)
        if (!durationSeen) fail("Missing field '$path.duration'.")

        return LedgerBackupEvent(
            sourceId = requireNotNull(sourceId),
            eventKey = requireNotNull(eventKey),
            title = requireNotNull(title),
            duration = duration,
            createdDate = requireNotNull(createdDate),
            isPrivate = requireNotNull(isPrivate),
            customFieldsJson = requireNotNull(customFieldsJson),
            members = requireNotNull(members),
            transactions = requireNotNull(transactions),
            receiptEvidence = requireNotNull(receiptEvidence)
        )
    }

    private fun readMember(reader: JsonReader, path: String): LedgerBackupMember {
        val fields = FieldTracker(path)
        var sourceId: Int? = null
        var name: String? = null
        var normalizedName: String? = null
        var phone: String? = null
        var email: String? = null
        var role: String? = null
        var createdAt: Long? = null
        reader.beginObject()
        while (reader.hasNext()) {
            when (val field = fields.next(reader)) {
                "sourceId" -> sourceId = reader.readInt("$path.sourceId")
                "name" -> name = reader.readString("$path.name", MAX_LONG_STRING_CHARS)
                "normalizedName" -> normalizedName = reader.readString("$path.normalizedName", MAX_LONG_STRING_CHARS)
                "phone" -> phone = reader.readString("$path.phone", MAX_CONTACT_STRING_CHARS)
                "email" -> email = reader.readString("$path.email", MAX_CONTACT_STRING_CHARS)
                "role" -> role = reader.readString("$path.role", MAX_SHORT_STRING_CHARS)
                "createdAt" -> createdAt = reader.readLong("$path.createdAt")
                else -> fail("Unknown field '$path.$field'.")
            }
        }
        reader.endObject()
        fields.require(MEMBER_FIELDS)
        return LedgerBackupMember(
            sourceId = requireNotNull(sourceId),
            name = requireNotNull(name),
            normalizedName = requireNotNull(normalizedName),
            phone = requireNotNull(phone),
            email = requireNotNull(email),
            role = requireNotNull(role),
            createdAt = requireNotNull(createdAt)
        )
    }

    private fun readTransaction(reader: JsonReader, path: String): LedgerBackupTransaction {
        val fields = FieldTracker(path)
        var sourceId: Int? = null
        var sourceMemberIdSeen = false
        var sourceMemberId: Int? = null
        var personName: String? = null
        var personPhone: String? = null
        var personEmail: String? = null
        var amount: Double? = null
        var type: String? = null
        var date: Long? = null
        var paymentReference: String? = null
        var isVerified: Boolean? = null
        var manualNotesSeen = false
        var manualNotes: String? = null
        var uploaderEmail: String? = null
        var receiptEvidenceIdSeen = false
        var receiptEvidenceId: String? = null

        reader.beginObject()
        while (reader.hasNext()) {
            when (val field = fields.next(reader)) {
                "sourceId" -> sourceId = reader.readInt("$path.sourceId")
                "sourceMemberId" -> {
                    sourceMemberIdSeen = true
                    sourceMemberId = reader.readNullableInt("$path.sourceMemberId")
                }
                "personName" -> personName = reader.readString("$path.personName", MAX_LONG_STRING_CHARS)
                "personPhone" -> personPhone = reader.readString("$path.personPhone", MAX_CONTACT_STRING_CHARS)
                "personEmail" -> personEmail = reader.readString("$path.personEmail", MAX_CONTACT_STRING_CHARS)
                "amount" -> amount = reader.readFiniteDouble("$path.amount")
                "type" -> type = reader.readString("$path.type", MAX_SHORT_STRING_CHARS)
                "date" -> date = reader.readLong("$path.date")
                "paymentReference" -> paymentReference = reader.readString(
                    "$path.paymentReference",
                    MAX_LONG_STRING_CHARS
                )
                "isVerified" -> isVerified = reader.readBoolean("$path.isVerified")
                "manualNotes" -> {
                    manualNotesSeen = true
                    manualNotes = reader.readNullableString("$path.manualNotes", MAX_MANUAL_NOTES_CHARS)
                }
                "uploaderEmail" -> uploaderEmail = reader.readString("$path.uploaderEmail", MAX_EMAIL_CHARS)
                "receiptEvidenceId" -> {
                    receiptEvidenceIdSeen = true
                    receiptEvidenceId = reader.readNullableString(
                        "$path.receiptEvidenceId",
                        MAX_LOGICAL_ID_CHARS
                    )
                }
                else -> fail("Unknown field '$path.$field'.")
            }
        }
        reader.endObject()
        fields.require(TRANSACTION_FIELDS)
        if (!sourceMemberIdSeen) fail("Missing field '$path.sourceMemberId'.")
        if (!manualNotesSeen) fail("Missing field '$path.manualNotes'.")
        if (!receiptEvidenceIdSeen) fail("Missing field '$path.receiptEvidenceId'.")

        return LedgerBackupTransaction(
            sourceId = requireNotNull(sourceId),
            sourceMemberId = sourceMemberId,
            personName = requireNotNull(personName),
            personPhone = requireNotNull(personPhone),
            personEmail = requireNotNull(personEmail),
            amount = requireNotNull(amount),
            type = requireNotNull(type),
            date = requireNotNull(date),
            paymentReference = requireNotNull(paymentReference),
            isVerified = requireNotNull(isVerified),
            manualNotes = manualNotes,
            uploaderEmail = requireNotNull(uploaderEmail),
            receiptEvidenceId = receiptEvidenceId
        )
    }

    private fun readReceiptEvidence(reader: JsonReader, path: String): LedgerBackupReceiptEvidence {
        val fields = FieldTracker(path)
        var id: String? = null
        var sourceTransactionId: Int? = null
        var sha256: String? = null
        var byteCount: Long? = null
        reader.beginObject()
        while (reader.hasNext()) {
            when (val field = fields.next(reader)) {
                "id" -> id = reader.readString("$path.id", MAX_LOGICAL_ID_CHARS)
                "sourceTransactionId" -> sourceTransactionId = reader.readInt("$path.sourceTransactionId")
                "sha256" -> sha256 = reader.readString("$path.sha256", SHA256_HEX_CHARS)
                "byteCount" -> byteCount = reader.readLong("$path.byteCount")
                else -> fail("Unknown field '$path.$field'.")
            }
        }
        reader.endObject()
        fields.require(EVIDENCE_FIELDS)
        return LedgerBackupReceiptEvidence(
            id = requireNotNull(id),
            sourceTransactionId = requireNotNull(sourceTransactionId),
            sha256 = requireNotNull(sha256),
            byteCount = requireNotNull(byteCount)
        )
    }

    private fun requireValidPayload(payload: LedgerBackupPayload) {
        val errors = validateLedgerBackupPayload(payload)
        if (errors.isNotEmpty()) {
            val displayedErrors = errors.take(MAX_REPORTED_VALIDATION_ERRORS)
            val omitted = errors.size - displayedErrors.size
            throw InvalidLedgerBackupManifestException(
                displayedErrors.joinToString(prefix = "Backup manifest is invalid: ") {
                    "${it.path}: ${it.message}"
                } + if (omitted > 0) " ($omitted additional errors omitted.)" else ""
            )
        }
    }

    private fun requireWithinBounds(payload: LedgerBackupPayload) {
        if (payload.events.size > MAX_EVENTS) fail("Backup manifest contains too many events.")
        if (payload.events.sumOf { it.members.size.toLong() } > MAX_MEMBERS) {
            fail("Backup manifest contains too many members.")
        }
        if (payload.events.sumOf { it.transactions.size.toLong() } > MAX_TRANSACTIONS) {
            fail("Backup manifest contains too many transactions.")
        }
        if (payload.events.sumOf { it.receiptEvidence.size.toLong() } > MAX_RECEIPT_EVIDENCE) {
            fail("Backup manifest contains too many receipt evidence descriptors.")
        }
        requireStringBound(payload.backupId, MAX_LOGICAL_ID_CHARS, "backupId")
        requireStringBound(payload.sourcePreferences.userEmail, MAX_EMAIL_CHARS, "sourcePreferences.userEmail")
        requireStringBound(payload.sourcePreferences.themeMode, MAX_SHORT_STRING_CHARS, "sourcePreferences.themeMode")
        payload.events.forEachIndexed { eventIndex, event ->
            val path = "events[$eventIndex]"
            requireStringBound(event.eventKey, MAX_LOGICAL_ID_CHARS, "$path.eventKey")
            requireStringBound(event.title, MAX_LONG_STRING_CHARS, "$path.title")
            event.duration?.let { requireStringBound(it, MAX_LONG_STRING_CHARS, "$path.duration") }
            requireStringBound(event.customFieldsJson, MAX_CUSTOM_FIELDS_CHARS, "$path.customFieldsJson")
            event.members.forEachIndexed { memberIndex, member ->
                val memberPath = "$path.members[$memberIndex]"
                requireStringBound(member.name, MAX_LONG_STRING_CHARS, "$memberPath.name")
                requireStringBound(
                    member.normalizedName,
                    MAX_LONG_STRING_CHARS,
                    "$memberPath.normalizedName"
                )
                requireStringBound(member.phone, MAX_CONTACT_STRING_CHARS, "$memberPath.phone")
                requireStringBound(member.email, MAX_CONTACT_STRING_CHARS, "$memberPath.email")
                requireStringBound(member.role, MAX_SHORT_STRING_CHARS, "$memberPath.role")
            }
            event.transactions.forEachIndexed { transactionIndex, transaction ->
                val transactionPath = "$path.transactions[$transactionIndex]"
                requireStringBound(transaction.personName, MAX_LONG_STRING_CHARS, "$transactionPath.personName")
                requireStringBound(transaction.personPhone, MAX_CONTACT_STRING_CHARS, "$transactionPath.personPhone")
                requireStringBound(transaction.personEmail, MAX_CONTACT_STRING_CHARS, "$transactionPath.personEmail")
                requireStringBound(transaction.type, MAX_SHORT_STRING_CHARS, "$transactionPath.type")
                requireStringBound(
                    transaction.paymentReference,
                    MAX_LONG_STRING_CHARS,
                    "$transactionPath.paymentReference"
                )
                requireStringBound(transaction.uploaderEmail, MAX_EMAIL_CHARS, "$transactionPath.uploaderEmail")
                transaction.manualNotes?.let {
                    requireStringBound(it, MAX_MANUAL_NOTES_CHARS, "$transactionPath.manualNotes")
                }
                transaction.receiptEvidenceId?.let {
                    requireStringBound(it, MAX_LOGICAL_ID_CHARS, "$transactionPath.receiptEvidenceId")
                }
            }
            event.receiptEvidence.forEachIndexed { evidenceIndex, evidence ->
                val evidencePath = "$path.receiptEvidence[$evidenceIndex]"
                requireStringBound(evidence.id, MAX_LOGICAL_ID_CHARS, "$evidencePath.id")
                requireStringBound(evidence.sha256, SHA256_HEX_CHARS, "$evidencePath.sha256")
            }
        }
    }

    private class BoundedOutputStream(
        private val delegate: OutputStream,
        private val maxBytes: Int
    ) : OutputStream() {
        private var count = 0

        override fun write(value: Int) {
            requireCapacity(1)
            delegate.write(value)
            count++
        }

        override fun write(bytes: ByteArray, offset: Int, length: Int) {
            requireCapacity(length)
            delegate.write(bytes, offset, length)
            count += length
        }

        private fun requireCapacity(additionalBytes: Int) {
            if (additionalBytes > maxBytes - count) {
                throw ManifestByteLimitExceededException()
            }
        }

        override fun flush() = delegate.flush()
    }

    private class BoundedInputStream(
        private val delegate: InputStream,
        private val maxBytes: Int
    ) : InputStream() {
        private var count = 0

        override fun read(): Int {
            if (count >= maxBytes) {
                if (delegate.read() == -1) return -1
                throw ManifestByteLimitExceededException()
            }
            val value = delegate.read()
            if (value >= 0) count++
            return value
        }

        override fun read(buffer: ByteArray, offset: Int, length: Int): Int {
            if (length == 0) return 0
            if (count >= maxBytes) return read().let { if (it < 0) -1 else 1 }
            val allowed = minOf(length, maxBytes - count)
            val read = delegate.read(buffer, offset, allowed)
            if (read > 0) count += read
            return read
        }
    }

    private class NonClosingOutputStream(private val delegate: OutputStream) : OutputStream() {
        override fun write(value: Int) = delegate.write(value)
        override fun write(bytes: ByteArray, offset: Int, length: Int) = delegate.write(bytes, offset, length)
        override fun flush() = delegate.flush()
        override fun close() = flush()
    }

    private class ManifestByteLimitExceededException : RuntimeException()

    private class DecodeBudget(
        var events: Int = 0,
        var members: Int = 0,
        var transactions: Int = 0,
        var receiptEvidence: Int = 0
    )

    private class FieldTracker(private val path: String) {
        private val seen = mutableSetOf<String>()

        fun next(reader: JsonReader): String {
            val name = reader.nextName()
            if (!seen.add(name)) fail("Duplicate field '$path.$name'.")
            return name
        }

        fun require(required: Set<String>) {
            val missing = required - seen
            if (missing.isNotEmpty()) fail("Missing field '$path.${missing.sorted().first()}'.")
        }
    }

    private fun JsonWriter.writeNullableString(value: String?) {
        if (value == null) nullValue() else value(value)
    }

    private fun JsonWriter.writeNullableInt(value: Int?) {
        if (value == null) nullValue() else value(value.toLong())
    }

    private inline fun <T> JsonReader.readArray(maxCount: Int, path: String, readItem: () -> T): List<T> {
        if (maxCount < 0) fail("$path exceeds the supported record count.")
        val values = mutableListOf<T>()
        beginArray()
        while (hasNext()) {
            if (values.size >= maxCount) fail("$path exceeds the supported record count.")
            values += readItem()
        }
        endArray()
        return values
    }

    private fun JsonReader.readString(path: String, maxChars: Int): String {
        if (peek() != JsonToken.STRING) fail("$path must be a string.")
        return nextString().also { requireStringBound(it, maxChars, path) }
    }

    private fun JsonReader.readNullableString(path: String, maxChars: Int): String? {
        if (peek() == JsonToken.NULL) {
            nextNull()
            return null
        }
        return readString(path, maxChars)
    }

    private fun JsonReader.readInt(path: String): Int {
        val value = readLong(path)
        if (value !in Int.MIN_VALUE..Int.MAX_VALUE) fail("$path is outside the supported integer range.")
        return value.toInt()
    }

    private fun JsonReader.readNullableInt(path: String): Int? {
        if (peek() == JsonToken.NULL) {
            nextNull()
            return null
        }
        return readInt(path)
    }

    private fun JsonReader.readLong(path: String): Long {
        if (peek() != JsonToken.NUMBER) fail("$path must be an integer.")
        val lexeme = nextString()
        if (!INTEGER_PATTERN.matches(lexeme)) fail("$path must be a plain decimal integer.")
        return lexeme.toLongOrNull()
            ?: fail("$path is outside the supported 64-bit integer range.")
    }

    private fun JsonReader.readFiniteDouble(path: String): Double {
        if (peek() != JsonToken.NUMBER) fail("$path must be a number.")
        val value = nextDouble()
        if (!value.isFinite()) fail("$path must be finite.")
        return value
    }

    private fun JsonReader.readBoolean(path: String): Boolean {
        if (peek() != JsonToken.BOOLEAN) fail("$path must be a boolean.")
        return nextBoolean()
    }

    private fun requireStringBound(value: String, maxChars: Int, path: String) {
        if (value.length > maxChars) fail("$path exceeds the supported character length.")
    }

    private fun fail(message: String): Nothing = throw InvalidLedgerBackupManifestException(message)

    private val PAYLOAD_FIELDS = setOf(
        "payloadVersion",
        "sourceSchemaVersion",
        "backupId",
        "createdAtEpochMillis",
        "sourcePreferences",
        "events"
    )
    private val PREFERENCE_FIELDS = setOf("userEmail", "themeMode")
    private val EVENT_FIELDS = setOf(
        "sourceId",
        "eventKey",
        "title",
        "duration",
        "createdDate",
        "isPrivate",
        "customFieldsJson",
        "members",
        "transactions",
        "receiptEvidence"
    )
    private val MEMBER_FIELDS = setOf("sourceId", "name", "normalizedName", "phone", "email", "role", "createdAt")
    private val TRANSACTION_FIELDS = setOf(
        "sourceId",
        "sourceMemberId",
        "personName",
        "personPhone",
        "personEmail",
        "amount",
        "type",
        "date",
        "paymentReference",
        "isVerified",
        "manualNotes",
        "uploaderEmail",
        "receiptEvidenceId"
    )
    private val EVIDENCE_FIELDS = setOf("id", "sourceTransactionId", "sha256", "byteCount")
    private val INTEGER_PATTERN = Regex("-?(?:0|[1-9][0-9]*)")

    private const val MAX_LOGICAL_ID_CHARS = 64
    private const val SHA256_HEX_CHARS = 64
    private const val MAX_SHORT_STRING_CHARS = 64
    private const val MAX_CONTACT_STRING_CHARS = 1_024
    private const val MAX_EMAIL_CHARS = 1_024
    private const val MAX_LONG_STRING_CHARS = 4 * 1_024
    private const val MAX_CUSTOM_FIELDS_CHARS = 64 * 1_024
    private const val MAX_MANUAL_NOTES_CHARS = 256 * 1_024
    private const val MAX_REPORTED_VALIDATION_ERRORS = 20
}