package com.example.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertWidthIsAtLeast
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import com.example.data.EventEntity
import com.example.ui.theme.MyApplicationTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class EventCardItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun cardAndDeleteActionsRemainIndependentAndAccessible() {
        var opened = false
        var deleted = false
        val event = EventEntity(
            id = 7,
            title = "Community Festival Ledger",
            duration = "3 days",
            createdDate = 1_720_000_000_000,
            isPrivate = true
        )

        composeTestRule.setContent {
            MyApplicationTheme {
                EventCardItem(
                    event = event,
                    onClick = { opened = true },
                    onDelete = { deleted = true }
                )
            }
        }

        composeTestRule.onNodeWithTag("event_card_7").performClick()
        assertTrue(opened)

        composeTestRule.onNodeWithTag("delete_event_7")
            .assertWidthIsAtLeast(48.dp)
            .performClick()
        assertTrue(deleted)
    }

    @Test
    fun cardsDescribeVisibilityAsMarkersInsteadOfAccessControl() {
        composeTestRule.setContent {
            MyApplicationTheme {
                Column {
                    EventCardItem(
                        event = EventEntity(id = 1, title = "Private", isPrivate = true),
                        onClick = {},
                        onDelete = {}
                    )
                    EventCardItem(
                        event = EventEntity(id = 2, title = "Public", isPrivate = false),
                        onClick = {},
                        onDelete = {}
                    )
                }
            }
        }

        composeTestRule.onNodeWithText("Private marker", useUnmergedTree = true).assertExists()
        composeTestRule.onNodeWithText("Public marker", useUnmergedTree = true).assertExists()
        composeTestRule.onNodeWithText("Invite Only").assertDoesNotExist()
    }

    @Test
    fun eventCardsScreenshot() {
        composeTestRule.setContent {
            MyApplicationTheme(darkTheme = false) {
                Surface(modifier = androidx.compose.ui.Modifier.fillMaxSize()) {
                    Column(
                        modifier = androidx.compose.ui.Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        EventCardItem(
                            event = EventEntity(
                                id = 1,
                                title = "Community Festival Ledger With A Longer Name",
                                duration = "3 days",
                                createdDate = 1_720_000_000_000,
                                isPrivate = true
                            ),
                            onClick = {},
                            onDelete = {}
                        )
                        EventCardItem(
                            event = EventEntity(
                                id = 2,
                                title = "Neighborhood Gathering",
                                createdDate = 1_720_000_000_000,
                                isPrivate = false
                            ),
                            onClick = {},
                            onDelete = {}
                        )
                    }
                }
            }
        }

        composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/event-cards.png")
    }
}