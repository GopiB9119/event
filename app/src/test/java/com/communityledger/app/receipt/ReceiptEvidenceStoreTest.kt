package com.communityledger.app.receipt

import com.communityledger.app.data.ReceiptEvidenceReference
import java.io.File
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ReceiptEvidenceStoreTest {
    @get:Rule
    val temporaryFolder = TemporaryFolder()

    @Test
    fun writesReceiptAtomicallyBelowTheReceiptRoot() {
        val store = ReceiptEvidenceStore(temporaryFolder.root)

        val path = store.writeReceipt(
            eventId = 7,
            personName = "Member",
            uploaderEmail = "organizer@example.com",
            receiptJsonText = """{"amount":500,"upiReferenceOrTransactionId":"REF-001"}"""
        )

        assertNotNull(path)
        val file = File(requireNotNull(path)).canonicalFile
        val root = File(temporaryFolder.root, "receipts").canonicalFile
        assertTrue(file.path.startsWith(root.path + File.separator))
        assertTrue(file.isFile)
        assertEquals(file.absolutePath, JSONObject(file.readText()).getString("receiptFilePath"))
        assertTrue(file.parentFile?.listFiles().orEmpty().none { it.name.endsWith(".tmp") })
    }

    @Test
    fun rejectsPathsOutsideTheReceiptRoot() {
        val store = ReceiptEvidenceStore(temporaryFolder.root)
        val outside = temporaryFolder.newFile("outside.json")
        val notes = JSONObject().put("receiptFilePath", outside.absolutePath).toString()

        assertNull(store.referencedEvidenceFile(7, notes))
        assertFalse(store.deleteReferencedEvidence(7, notes))
        assertTrue(outside.isFile)
    }

    @Test
    fun deleteReferencedEvidenceRemovesOnlyTheConfinedFile() {
        val store = ReceiptEvidenceStore(temporaryFolder.root)
        val path = requireNotNull(
            store.writeReceipt(8, "Member", "organizer@example.com", """{"amount":500}""")
        )
        val notes = JSONObject().put("receiptFilePath", path).toString()

        assertTrue(store.deleteReferencedEvidence(8, notes))
        assertFalse(File(path).exists())
    }

    @Test
    fun reconciliationPreservesReferencedEvidenceAndDeletesOrphans() {
        val store = ReceiptEvidenceStore(temporaryFolder.root)
        val referencedPath = requireNotNull(
            store.writeReceipt(9, "Member", "organizer@example.com", """{"amount":500}""")
        )
        val orphanPath = requireNotNull(
            store.writeReceipt(9, "Member", "organizer@example.com", """{"amount":750}""")
        )
        val referencedNotes = JSONObject().put("receiptFilePath", referencedPath).toString()

        val result = store.reconcile(
            listOf(ReceiptEvidenceReference(9, referencedNotes)),
            staleBeforeEpochMillis = Long.MAX_VALUE
        )

        assertTrue(File(referencedPath).isFile)
        assertFalse(File(orphanPath).exists())
        assertEquals(2, result.scannedFileCount)
        assertEquals(1, result.deletedFileCount)
        assertFalse(result.truncated)
    }

    @Test
    fun eventCannotClaimAnotherEventsReceiptFile() {
        val store = ReceiptEvidenceStore(temporaryFolder.root)
        val path = requireNotNull(
            store.writeReceipt(12, "Member", "organizer@example.com", """{"amount":500}""")
        )
        val notes = JSONObject().put("receiptFilePath", path).toString()

        assertNull(store.referencedEvidenceFile(13, notes))
        assertFalse(store.deleteReferencedEvidence(13, notes))
        assertTrue(File(path).isFile)
    }

    @Test
    fun reconciliationStopsAtTheConfiguredFileBudget() {
        val store = ReceiptEvidenceStore(temporaryFolder.root)
        repeat(3) { index ->
            assertNotNull(
                store.writeReceipt(10, "Member", "organizer@example.com", """{"amount":${index + 1}}""")
            )
        }

        val result = store.reconcile(
            emptyList(),
            maxScannedFiles = 1,
            staleBeforeEpochMillis = Long.MAX_VALUE
        )

        assertEquals(1, result.scannedFileCount)
        assertEquals(1, result.deletedFileCount)
        assertTrue(result.truncated)
    }

    @Test
    fun reconciliationDoesNotDeleteFreshUncommittedEvidence() {
        val store = ReceiptEvidenceStore(temporaryFolder.root)
        val freshPath = requireNotNull(
            store.writeReceipt(11, "Member", "organizer@example.com", """{"amount":500}""")
        )

        val result = store.reconcile(
            emptyList(),
            staleBeforeEpochMillis = System.currentTimeMillis() - 60_000L
        )

        assertTrue(File(freshPath).isFile)
        assertEquals(0, result.deletedFileCount)
    }
}