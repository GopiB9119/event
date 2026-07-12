package com.example

import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Looper
import androidx.test.core.app.ApplicationProvider
import com.example.ui.EventViewModel
import com.example.ui.Screen
import org.json.JSONObject
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
    fun `receipt evidence file records its own private path`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = EventViewModel(app)

        val path = viewModel.saveReceiptJsonFile(
            eventId = 77,
            personName = "Synthetic Member",
            uploaderEmail = "organizer@example.com",
            receiptJsonText = JSONObject().apply {
                put("amount", 500.0)
                put("upiReferenceOrTransactionId", "SYNTHETIC123456")
            }.toString()
        )

        assertNotNull(path)
        val evidenceFile = java.io.File(path.orEmpty())
        assertTrue(evidenceFile.isFile)
        assertEquals(path, JSONObject(evidenceFile.readText()).getString("receiptFilePath"))
    }

    @Test
    fun `same receipt reference creates distinct evidence files`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = EventViewModel(app)
        val evidence = JSONObject().apply {
            put("amount", 500.0)
            put("upiReferenceOrTransactionId", "SYNTHETIC123456")
        }.toString()

        val firstPath = viewModel.saveReceiptJsonFile(78, "Synthetic Member", "organizer@example.com", evidence)
        val secondPath = viewModel.saveReceiptJsonFile(78, "Synthetic Member", "organizer@example.com", evidence)

        assertNotNull(firstPath)
        assertNotNull(secondPath)
        assertNotEquals(firstPath, secondPath)
        assertTrue(java.io.File(firstPath.orEmpty()).isFile)
        assertTrue(java.io.File(secondPath.orEmpty()).isFile)
    }

    @Test
    fun `receipt evidence rejects invalid event or uploader`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = EventViewModel(app)

        assertNull(viewModel.saveReceiptJsonFile(0, "Member", "organizer@example.com", "{}"))
        assertNull(viewModel.saveReceiptJsonFile(1, "Member", "not-an-email", "{}"))
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
        assertEquals("Added an independent copy of 'TestEvent'. You were not added as a member; entries and balances do not sync.", viewModel.deepLinkMessage.value)
    }

    @Test
    fun `legacy event copy with reused local id creates a distinct ledger`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = EventViewModel(app)

        viewModel.setMyUserEmail("local.organizer@example.com")
        viewModel.createEvent("Existing Local Event", null, false, emptyMap())
        shadowOf(Looper.getMainLooper()).idle()

        val reusedSourceId = 1
        val expiry = System.currentTimeMillis() + 3600000L
        val remoteCreator = "remote.organizer@example.com"
        val signature = viewModel.generateInviteChecksum(reusedSourceId, expiry, remoteCreator)
        val encodedEventId = viewModel.encodeEventId(reusedSourceId)
        val deepLinkUri = Uri.parse(
            "https://gopib9119.github.io/event/join?eventId=$encodedEventId" +
                "&expiry=$expiry&checksum=$signature&creatorEmail=$remoteCreator&title=RemoteEvent"
        )

        viewModel.handleDeepLink(deepLinkUri)
        awaitDeepLinkResult(viewModel)

        assertNull(viewModel.deepLinkError.value)
        assertEquals(
            "Added an independent copy of 'RemoteEvent'. You were not added as a member; entries and balances do not sync.",
            viewModel.deepLinkMessage.value
        )
        val openedEvent = viewModel.navigationStack.last() as Screen.EventDetails
        assertNotEquals(reusedSourceId, openedEvent.eventId)
    }

    @Test
    fun `legacy link cannot attach to matching local title and creator`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = EventViewModel(app)

        val creator = "organizer@example.com"
        val title = "Existing Imported Event"
        viewModel.setMyUserEmail(creator)
        viewModel.createEvent(title, null, false, emptyMap())
        shadowOf(Looper.getMainLooper()).idle()

        val legacySourceId = 1
        val expiry = System.currentTimeMillis() + 3600000L
        val signature = viewModel.generateInviteChecksum(legacySourceId, expiry, creator)
        val encodedEventId = viewModel.encodeEventId(legacySourceId)
        val deepLinkUri = Uri.parse(
            "https://gopib9119.github.io/event/join?eventId=$encodedEventId" +
                "&expiry=$expiry&checksum=$signature&creatorEmail=$creator&title=$title"
        )

        viewModel.handleDeepLink(deepLinkUri)
        awaitDeepLinkResult(viewModel)

        assertNull(viewModel.deepLinkError.value)
        val openedEvent = viewModel.navigationStack.last() as Screen.EventDetails
        assertNotEquals(legacySourceId, openedEvent.eventId)
    }

    @Test
    fun `legacy title change cannot create another event shell`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = EventViewModel(app)

        val legacySourceId = 404
        val expiry = System.currentTimeMillis() + 3600000L
        val creator = "organizer@example.com"
        val signature = viewModel.generateInviteChecksum(legacySourceId, expiry, creator)
        val encodedEventId = viewModel.encodeEventId(legacySourceId)
        val firstUri = Uri.parse(
            "https://gopib9119.github.io/event/join?eventId=$encodedEventId" +
                "&expiry=$expiry&checksum=$signature&creatorEmail=$creator&title=OriginalTitle"
        )
        val changedTitleUri = Uri.parse(
            "https://gopib9119.github.io/event/join?eventId=$encodedEventId" +
                "&expiry=$expiry&checksum=$signature&creatorEmail=$creator&title=ChangedTitle"
        )

        viewModel.handleDeepLink(firstUri)
        awaitDeepLinkResult(viewModel)
        val firstLocalId = (viewModel.navigationStack.last() as Screen.EventDetails).eventId

        viewModel.dismissDeepLinkMessage()
        viewModel.handleDeepLink(changedTitleUri)
        awaitDeepLinkResult(viewModel)
        val secondLocalId = (viewModel.navigationStack.last() as Screen.EventDetails).eventId

        assertNull(viewModel.deepLinkError.value)
        assertEquals(firstLocalId, secondLocalId)
        assertEquals(
            "Added an independent copy of 'OriginalTitle'. You were not added as a member; entries and balances do not sync.",
            viewModel.deepLinkMessage.value
        )
    }

    @Test
    fun `opaque event copy key reopens one local ledger`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = EventViewModel(app)

        val eventKey = "0123456789abcdef0123456789abcdef"
        val expiry = System.currentTimeMillis() + 3600000L
        val creator = "organizer@example.com"
        val title = "Shared Event"
        val checksum = viewModel.generateEventCopyChecksum(
            eventKey = eventKey,
            expiry = expiry,
            creatorEmail = creator,
            isPrivate = false,
            title = title
        )
        val deepLinkUri = Uri.parse(
            "https://gopib9119.github.io/event/join?eventKey=$eventKey" +
                "&expiry=$expiry&checksum=$checksum&creatorEmail=$creator&title=$title&private=false"
        )

        viewModel.handleDeepLink(deepLinkUri)
        awaitDeepLinkResult(viewModel)
        assertNull(viewModel.deepLinkError.value)
        val firstLocalEventId = (viewModel.navigationStack.last() as Screen.EventDetails).eventId

        viewModel.dismissDeepLinkMessage()
        viewModel.handleDeepLink(deepLinkUri)
        awaitDeepLinkResult(viewModel)
        assertNull(viewModel.deepLinkError.value)
        val reopenedLocalEventId = (viewModel.navigationStack.last() as Screen.EventDetails).eventId

        assertEquals(firstLocalEventId, reopenedLocalEventId)
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
        assertEquals("Added an independent copy of 'OneDayEvent'. You were not added as a member; entries and balances do not sync.", viewModel.deepLinkMessage.value)
    }
}
