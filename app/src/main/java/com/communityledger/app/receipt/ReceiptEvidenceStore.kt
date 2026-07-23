package com.communityledger.app.receipt

import com.communityledger.app.data.ReceiptEvidenceNotes
import com.communityledger.app.data.ReceiptEvidenceReference
import com.communityledger.app.data.classifyReceiptEvidenceNotes
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.Locale
import java.util.UUID
import org.json.JSONObject

data class ReceiptEvidenceReconciliationResult(
    val scannedFileCount: Int,
    val deletedFileCount: Int,
    val truncated: Boolean
)

class ReceiptEvidenceStore(private val appFilesDirectory: File) {
    private val receiptsRoot: File
        get() = File(appFilesDirectory, RECEIPTS_DIRECTORY_NAME)

    fun writeReceipt(
        eventId: Int,
        personName: String,
        uploaderEmail: String,
        receiptJsonText: String
    ): String? {
        if (eventId <= 0 || uploaderEmail.isBlank()) return null

        return try {
            val root = receiptsRoot.canonicalFile
            if ((!root.exists() && !root.mkdirs()) || !root.isDirectory) return null

            val safePerson = safeFileSegment(personName.ifBlank { uploaderEmail.substringBefore("@") })
            val safeUploader = safeFileSegment(uploaderEmail)
            val directory = File(root, "event_$eventId/person_$safePerson/uploader_$safeUploader").canonicalFile
            if (!isStrictlyBelowRoot(directory, root)) return null
            if ((!directory.exists() && !directory.mkdirs()) || !directory.isDirectory) return null

            val receiptJson = JSONObject(receiptJsonText).apply {
                put("eventId", eventId)
                put("storedAt", System.currentTimeMillis())
            }
            val reference = receiptJson.optString("upiReferenceOrTransactionId")
                .takeIf { it.isNotBlank() && it != "null" }
                ?: "no_reference"
            val finalFile = File(
                directory,
                "receipt_${safeFileSegment(reference)}_${System.currentTimeMillis()}_${UUID.randomUUID()}.json"
            ).canonicalFile
            if (!isStrictlyBelowRoot(finalFile, root) || finalFile.exists()) return null

            receiptJson.put(RECEIPT_FILE_PATH_NAME, finalFile.absolutePath)
            val temporaryFile = File.createTempFile(".receipt_", ".tmp", directory).canonicalFile
            if (!isStrictlyBelowRoot(temporaryFile, root)) {
                temporaryFile.delete()
                return null
            }
            try {
                temporaryFile.outputStream().bufferedWriter(StandardCharsets.UTF_8).use { writer ->
                    writer.write(receiptJson.toString(2))
                }
                if (!temporaryFile.renameTo(finalFile)) return null
            } finally {
                if (temporaryFile.exists()) temporaryFile.delete()
            }
            finalFile.absolutePath
        } catch (error: Exception) {
            null
        }
    }

    fun deleteReferencedEvidence(eventId: Int, notes: String?): Boolean {
        val sourcePath = (notes.classifyReceiptEvidenceNotes() as? ReceiptEvidenceNotes.Persisted)
            ?.sourcePath
            ?: return true
        val file = resolveConfinedPath(eventId, sourcePath) ?: return false
        return !file.exists() || (file.isFile && file.delete())
    }

    fun referencedEvidenceFile(eventId: Int, notes: String?): File? {
        val sourcePath = (notes.classifyReceiptEvidenceNotes() as? ReceiptEvidenceNotes.Persisted)
            ?.sourcePath
            ?: return null
        return resolveConfinedPath(eventId, sourcePath)?.takeIf { it.isFile }
    }

    fun reconcile(
        references: List<ReceiptEvidenceReference>,
        maxScannedFiles: Int = DEFAULT_MAX_RECONCILIATION_FILES,
        staleBeforeEpochMillis: Long = System.currentTimeMillis() - DEFAULT_ORPHAN_GRACE_MILLIS
    ): ReceiptEvidenceReconciliationResult {
        require(maxScannedFiles > 0)
        val root = try {
            receiptsRoot.canonicalFile
        } catch (error: Exception) {
            return ReceiptEvidenceReconciliationResult(0, 0, truncated = false)
        }
        if (!root.isDirectory) return ReceiptEvidenceReconciliationResult(0, 0, truncated = false)

        val referencedPaths = references.mapNotNull { reference ->
            val sourcePath = (reference.notes.classifyReceiptEvidenceNotes() as? ReceiptEvidenceNotes.Persisted)
                ?.sourcePath
                ?: return@mapNotNull null
            resolveConfinedPath(reference.eventId, sourcePath)?.absolutePath
        }.toHashSet()

        var scanned = 0
        var deleted = 0
        var truncated = false

        fun visit(directory: File, depth: Int) {
            if (truncated || depth > MAX_RECONCILIATION_DEPTH) return
            val canonicalDirectory = try {
                directory.canonicalFile
            } catch (error: Exception) {
                return
            }
            if (canonicalDirectory != root && !isStrictlyBelowRoot(canonicalDirectory, root)) return

            canonicalDirectory.listFiles()?.sortedBy { it.name }?.forEach { child ->
                if (truncated) return@forEach
                val canonicalChild = try {
                    child.canonicalFile
                } catch (error: Exception) {
                    return@forEach
                }
                if (!isStrictlyBelowRoot(canonicalChild, root)) return@forEach
                if (canonicalChild.isDirectory) {
                    visit(canonicalChild, depth + 1)
                    if (canonicalChild.list()?.isEmpty() == true) canonicalChild.delete()
                } else if (canonicalChild.isFile) {
                    if (scanned >= maxScannedFiles) {
                        truncated = true
                        return@forEach
                    }
                    scanned++
                    if (
                        canonicalChild.absolutePath !in referencedPaths &&
                        canonicalChild.lastModified() <= staleBeforeEpochMillis &&
                        canonicalChild.delete()
                    ) {
                        deleted++
                    }
                }
            }
        }

        visit(root, 0)
        return ReceiptEvidenceReconciliationResult(scanned, deleted, truncated)
    }

    private fun resolveConfinedPath(eventId: Int, path: String): File? {
        if (eventId <= 0) return null
        return try {
            val root = receiptsRoot.canonicalFile
            val eventRoot = File(root, "event_$eventId").canonicalFile
            if (!isStrictlyBelowRoot(eventRoot, root)) return null
            val candidate = File(path).canonicalFile
            candidate.takeIf { isStrictlyBelowRoot(it, eventRoot) }
        } catch (error: Exception) {
            null
        }
    }

    private fun isStrictlyBelowRoot(candidate: File, root: File): Boolean {
        return candidate.path.startsWith(root.path + File.separator)
    }

    private fun safeFileSegment(value: String): String {
        return value.trim()
            .lowercase(Locale.ROOT)
            .replace(Regex("[^a-z0-9._-]+"), "_")
            .trim('_')
            .ifBlank { "unknown" }
            .take(80)
    }

    private companion object {
        const val RECEIPTS_DIRECTORY_NAME = "receipts"
        const val RECEIPT_FILE_PATH_NAME = "receiptFilePath"
        const val DEFAULT_MAX_RECONCILIATION_FILES = 10_000
        const val MAX_RECONCILIATION_DEPTH = 8
        const val DEFAULT_ORPHAN_GRACE_MILLIS = 60 * 60 * 1000L
    }
}