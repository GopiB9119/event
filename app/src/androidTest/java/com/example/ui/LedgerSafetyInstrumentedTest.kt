package com.example.ui

import android.app.Application
import android.content.Intent
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
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