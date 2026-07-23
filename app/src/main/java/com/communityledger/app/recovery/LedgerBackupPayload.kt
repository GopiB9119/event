package com.communityledger.app.recovery

import android.util.JsonReader
import android.util.JsonToken
import com.communityledger.app.data.ReceiptEvidenceNotes
import com.communityledger.app.data.classifyReceiptEvidenceNotes
import com.communityledger.app.data.isValidLedgerTransaction
import com.communityledger.app.data.normalizeLocalIdentity
import com.communityledger.app.data.normalizeMemberName
import java.io.StringReader
import java.util.Locale

const val LEDGER_BACKUP_PAYLOAD_VERSION = 1
const val LEDGER_BACKUP_MIN_SOURCE_SCHEMA_VERSION = 5
const val LEDGER_BACKUP_SOURCE_SCHEMA_VERSION = 6

data class LedgerBackupPayload(
    val payloadVersion: Int = LEDGER_BACKUP_PAYLOAD_VERSION,
    val sourceSchemaVersion: Int = LEDGER_BACKUP_SOURCE_SCHEMA_VERSION,
    val backupId: String,
    val createdAtEpochMillis: Long,
    val sourcePreferences: LedgerBackupSourcePreferences,
    val events: List<LedgerBackupEvent>
)

data class LedgerBackupSourcePreferences(
    val userEmail: String,
    val themeMode: String
)

data class LedgerBackupEvent(
    val sourceId: Int,
    val eventKey: String,
    val title: String,
    val duration: String?,
    val createdDate: Long,
    val isPrivate: Boolean,
    val customFieldsJson: String,
    val members: List<LedgerBackupMember>,
    val transactions: List<LedgerBackupTransaction>,
    val receiptEvidence: List<LedgerBackupReceiptEvidence>
)

data class LedgerBackupMember(
    val sourceId: Int,
    val name: String,
    val normalizedName: String,
    val phone: String,
    val email: String,
    val role: String,
    val createdAt: Long
)

data class LedgerBackupTransaction(
    val sourceId: Int,
    val sourceMemberId: Int?,
    val personName: String,
    val personPhone: String,
    val personEmail: String,
    val amount: Double,
    val type: String,
    val date: Long,
    val paymentReference: String,
    val isVerified: Boolean,
    val manualNotes: String?,
    val uploaderEmail: String,
    val receiptEvidenceId: String?
)

data class LedgerBackupReceiptEvidence(
    val id: String,
    val sourceTransactionId: Int,
    val sha256: String,
    val byteCount: Long
)

data class LedgerBackupValidationError(
    val path: String,
    val message: String
)

fun validateLedgerBackupPayload(payload: LedgerBackupPayload): List<LedgerBackupValidationError> {
    val errors = mutableListOf<LedgerBackupValidationError>()

    if (!payload.backupId.matches(logicalIdPattern)) {
        errors += LedgerBackupValidationError("backupId", "Backup ID must be 32-character lowercase hexadecimal.")
    }
    if (payload.payloadVersion != LEDGER_BACKUP_PAYLOAD_VERSION) {
        errors += LedgerBackupValidationError("payloadVersion", "Unsupported backup payload version.")
    }
    if (payload.sourceSchemaVersion !in
        LEDGER_BACKUP_MIN_SOURCE_SCHEMA_VERSION..LEDGER_BACKUP_SOURCE_SCHEMA_VERSION
    ) {
        errors += LedgerBackupValidationError("sourceSchemaVersion", "Unsupported source database schema version.")
    }
    if (payload.createdAtEpochMillis <= 0L) {
        errors += LedgerBackupValidationError("createdAtEpochMillis", "Backup creation time must be positive.")
    }
    if (
        payload.sourcePreferences.userEmail.isNotBlank() &&
        normalizeLocalIdentity(payload.sourcePreferences.userEmail) != payload.sourcePreferences.userEmail
    ) {
        errors += LedgerBackupValidationError(
            "sourcePreferences.userEmail",
            "Source user email must be empty or normalized and valid."
        )
    }
    if (payload.sourcePreferences.themeMode !in setOf("System", "Light", "Dark")) {
        errors += LedgerBackupValidationError("sourcePreferences.themeMode", "Source theme mode is not supported.")
    }
    if (payload.events.isEmpty()) {
        errors += LedgerBackupValidationError("events", "Backup payload must contain at least one event.")
    }

    val eventIds = mutableSetOf<Int>()
    val eventKeys = mutableSetOf<String>()
    val memberIds = mutableSetOf<Int>()
    val transactionIds = mutableSetOf<Int>()
    payload.events.forEachIndexed { eventIndex, event ->
        val eventPath = "events[$eventIndex]"
        if (event.sourceId <= 0 || !eventIds.add(event.sourceId)) {
            errors += LedgerBackupValidationError("$eventPath.sourceId", "Event source ID must be positive and unique.")
        }
        if (!event.eventKey.matches(logicalIdPattern) || !eventKeys.add(event.eventKey)) {
            errors += LedgerBackupValidationError(
                "$eventPath.eventKey",
                "Event key must be a unique 32-character lowercase hexadecimal value."
            )
        }
        if (event.title.isBlank()) {
            errors += LedgerBackupValidationError("$eventPath.title", "Event title must not be blank.")
        }
        if (event.createdDate <= 0L) {
            errors += LedgerBackupValidationError("$eventPath.createdDate", "Event creation time must be positive.")
        }
        validateCustomFieldsJson(event.customFieldsJson, "$eventPath.customFieldsJson", errors)

        validateEventChildren(event, eventPath, memberIds, transactionIds, errors)
    }

    return errors
}

private fun validateEventChildren(
    event: LedgerBackupEvent,
    eventPath: String,
    globalMemberIds: MutableSet<Int>,
    globalTransactionIds: MutableSet<Int>,
    errors: MutableList<LedgerBackupValidationError>
) {
    val memberIds = mutableSetOf<Int>()
    event.members.forEachIndexed { memberIndex, member ->
        val memberPath = "$eventPath.members[$memberIndex]"
        if (member.sourceId <= 0 || !memberIds.add(member.sourceId)) {
            errors += LedgerBackupValidationError(
                "$memberPath.sourceId",
                "Member source ID must be positive and unique within its event."
            )
        } else if (!globalMemberIds.add(member.sourceId)) {
            errors += LedgerBackupValidationError(
                "$memberPath.sourceId",
                "Member source ID must be unique across the backup payload."
            )
        }
        if (member.name.isBlank() || member.normalizedName.isBlank()) {
            errors += LedgerBackupValidationError(memberPath, "Member name and normalized name must not be blank.")
        } else if (normalizeMemberName(member.name) != member.normalizedName) {
            errors += LedgerBackupValidationError(
                "$memberPath.normalizedName",
                "Member normalized name does not match its canonical name."
            )
        }
        if (member.createdAt <= 0L) {
            errors += LedgerBackupValidationError("$memberPath.createdAt", "Member creation time must be positive.")
        }
        if (member.role !in supportedMemberRoles) {
            errors += LedgerBackupValidationError("$memberPath.role", "Member role is not supported.")
        }
    }

    val transactionIds = mutableSetOf<Int>()
    val referencedEvidenceIds = mutableSetOf<String>()
    val transactionIdByEvidenceId = mutableMapOf<String, Int>()
    val receiptPaymentReferences = mutableSetOf<String>()
    var totalCollected = 0.0
    var totalSpent = 0.0
    event.transactions.forEachIndexed { transactionIndex, transaction ->
        val transactionPath = "$eventPath.transactions[$transactionIndex]"
        if (transaction.sourceId <= 0 || !transactionIds.add(transaction.sourceId)) {
            errors += LedgerBackupValidationError(
                "$transactionPath.sourceId",
                "Transaction source ID must be positive and unique within its event."
            )
        } else if (!globalTransactionIds.add(transaction.sourceId)) {
            errors += LedgerBackupValidationError(
                "$transactionPath.sourceId",
                "Transaction source ID must be unique across the backup payload."
            )
        }
        if (transaction.sourceMemberId != null && transaction.sourceMemberId !in memberIds) {
            errors += LedgerBackupValidationError(
                "$transactionPath.sourceMemberId",
                "Transaction references a member outside its event."
            )
        }
        if (!isValidLedgerTransaction(event.sourceId, transaction.amount, transaction.type)) {
            errors += LedgerBackupValidationError(transactionPath, "Transaction amount or type is invalid.")
        } else {
            when (transaction.type) {
                "Donated", "Credit" -> totalCollected += transaction.amount
                "Debit", "Expense" -> totalSpent += transaction.amount
            }
        }
        if (transaction.date <= 0L) {
            errors += LedgerBackupValidationError("$transactionPath.date", "Transaction time must be positive.")
        }
        if (transaction.personName.isBlank()) {
            errors += LedgerBackupValidationError("$transactionPath.personName", "Transaction person name must not be blank.")
        }
        if (normalizeLocalIdentity(transaction.uploaderEmail) != transaction.uploaderEmail) {
            errors += LedgerBackupValidationError(
                "$transactionPath.uploaderEmail",
                "Transaction uploader email must be normalized and valid."
            )
        }
        if (
            transaction.receiptEvidenceId == null &&
            transaction.manualNotes.classifyReceiptEvidenceNotes() !is ReceiptEvidenceNotes.Manual
        ) {
            errors += LedgerBackupValidationError(
                "$transactionPath.manualNotes",
                "Portable manual notes must not contain a source-device receipt path."
            )
        }
        transaction.receiptEvidenceId?.let { evidenceId ->
            val normalizedReference = transaction.paymentReference.trim().lowercase(Locale.ROOT)
            if (normalizedReference.isNotBlank() && !receiptPaymentReferences.add(normalizedReference)) {
                errors += LedgerBackupValidationError(
                    "$transactionPath.paymentReference",
                    "Receipt payment reference must be unique within its event."
                )
            }
            if (transaction.manualNotes != null) {
                errors += LedgerBackupValidationError(
                    "$transactionPath.manualNotes",
                    "Receipt transactions must rebuild notes from portable evidence during restore."
                )
            }
            if (evidenceId.isBlank() || !referencedEvidenceIds.add(evidenceId)) {
                errors += LedgerBackupValidationError(
                    "$transactionPath.receiptEvidenceId",
                    "Receipt evidence ID must be non-blank and referenced by only one transaction."
                )
            } else {
                transactionIdByEvidenceId[evidenceId] = transaction.sourceId
            }
        }
    }

    val evidenceIds = mutableSetOf<String>()
    val evidenceHashes = mutableSetOf<String>()
    event.receiptEvidence.forEachIndexed { evidenceIndex, evidence ->
        val evidencePath = "$eventPath.receiptEvidence[$evidenceIndex]"
        if (!evidence.id.matches(logicalIdPattern) || !evidenceIds.add(evidence.id)) {
            errors += LedgerBackupValidationError(
                "$evidencePath.id",
                "Receipt evidence ID must be unique 32-character lowercase hexadecimal within its event."
            )
        }
        if (evidence.sourceTransactionId !in transactionIds) {
            errors += LedgerBackupValidationError(
                "$evidencePath.sourceTransactionId",
                "Receipt evidence references a transaction outside its event."
            )
        } else if (transactionIdByEvidenceId[evidence.id] != evidence.sourceTransactionId) {
            errors += LedgerBackupValidationError(
                "$evidencePath.sourceTransactionId",
                "Receipt evidence does not match the transaction that references its ID."
            )
        }
        if (!evidence.sha256.matches(Regex("^[0-9a-f]{64}$"))) {
            errors += LedgerBackupValidationError(
                "$evidencePath.sha256",
                "Receipt evidence hash must be lowercase SHA-256 hexadecimal."
            )
        } else if (!evidenceHashes.add(evidence.sha256)) {
            errors += LedgerBackupValidationError(
                "$evidencePath.sha256",
                "Receipt evidence hash must be unique within its event."
            )
        }
        if (evidence.byteCount <= 0L) {
            errors += LedgerBackupValidationError(
                "$evidencePath.byteCount",
                "Receipt evidence must contain at least one byte."
            )
        }
    }

    referencedEvidenceIds.filterNot(evidenceIds::contains).forEach { missingEvidenceId ->
        errors += LedgerBackupValidationError(
            eventPath,
            "Transaction references missing receipt evidence '$missingEvidenceId'."
        )
    }
    evidenceIds.filterNot(referencedEvidenceIds::contains).forEach { orphanedEvidenceId ->
        errors += LedgerBackupValidationError(
            eventPath,
            "Receipt evidence '$orphanedEvidenceId' is not linked to a transaction."
        )
    }
    if (!totalCollected.isFinite()) {
        errors += LedgerBackupValidationError("$eventPath.totalCollected", "Collected total must remain finite.")
    }
    if (!totalSpent.isFinite()) {
        errors += LedgerBackupValidationError("$eventPath.totalSpent", "Spent total must remain finite.")
    }
    if (!(totalCollected - totalSpent).isFinite()) {
        errors += LedgerBackupValidationError("$eventPath.availableBalance", "Available balance must remain finite.")
    }
}

private fun validateCustomFieldsJson(
    value: String,
    path: String,
    errors: MutableList<LedgerBackupValidationError>
) {
    if (value.length > MAX_CUSTOM_FIELDS_JSON_CHARS) {
        errors += LedgerBackupValidationError(path, "Event custom fields exceed the supported size.")
        return
    }
    if (value.isBlank()) return

    try {
        JsonReader(StringReader(value)).use { reader ->
            reader.isLenient = false
            if (reader.peek() != JsonToken.BEGIN_OBJECT) {
                errors += LedgerBackupValidationError(path, "Event custom fields must be one JSON object.")
                return
            }
            val names = mutableSetOf<String>()
            var fieldCount = 0
            reader.beginObject()
            while (reader.hasNext()) {
                val name = reader.nextName()
                fieldCount++
                if (fieldCount > MAX_CUSTOM_FIELD_COUNT) {
                    errors += LedgerBackupValidationError(path, "Event custom fields contain too many entries.")
                }
                if (name.isBlank() || name.length > MAX_CUSTOM_FIELD_NAME_CHARS) {
                    errors += LedgerBackupValidationError(path, "Event custom field names must be nonblank and bounded.")
                }
                if (!names.add(name)) {
                    errors += LedgerBackupValidationError(path, "Event custom field names must be unique.")
                }
                if (reader.peek() != JsonToken.STRING) {
                    errors += LedgerBackupValidationError(path, "Event custom field values must be strings.")
                    reader.skipValue()
                    continue
                }
                val fieldValue = reader.nextString()
                if (fieldValue.length > MAX_CUSTOM_FIELD_VALUE_CHARS) {
                    errors += LedgerBackupValidationError(path, "Event custom field values exceed the supported size.")
                }
                if (name == "creatorEmail" && normalizeLocalIdentity(fieldValue) != fieldValue) {
                    errors += LedgerBackupValidationError(path, "Event creator email must be normalized and valid.")
                }
            }
            reader.endObject()
            if (reader.peek() != JsonToken.END_DOCUMENT) {
                errors += LedgerBackupValidationError(path, "Event custom fields contain trailing JSON data.")
            }
        }
    } catch (error: Exception) {
        errors += LedgerBackupValidationError(path, "Event custom fields contain malformed JSON.")
    }
}

private val logicalIdPattern = Regex("^[0-9a-f]{32}$")
private val supportedMemberRoles = setOf("Donor", "Vendor")
private const val MAX_CUSTOM_FIELDS_JSON_CHARS = 64 * 1024
private const val MAX_CUSTOM_FIELD_COUNT = 64
private const val MAX_CUSTOM_FIELD_NAME_CHARS = 80
private const val MAX_CUSTOM_FIELD_VALUE_CHARS = 4 * 1024