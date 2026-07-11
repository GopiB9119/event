package com.example

import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Looper
import androidx.test.core.app.ApplicationProvider
import com.example.ui.EventViewModel
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    private fun awaitDeepLinkResult(viewModel: EventViewModel, timeoutMillis: Long = 30_000L) {
        val deadline = System.nanoTime() + timeoutMillis * 1_000_000L
        while (
            viewModel.deepLinkMessage.value == null &&
            viewModel.deepLinkError.value == null &&
            System.nanoTime() < deadline
        ) {
            shadowOf(Looper.getMainLooper()).idle()
            Thread.sleep(25)
        }
        shadowOf(Looper.getMainLooper()).idle()
    }

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Community Ledger", appName)
    }

    @Test
    fun `local beta acknowledgement persists across view models`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val preferences = app.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        preferences.edit().remove("local_beta_acknowledged").commit()

        try {
            val firstViewModel = EventViewModel(app)
            assertFalse(firstViewModel.localBetaAcknowledged.value)

            firstViewModel.acknowledgeLocalBeta()
            assertTrue(firstViewModel.localBetaAcknowledged.value)

            val recreatedViewModel = EventViewModel(app)
            assertTrue(recreatedViewModel.localBetaAcknowledged.value)
        } finally {
            preferences.edit().remove("local_beta_acknowledged").commit()
        }
    }

    @Test
    fun `receipt review interruption survives recreation until acknowledged`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val preferences = app.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        preferences.edit().remove("receipt_review_in_progress").commit()

        try {
            EventViewModel(app).markReceiptReviewInProgress()

            val recreatedViewModel = EventViewModel(app)
            assertTrue(recreatedViewModel.receiptReviewInterrupted.value)

            recreatedViewModel.clearReceiptReviewInProgress()
            assertFalse(recreatedViewModel.receiptReviewInterrupted.value)
            assertFalse(EventViewModel(app).receiptReviewInterrupted.value)
        } finally {
            preferences.edit().remove("receipt_review_in_progress").commit()
        }
    }

    @Test
    fun `verify event ID encoding and decoding`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = EventViewModel(app)

        val originalId = 42
        val encoded = viewModel.encodeEventId(originalId)
        assertNotEquals(originalId.toString(), encoded)
        assertTrue(encoded.isNotEmpty())

        val decoded = viewModel.decodeEventId(encoded)
        assertEquals(originalId, decoded)
    }

    @Test
    fun `verify deep link checksum generation and matching`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = EventViewModel(app)

        val eventId = 101
        val expiry = System.currentTimeMillis() + 7200000L // 2 Hours
        val creator = "creator@gmail.com"

        val sig1 = viewModel.generateInviteChecksum(eventId, expiry, creator)
        val sig2 = viewModel.generateInviteChecksum(eventId, expiry, creator)
        val sigDifferent = viewModel.generateInviteChecksum(eventId, expiry, "another_creator@gmail.com")

        assertEquals(sig1, sig2)
        assertNotEquals(sig1, sigDifferent)
    }

    @Test
    fun `verify valid link adds an event copy`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = EventViewModel(app)

        viewModel.setMyUserEmail("any_user@gmail.com")

        val eventId = 202
        val expiry = System.currentTimeMillis() + 3600000L // 1 Hour
        val creator = "creator@gmail.com"

        val sig = viewModel.generateInviteChecksum(eventId, expiry, creator)
        val encEventId = viewModel.encodeEventId(eventId)
        val deepLinkUri = Uri.parse("https://gopib9119.github.io/event/join?eventId=$encEventId&expiry=$expiry&checksum=$sig&creatorEmail=$creator&title=TestEvent")

        viewModel.handleDeepLink(deepLinkUri)
        awaitDeepLinkResult(viewModel)

        assertNull(viewModel.deepLinkError.value)
        assertEquals("Added 'TestEvent' to this device. Ledger entries do not sync between devices.", viewModel.deepLinkMessage.value)
    }

    @Test
    fun `verify deep link blocked for expired links`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = EventViewModel(app)

        viewModel.setMyUserEmail("logged_in_user@gmail.com")

        val eventId = 202
        val expiryExpired = System.currentTimeMillis() - 1000L // Already expired
        val creator = "creator@gmail.com"

        val sigExpired = viewModel.generateInviteChecksum(eventId, expiryExpired, creator)
        val encEventId = viewModel.encodeEventId(eventId)
        val deepLinkUriExpired = Uri.parse("https://gopib9119.github.io/event/join?eventId=$encEventId&expiry=$expiryExpired&checksum=$sigExpired&creatorEmail=$creator")

        viewModel.handleDeepLink(deepLinkUriExpired)
        awaitDeepLinkResult(viewModel)

        assertTrue(viewModel.deepLinkError.value?.contains("Expired event-copy link") == true)
    }

    @Test
    fun `verify deep link blocked for invalid signatures`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = EventViewModel(app)

        viewModel.setMyUserEmail("logged_in_user@gmail.com")

        val eventId = 202
        val expiry = System.currentTimeMillis() + 3600000L // 1 Hour
        val creator = "creator@gmail.com"

        val encEventId = viewModel.encodeEventId(eventId)
        val deepLinkUriTampered = Uri.parse("https://gopib9119.github.io/event/join?eventId=$encEventId&expiry=$expiry&checksum=tampered_signature&creatorEmail=$creator")

        viewModel.handleDeepLink(deepLinkUriTampered)
        awaitDeepLinkResult(viewModel)

        assertTrue(viewModel.deepLinkError.value?.contains("Invalid event-copy link") == true)
    }

    @Test
    fun `verify one day event-copy link allows entry`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = EventViewModel(app)

        viewModel.setMyUserEmail("logged_in_user@gmail.com")

        val eventId = 303
        val expiryOneDay = System.currentTimeMillis() + 24 * 3600000L // 1 Day
        val creator = "creator@gmail.com"

        val sig = viewModel.generateInviteChecksum(eventId, expiryOneDay, creator)
        val encEventId = viewModel.encodeEventId(eventId)
        val deepLinkUri = Uri.parse("https://gopib9119.github.io/event/join?eventId=$encEventId&expiry=$expiryOneDay&checksum=$sig&creatorEmail=$creator&title=OneDayEvent")

        viewModel.handleDeepLink(deepLinkUri)
        awaitDeepLinkResult(viewModel)

        assertNull(viewModel.deepLinkError.value)
        assertEquals("Added 'OneDayEvent' to this device. Ledger entries do not sync between devices.", viewModel.deepLinkMessage.value)
    }
}
