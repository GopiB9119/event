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

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Community Ledger", appName)
    }

    @Test
    fun `verify AES event ID encryption and decryption`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = EventViewModel(app)

        val originalId = 42
        val encrypted = viewModel.encryptEventId(originalId)
        assertNotEquals(originalId.toString(), encrypted)
        assertTrue(encrypted.isNotEmpty())

        val decrypted = viewModel.decryptEventId(encrypted)
        assertEquals(originalId, decrypted)
    }

    @Test
    fun `verify deep link signature generation and matching`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = EventViewModel(app)

        val eventId = 101
        val expiry = System.currentTimeMillis() + 7200000L // 2 Hours
        val creator = "creator@gmail.com"

        val sig1 = viewModel.generateSignature(eventId, expiry, creator)
        val sig2 = viewModel.generateSignature(eventId, expiry, creator)
        val sigDifferent = viewModel.generateSignature(eventId, expiry, "another_creator@gmail.com")

        assertEquals(sig1, sig2)
        assertNotEquals(sig1, sigDifferent)
    }

    @Test
    fun `verify anyone can join with a valid deep link`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = EventViewModel(app)

        viewModel.setMyUserEmail("any_user@gmail.com")

        val eventId = 202
        val expiry = System.currentTimeMillis() + 3600000L // 1 Hour
        val creator = "creator@gmail.com"

        val sig = viewModel.generateSignature(eventId, expiry, creator)
        val encEventId = viewModel.encryptEventId(eventId)
        val deepLinkUri = Uri.parse("https://communityledger.com/join?eventId=$encEventId&expiry=$expiry&signature=$sig&creatorEmail=$creator&title=TestEvent")

        viewModel.handleDeepLink(deepLinkUri)
        var attempts = 0
        while (viewModel.deepLinkMessage.value == null && viewModel.deepLinkError.value == null && attempts < 50) {
            shadowOf(Looper.getMainLooper()).idle()
            Thread.sleep(20)
            attempts++
        }

        assertNull(viewModel.deepLinkError.value)
        assertEquals("Access Granted: Securely joined shared ledger event 'TestEvent' via secure invitation link!", viewModel.deepLinkMessage.value)
    }

    @Test
    fun `verify deep link blocked for expired links`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = EventViewModel(app)

        viewModel.setMyUserEmail("logged_in_user@gmail.com")

        val eventId = 202
        val expiryExpired = System.currentTimeMillis() - 1000L // Already expired
        val creator = "creator@gmail.com"

        val sigExpired = viewModel.generateSignature(eventId, expiryExpired, creator)
        val encEventId = viewModel.encryptEventId(eventId)
        val deepLinkUriExpired = Uri.parse("https://communityledger.com/join?eventId=$encEventId&expiry=$expiryExpired&signature=$sigExpired&creatorEmail=$creator")

        viewModel.handleDeepLink(deepLinkUriExpired)
        shadowOf(Looper.getMainLooper()).idle()

        assertTrue(viewModel.deepLinkError.value?.contains("Expired Link") == true)
    }

    @Test
    fun `verify deep link blocked for invalid signatures`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = EventViewModel(app)

        viewModel.setMyUserEmail("logged_in_user@gmail.com")

        val eventId = 202
        val expiry = System.currentTimeMillis() + 3600000L // 1 Hour
        val creator = "creator@gmail.com"

        val encEventId = viewModel.encryptEventId(eventId)
        val deepLinkUriTampered = Uri.parse("https://communityledger.com/join?eventId=$encEventId&expiry=$expiry&signature=tampered_signature&creatorEmail=$creator")

        viewModel.handleDeepLink(deepLinkUriTampered)
        shadowOf(Looper.getMainLooper()).idle()

        assertTrue(viewModel.deepLinkError.value?.contains("Security Block") == true)
    }

    @Test
    fun `verify smart heuristic amount and txn extraction from filename`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = EventViewModel(app)

        // 1. Google Pay Test
        val gpayUri = Uri.parse("content://test/gpay_1500_310725987654.png")
        val parsedGpay = viewModel.extractHeuristicsFromUri(app, gpayUri)
        assertNotNull(parsedGpay)
        assertEquals("Google Pay", parsedGpay.paymentApp)
        assertEquals(1500.0, parsedGpay.amount, 0.01)
        assertEquals("310725987654", parsedGpay.transactionId)

        // 2. PhonePe with Exact Date Test
        val phonepeUri = Uri.parse("content://test/Screenshot_20260625-101530_PhonePe.png")
        val parsedPhonepe = viewModel.extractHeuristicsFromUri(app, phonepeUri)
        assertNotNull(parsedPhonepe)
        assertEquals("PhonePe", parsedPhonepe.paymentApp)
        assertEquals("25 Jun 2026", parsedPhonepe.date)

        // 3. Paytm with Date and Amount Test
        val paytmUri = Uri.parse("content://test/paytm_amount_250_date_2026-06-24.png")
        val parsedPaytm = viewModel.extractHeuristicsFromUri(app, paytmUri)
        assertNotNull(parsedPaytm)
        assertEquals("Paytm", parsedPaytm.paymentApp)
        assertEquals(250.0, parsedPaytm.amount, 0.01)
        assertEquals("24 Jun 2026", parsedPaytm.date)
    }

    @Test
    fun `verify secure link with one day expiration allows entry`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = EventViewModel(app)

        viewModel.setMyUserEmail("logged_in_user@gmail.com")

        val eventId = 303
        val expiryOneDay = System.currentTimeMillis() + 24 * 3600000L // 1 Day
        val creator = "creator@gmail.com"

        val sig = viewModel.generateSignature(eventId, expiryOneDay, creator)
        val encEventId = viewModel.encryptEventId(eventId)
        val deepLinkUri = Uri.parse("https://communityledger.com/join?eventId=$encEventId&expiry=$expiryOneDay&signature=$sig&creatorEmail=$creator&title=OneDayEvent")

        viewModel.handleDeepLink(deepLinkUri)
        var attempts = 0
        while (viewModel.deepLinkMessage.value == null && viewModel.deepLinkError.value == null && attempts < 50) {
            shadowOf(Looper.getMainLooper()).idle()
            Thread.sleep(20)
            attempts++
        }

        assertNull(viewModel.deepLinkError.value)
        assertEquals("Access Granted: Securely joined shared ledger event 'OneDayEvent' via secure invitation link!", viewModel.deepLinkMessage.value)
    }
}
