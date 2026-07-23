package com.communityledger.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.communityledger.app.ui.AppContent
import com.communityledger.app.ui.EventViewModel
import com.communityledger.app.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  private val viewModel: EventViewModel by viewModels()

  private fun handleLaunchIntent(intent: Intent?) {
    viewModel.handleSharedReceiptIntent(intent)
    intent?.data?.let { uri ->
      viewModel.handleDeepLink(uri)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    handleLaunchIntent(intent)

    setContent {
      MyApplicationTheme(darkTheme = false) {
        AppContent(viewModel = viewModel)
      }
    }
  }

  override fun onNewIntent(intent: android.content.Intent) {
    super.onNewIntent(intent)
    setIntent(intent)
    handleLaunchIntent(intent)
  }
}
