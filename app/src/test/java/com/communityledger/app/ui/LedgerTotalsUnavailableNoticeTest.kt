package com.communityledger.app.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class LedgerTotalsUnavailableNoticeTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun invalidLedgerNoticeUsesNeutralBoundedMessage() {
        composeRule.setContent {
            MaterialTheme {
                LedgerTotalsUnavailableNotice()
            }
        }

        composeRule.onNodeWithTag("ledger_totals_unavailable").assertExists()
        composeRule.onNodeWithText(INVALID_LEDGER_TOTALS_MESSAGE).assertExists()
    }
}