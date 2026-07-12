package com.example.ui

import android.app.Application
import android.content.Intent
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.data.EventEntity
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class LedgerSafetyInstrumentedTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun moneyOutUsesOcrCounterpartyWithoutUploaderIdentity() {
        val (viewModel, event) = launchEventDetails()
        val sharedReceiptText = """
            PhonePe
            Paid to
            SAMPLE MERCHANT
            Paid ₹5,000.00
            UPI Ref No: 310725987654
            25 Jun 2026
        """.trimIndent()

        composeRule.runOnIdle {
            viewModel.handleSharedReceiptIntent(
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, sharedReceiptText)
                }
            )
        }

        composeRule.onNodeWithTag("receipt_money_in")
            .performScrollTo()
            .assertIsSelected()
        composeRule.onNodeWithTag("receipt_ledger_person_input")
            .assertTextContains("Organizer")

        composeRule.onNodeWithTag("receipt_money_out")
            .performScrollTo()
            .performClick()
            .assertIsSelected()
        composeRule.onNodeWithTag("receipt_ledger_person_input")
            .assertTextContains("SAMPLE MERCHANT")
        assertTrue(composeRule.onAllNodesWithText("Extracted JSON").fetchSemanticsNodes().isEmpty())
        assertTrue(composeRule.onAllNodesWithText("Confidence:", substring = true).fetchSemanticsNodes().isEmpty())
        composeRule.onNodeWithText("Save to Ledger")
            .performClick()

        composeRule.waitUntil(timeoutMillis = 5_000) {
            viewModel.selectedEventTransactions.value.size == 1
        }
        val savedTransaction = viewModel.selectedEventTransactions.value.single()
        val receiptJson = JSONObject(savedTransaction.notes.orEmpty())

        assertEquals(event.id, savedTransaction.eventId)
        assertEquals("Expense", savedTransaction.type)
        assertEquals("SAMPLE MERCHANT", savedTransaction.personName)
        assertEquals("", savedTransaction.personEmail)
        assertEquals("organizer@example.com", savedTransaction.uploaderEmail)
        assertEquals("SAMPLE MERCHANT", receiptJson.getString("counterpartyName"))
        assertEquals("User-entered during review", receiptJson.getString("ledgerPersonSource"))
        val receiptFile = File(receiptJson.getString("receiptFilePath"))
        assertTrue(receiptFile.isFile)
        assertEquals(receiptFile.absolutePath, JSONObject(receiptFile.readText()).getString("receiptFilePath"))
    }

    @Test
    fun labelledAmountCanSaveWhenOptionalDetailsAreMissing() {
        val (viewModel, event) = launchEventDetails()
        val sharedReceiptText = """
            Amount INR 54,000
            Received from SAMPLE MEMBER
            11 April 2026
        """.trimIndent()

        composeRule.runOnIdle {
            viewModel.handleSharedReceiptIntent(
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, sharedReceiptText)
                }
            )
        }

        composeRule.onNodeWithTag("receipt_amount_input")
            .assertTextContains("54000")
        assertTrue(composeRule.onAllNodesWithText("Extracted JSON").fetchSemanticsNodes().isEmpty())
        assertTrue(composeRule.onAllNodesWithText("Confidence:", substring = true).fetchSemanticsNodes().isEmpty())
        assertTrue(
            composeRule.onAllNodesWithText("Optional details do not block", substring = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        )
        composeRule.onNodeWithTag("verify_receipt_confirm_button")
            .assertIsEnabled()
            .performClick()

        composeRule.waitUntil(timeoutMillis = 5_000) {
            viewModel.selectedEventTransactions.value.size == 1
        }
        val savedTransaction = viewModel.selectedEventTransactions.value.single()
        assertEquals(event.id, savedTransaction.eventId)
        assertEquals(54000.0, savedTransaction.amount, 0.0)
    }

    @Test
    fun unlabelledAmountRequiresExplicitConfirmation() {
        val (viewModel, _) = launchEventDetails()
        val sharedReceiptText = """
            Google Pay
            UPI Ref No: 310725987654
            Paid to SAMPLE MERCHANT
            sample.merchant@okaxis
            11 April 2026
            Completed
            54000
        """.trimIndent()

        composeRule.runOnIdle {
            viewModel.handleSharedReceiptIntent(
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, sharedReceiptText)
                }
            )
        }

        composeRule.onNodeWithTag("verify_receipt_confirm_button")
            .assertIsNotEnabled()
        composeRule.onNodeWithTag("receipt_amount_confirmation")
            .performScrollTo()
            .performClick()
        composeRule.onNodeWithTag("verify_receipt_confirm_button")
            .assertIsEnabled()
            .performClick()

        composeRule.waitUntil(timeoutMillis = 5_000) {
            viewModel.selectedEventTransactions.value.size == 1
        }
        assertEquals(54000.0, viewModel.selectedEventTransactions.value.single().amount, 0.0)
    }

    @Test
    fun changedAmountRequiresExplicitConfirmation() {
        val (viewModel, _) = launchEventDetails()
        val sharedReceiptText = """
            Google Pay
            Paid ₹65,653
            UPI Ref No: 310725987654
            Paid to SAMPLE MERCHANT
            11 April 2026
        """.trimIndent()

        composeRule.runOnIdle {
            viewModel.handleSharedReceiptIntent(
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, sharedReceiptText)
                }
            )
        }

        composeRule.onNodeWithTag("receipt_amount_input")
            .performTextReplacement("750000")
        composeRule.onNodeWithTag("verify_receipt_confirm_button").assertIsNotEnabled()
        composeRule.onNodeWithTag("receipt_amount_confirmation")
            .performScrollTo()
            .performClick()
        composeRule.onNodeWithTag("verify_receipt_confirm_button")
            .assertIsEnabled()
            .performClick()

        composeRule.waitUntil(timeoutMillis = 5_000) {
            viewModel.selectedEventTransactions.value.size == 1
        }
        assertEquals(750000.0, viewModel.selectedEventTransactions.value.single().amount, 0.0)
    }

    @Test
    fun deleteRequiresAcknowledgementAndCancelPreservesEntry() {
        val (viewModel, event) = launchEventDetails()
        viewModel.addTransaction(
            eventId = event.id,
            personName = "Synthetic Member",
            personPhone = "",
            personEmail = "",
            amount = 250.0,
            type = "Donated",
            uploaderEmail = "organizer@example.com"
        )
        composeRule.waitUntil(timeoutMillis = 5_000) {
            viewModel.selectedEventTransactions.value.size == 1
        }
        val transactionId = viewModel.selectedEventTransactions.value.single().id

        composeRule.onNodeWithTag("delete_transaction_button_$transactionId")
            .performScrollTo()
            .performClick()
        composeRule.onNodeWithTag("delete_transaction_confirm").assertIsNotEnabled()
        composeRule.onNodeWithText("Keep entry").performClick()
        composeRule.waitForIdle()
        assertEquals(1, viewModel.selectedEventTransactions.value.size)

        composeRule.onNodeWithTag("delete_transaction_button_$transactionId")
            .performScrollTo()
            .performClick()
        composeRule.onNodeWithTag("delete_transaction_acknowledgement").performClick()
        composeRule.onNodeWithTag("delete_transaction_confirm")
            .assertIsEnabled()
            .performClick()

        composeRule.waitUntil(timeoutMillis = 5_000) {
            viewModel.selectedEventTransactions.value.isEmpty()
        }
        assertTrue(viewModel.selectedEventTransactions.value.isEmpty())
    }

    private fun launchEventDetails(): Pair<EventViewModel, EventEntity> {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val viewModel = EventViewModel(context.applicationContext as Application)
        assertTrue(viewModel.setMyUserEmail("organizer@example.com"))

        val eventTitle = "Ledger safety ${System.nanoTime()}"
        viewModel.createEvent(
            title = eventTitle,
            duration = null,
            isPrivate = true,
            customFields = emptyMap()
        )
        val event = runBlocking {
            withTimeout(5_000) {
                viewModel.events.first { events -> events.any { it.title == eventTitle } }
                    .single { it.title == eventTitle }
            }
        }

        viewModel.selectEvent(event.id)
        composeRule.setContent {
            MyApplicationTheme {
                EventDetailsScreen(eventId = event.id, viewModel = viewModel)
            }
        }
        composeRule.waitUntil(timeoutMillis = 5_000) {
            viewModel.selectedEvent.value?.id == event.id
        }

        return viewModel to event
    }
}