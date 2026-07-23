package com.communityledger.app.ui

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SharedReceiptIntentInstrumentedTest {
    @Test
    fun sharedTextIsRetainedUntilExplicitlyCleared() {
        val viewModel = createViewModel()
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Shared receipt text payload")
        }

        viewModel.handleSharedReceiptIntent(intent)

        assertEquals("Shared receipt text payload", viewModel.pendingSharedReceipt.value?.text)
        viewModel.clearPendingSharedReceipt()
        assertNull(viewModel.pendingSharedReceipt.value)
    }

    @Test
    fun sharedImageUriIsRetainedUntilExplicitlyCleared() {
        val viewModel = createViewModel()
        val receiptUri = Uri.parse("content://community-ledger-test/receipt-image")
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, receiptUri)
        }

        viewModel.handleSharedReceiptIntent(intent)

        val pendingReceipt = viewModel.pendingSharedReceipt.value
        assertNotNull(pendingReceipt)
        assertEquals(receiptUri, pendingReceipt?.imageUri)
        viewModel.clearPendingSharedReceipt()
        assertNull(viewModel.pendingSharedReceipt.value)
    }

    private fun createViewModel(): EventViewModel {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        return EventViewModel(context.applicationContext as Application)
    }
}