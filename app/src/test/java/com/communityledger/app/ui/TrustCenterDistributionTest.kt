package com.communityledger.app.ui

import android.app.Application
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ApplicationProvider
import com.communityledger.app.BuildConfig
import com.communityledger.app.ui.theme.MyApplicationTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class TrustCenterDistributionTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun updateControlsMatchDistributionChannel() {
        val application = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = EventViewModel(application)

        composeRule.setContent {
            MyApplicationTheme {
                TrustCenterScreen(viewModel)
            }
        }

        if (BuildConfig.DIRECT_UPDATE_ENABLED) {
            composeRule.onNodeWithText("Check for Updates").assertExists()
            composeRule.onNodeWithText("Updates for this build are delivered by Google Play.", substring = true)
                .assertDoesNotExist()
        } else {
            composeRule.onNodeWithText("Check for Updates").assertDoesNotExist()
            composeRule.onNodeWithText("Updates for this build are delivered by Google Play.", substring = true)
                .assertExists()
        }
    }
}