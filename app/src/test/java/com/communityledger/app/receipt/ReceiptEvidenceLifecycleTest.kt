package com.communityledger.app.receipt

import android.app.Application
import android.os.Looper
import androidx.test.core.app.ApplicationProvider
import com.communityledger.app.data.EventEntity
import com.communityledger.app.ui.EventViewModel
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ReceiptEvidenceLifecycleTest {
    private lateinit var application: Application
    private lateinit var receiptsRoot: File
    private lateinit var collectionScope: CoroutineScope

    @Before
    fun setUp() {
        application = ApplicationProvider.getApplicationContext()
        receiptsRoot = File(application.filesDir, "receipts")
        receiptsRoot.deleteRecursively()
        collectionScope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        collectionScope.cancel()
        receiptsRoot.deleteRecursively()
    }

    @Test
    fun transactionDeletionRemovesCommittedReceiptEvidence() = runBlocking {
        val viewModel = configuredViewModel()
        val event = createSelectedEvent(viewModel, "Transaction deletion")
        assertTrue(persistReceipt(viewModel, event.id, amount = 500.0, reference = "DELETE-REF-001"))
        val transaction = awaitTransactions(viewModel) { it.size == 1 }.single()
        val evidenceFile = evidenceFile(transaction.notes)

        viewModel.deleteTransaction(transaction.id)
        awaitTransactions(viewModel) { it.isEmpty() }

        assertFalse(evidenceFile.exists())
    }

    @Test
    fun eventDeletionRemovesAllCommittedReceiptEvidence() = runBlocking {
        val viewModel = configuredViewModel()
        val event = createSelectedEvent(viewModel, "Event deletion")
        assertTrue(persistReceipt(viewModel, event.id, amount = 600.0, reference = "EVENT-REF-001"))
        val evidenceFile = evidenceFile(awaitTransactions(viewModel) { it.size == 1 }.single().notes)

        viewModel.deleteEvent(event.id)
        awaitCondition { viewModel.events.value.none { it.id == event.id } }

        assertFalse(evidenceFile.exists())
    }

    @Test
    fun successfulReplacementDeletesOldEvidenceAndKeepsNewEvidence() = runBlocking {
        val viewModel = configuredViewModel()
        val event = createSelectedEvent(viewModel, "Receipt replacement")
        assertTrue(persistReceipt(viewModel, event.id, amount = 700.0, reference = "REPLACE-REF-001"))
        val original = awaitTransactions(viewModel) { it.size == 1 }.single()
        val originalFile = evidenceFile(original.notes)

        assertTrue(
            persistReceipt(
                viewModel = viewModel,
                eventId = event.id,
                amount = 800.0,
                reference = "REPLACE-REF-002",
                transactionId = original.id,
                existingMemberId = original.memberId
            )
        )
        val replacement = awaitTransactions(viewModel) {
            it.size == 1 && it.single().amount == 800.0
        }.single()
        val replacementFile = evidenceFile(replacement.notes)

        assertNotEquals(originalFile.absolutePath, replacementFile.absolutePath)
        assertFalse(originalFile.exists())
        assertTrue(replacementFile.isFile)
    }

    @Test
    fun overflowRejectionRollsBackMemberTransactionAndStagedEvidence() = runBlocking {
        val viewModel = configuredViewModel()
        val event = createSelectedEvent(viewModel, "Overflow rollback")
        viewModel.addTransaction(
            eventId = event.id,
            personName = "Existing entry",
            personPhone = "",
            personEmail = "",
            amount = Double.MAX_VALUE,
            type = "Donated",
            uploaderEmail = "organizer@example.com"
        )
        awaitTransactions(viewModel) { it.size == 1 }

        val saved = persistReceipt(
            viewModel = viewModel,
            eventId = event.id,
            amount = Double.MAX_VALUE,
            reference = "OVERFLOW-REF-001"
        )

        assertFalse(saved)
        assertEquals(1, awaitTransactions(viewModel) { it.size == 1 }.size)
        awaitCondition { viewModel.selectedEventMembers.value.isEmpty() }
        assertTrue(viewModel.selectedEventMembers.value.isEmpty())
        assertEquals(0, eventReceiptFiles(event.id).size)
    }

    private fun configuredViewModel(): EventViewModel {
        return EventViewModel(application).also { viewModel ->
            assertTrue(viewModel.setMyUserEmail("organizer@example.com"))
            collectionScope.launch { viewModel.events.collect() }
            collectionScope.launch { viewModel.selectedEvent.collect() }
            collectionScope.launch { viewModel.selectedEventTransactions.collect() }
            collectionScope.launch { viewModel.selectedEventMembers.collect() }
        }
    }

    private fun createSelectedEvent(viewModel: EventViewModel, title: String): EventEntity {
        viewModel.createEvent(title, duration = null, isPrivate = true, customFields = emptyMap())
        awaitCondition { viewModel.events.value.any { it.title == title } }
        val event = viewModel.events.value.single { it.title == title }
        viewModel.selectEvent(event.id)
        awaitCondition { viewModel.selectedEvent.value?.id == event.id }
        return event
    }

    private suspend fun persistReceipt(
        viewModel: EventViewModel,
        eventId: Int,
        amount: Double,
        reference: String,
        transactionId: Int? = null,
        existingMemberId: Int? = null
    ): Boolean {
        val receiptJson = JSONObject().apply {
            put("amount", amount)
            put("upiReferenceOrTransactionId", reference)
        }.toString()
        return viewModel.persistReceiptTransactionWithEvidence(
            txId = transactionId,
            eventId = eventId,
            personName = "Ledger person",
            personPhone = "",
            personEmail = "",
            amount = amount,
            type = "Donated",
            receiptJsonText = receiptJson,
            transactionId = reference,
            uploaderEmail = "organizer@example.com",
            existingMemberId = existingMemberId
        )
    }

    private fun awaitTransactions(
        viewModel: EventViewModel,
        predicate: (List<com.communityledger.app.data.TransactionEntity>) -> Boolean
    ): List<com.communityledger.app.data.TransactionEntity> {
        awaitCondition { predicate(viewModel.selectedEventTransactions.value) }
        return viewModel.selectedEventTransactions.value
    }

    private fun awaitCondition(timeoutMillis: Long = 10_000L, condition: () -> Boolean) {
        val deadline = System.nanoTime() + timeoutMillis * 1_000_000L
        while (!condition() && System.nanoTime() < deadline) {
            shadowOf(Looper.getMainLooper()).idle()
            Thread.sleep(25)
        }
        shadowOf(Looper.getMainLooper()).idle()
        assertTrue("Timed out waiting for receipt lifecycle state.", condition())
    }

    private fun evidenceFile(notes: String?): File {
        return File(JSONObject(requireNotNull(notes)).getString("receiptFilePath"))
    }

    private fun eventReceiptFiles(eventId: Int): List<File> {
        val eventDirectory = File(receiptsRoot, "event_$eventId")
        if (!eventDirectory.isDirectory) return emptyList()
        return eventDirectory.walkTopDown().filter(File::isFile).toList()
    }
}