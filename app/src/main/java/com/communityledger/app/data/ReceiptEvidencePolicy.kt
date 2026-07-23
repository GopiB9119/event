package com.communityledger.app.data

import android.util.JsonReader
import android.util.JsonToken
import java.io.StringReader

sealed interface ReceiptEvidenceNotes {
    data object Manual : ReceiptEvidenceNotes
    data class Persisted(val sourcePath: String) : ReceiptEvidenceNotes
    data class Invalid(val reason: String) : ReceiptEvidenceNotes
}

fun String?.classifyReceiptEvidenceNotes(): ReceiptEvidenceNotes {
    val rawValue = this ?: return ReceiptEvidenceNotes.Manual
    if (rawValue.length > MAX_RECEIPT_NOTES_CHARS) {
        return ReceiptEvidenceNotes.Invalid("Receipt notes exceed the supported size.")
    }
    val value = rawValue.trim()
    if (!value.startsWith("{") && !value.startsWith("[")) {
        return ReceiptEvidenceNotes.Manual
    }

    val scan = ReceiptPathScan()
    return try {
        JsonReader(StringReader(value)).use { reader ->
            reader.isLenient = false
            when (reader.peek()) {
                JsonToken.BEGIN_OBJECT -> scanObject(reader, containerDepth = 1, isRoot = true, scan)
                JsonToken.BEGIN_ARRAY -> scanArray(reader, containerDepth = 1, scan)
                else -> return ReceiptEvidenceNotes.Invalid("Receipt notes must contain one JSON value.")
            }
            if (reader.peek() != JsonToken.END_DOCUMENT) {
                return ReceiptEvidenceNotes.Invalid("Receipt notes contain trailing JSON data.")
            }
        }

        when {
            scan.topLevelOccurrences == 0 && scan.nestedOccurrences == 0 -> ReceiptEvidenceNotes.Manual
            scan.topLevelOccurrences != 1 -> ReceiptEvidenceNotes.Invalid(
                "Receipt notes must contain exactly one top-level receiptFilePath."
            )
            scan.invalidTopLevelValue -> ReceiptEvidenceNotes.Invalid(
                "Top-level receiptFilePath must be a string."
            )
            scan.nestedOccurrences > 0 -> ReceiptEvidenceNotes.Invalid(
                "Receipt notes contain ambiguous nested receiptFilePath values."
            )
            scan.topLevelPath.isNullOrBlank() -> ReceiptEvidenceNotes.Invalid(
                "Top-level receiptFilePath must not be blank."
            )
            else -> ReceiptEvidenceNotes.Persisted(requireNotNull(scan.topLevelPath))
        }
    } catch (error: Exception) {
        ReceiptEvidenceNotes.Invalid("Receipt notes contain malformed or unsupported JSON.")
    }
}

fun String?.hasPersistedReceiptEvidence(): Boolean {
    return classifyReceiptEvidenceNotes() is ReceiptEvidenceNotes.Persisted
}

private fun scanObject(
    reader: JsonReader,
    containerDepth: Int,
    isRoot: Boolean,
    scan: ReceiptPathScan
) {
    requireSupportedDepth(containerDepth)
    reader.beginObject()
    while (reader.hasNext()) {
        val name = reader.nextName()
        if (name == RECEIPT_FILE_PATH_NAME) {
            if (isRoot) {
                scan.topLevelOccurrences++
                if (reader.peek() == JsonToken.STRING) {
                    scan.topLevelPath = reader.nextString()
                } else {
                    scan.invalidTopLevelValue = true
                    scanValue(reader, containerDepth, scan)
                }
            } else {
                scan.nestedOccurrences++
                scanValue(reader, containerDepth, scan)
            }
        } else {
            scanValue(reader, containerDepth, scan)
        }
    }
    reader.endObject()
}

private fun scanArray(reader: JsonReader, containerDepth: Int, scan: ReceiptPathScan) {
    requireSupportedDepth(containerDepth)
    reader.beginArray()
    while (reader.hasNext()) {
        scanValue(reader, containerDepth, scan)
    }
    reader.endArray()
}

private fun scanValue(reader: JsonReader, containerDepth: Int, scan: ReceiptPathScan) {
    when (reader.peek()) {
        JsonToken.BEGIN_OBJECT -> scanObject(reader, containerDepth + 1, isRoot = false, scan)
        JsonToken.BEGIN_ARRAY -> scanArray(reader, containerDepth + 1, scan)
        else -> reader.skipValue()
    }
}

private fun requireSupportedDepth(containerDepth: Int) {
    require(containerDepth <= MAX_RECEIPT_JSON_DEPTH) {
        "Receipt notes exceed the supported JSON depth."
    }
}

private data class ReceiptPathScan(
    var topLevelOccurrences: Int = 0,
    var nestedOccurrences: Int = 0,
    var topLevelPath: String? = null,
    var invalidTopLevelValue: Boolean = false
)

private const val RECEIPT_FILE_PATH_NAME = "receiptFilePath"
internal const val MAX_RECEIPT_NOTES_CHARS = 256 * 1024
internal const val MAX_RECEIPT_JSON_DEPTH = 32