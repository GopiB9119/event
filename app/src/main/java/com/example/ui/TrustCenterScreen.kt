package com.example.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.BuildConfig
import com.example.update.UpdateCheckResult

private const val SUPPORT_URL = "https://github.com/GopiB9119/event/issues"
private const val SUPPORT_EMAIL = "banothgopikrishna19@gmail.com"
private const val PUBLISHER_NAME = "Gopi Banoth"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrustCenterScreen(viewModel: EventViewModel) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("About", "Privacy", "Terms")

    Scaffold(
        modifier = Modifier.testTag("trust_center_screen"),
        topBar = {
            TopAppBar(
                title = { Text("Trust Center", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, label ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(label) }
                    )
                }
            }

            when (selectedTab) {
                0 -> AboutContent(viewModel)
                1 -> PrivacyContent()
                else -> TermsContent()
            }
        }
    }
}

@Composable
private fun AboutContent(viewModel: EventViewModel) {
    val context = LocalContext.current
    val updateResult by viewModel.updateCheckResult.collectAsStateWithLifecycle()
    val supportEmailIntent = remember(context) {
        Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$SUPPORT_EMAIL")).apply {
            putExtra(Intent.EXTRA_SUBJECT, "Community Ledger support")
        }
    }
    val canEmailSupport = remember(context, supportEmailIntent) {
        supportEmailIntent.resolveActivity(context.packageManager) != null
    }

    TrustContent {
        TrustHeader(
            icon = Icons.Default.Info,
            title = "Community Ledger",
            body = "A local-first Android ledger for community event money records and reviewed receipt evidence."
        )
        TrustSection(
            icon = Icons.AutoMirrored.Filled.ReceiptLong,
            title = "What this app does",
            body = "An organizer records donations, credits, expenses, and debits. Receipt screenshots can be read with on-device OCR, but nothing enters totals until a person reviews and saves it."
        )
        TrustSection(
            icon = Icons.Default.Gavel,
            title = "What this app does not do",
            body = "Community Ledger does not collect, hold, invest, transfer, or process money. It is not a bank, payment service, investment platform, fundraising platform, accounting service, or financial adviser."
        )
        TrustSection(
            icon = Icons.Default.Devices,
            title = "Event links create local copies",
            body = "An event-copy link carries only the event title, organizer label, and visibility marker to another device. It does not connect people to one live ledger, verify the organizer, or sync custom fields, members, transactions, balances, and receipts."
        )
        TrustSection(
            icon = Icons.Default.CloudOff,
            title = "Local beta limits",
            body = "There is no account recovery, export, restore, or cloud backup. Uninstalling the app or losing the device deletes its ledger. Keep an independent record for important money."
        )

        Text(
            text = "Version ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE}) · Local beta",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        TrustSection(
            title = "Publisher and private support",
            body = "Community Ledger is independently published by $PUBLISHER_NAME. Monitored support email: $SUPPORT_EMAIL. Use private email for privacy or sensitive reports, and include only the minimum information needed."
        )
        Button(
            onClick = { context.startActivity(supportEmailIntent) },
            enabled = canEmailSupport,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.SupportAgent, contentDescription = null)
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Text("Email Private Support")
        }

        TrustSection(
            title = "Updates",
            body = "Checks run only when you tap the button. The app reads a small HTTPS release file and never installs an APK silently."
        )
        Button(
            onClick = viewModel::checkForUpdates,
            enabled = updateResult != UpdateCheckResult.Checking,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (updateResult == UpdateCheckResult.Checking) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            }
            Text(if (updateResult == UpdateCheckResult.Checking) "Checking…" else "Check for Updates")
        }
        when (val result = updateResult) {
            UpdateCheckResult.Idle -> Text(
                text = "No automatic background checks.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            UpdateCheckResult.Checking -> Unit
            UpdateCheckResult.NotPublished -> Text(
                text = "The permanent public APK channel is not published yet.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            is UpdateCheckResult.UpToDate -> Text(
                text = "This build is current. Latest published version: ${result.latestVersionName}.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            is UpdateCheckResult.Failed -> Text(
                text = result.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
            is UpdateCheckResult.Available -> {
                Text(
                    text = "Version ${result.versionName} is available.${result.releaseNotes.firstOrNull()?.let { " $it" }.orEmpty()}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "SHA-256: ${result.sha256}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Button(
                    onClick = {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(result.downloadUrl)))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Open Official Release")
                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                    Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null)
                }
            }
        }

        Button(
            onClick = {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(SUPPORT_URL)))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.SupportAgent, contentDescription = null)
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Text("Open Support Page")
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null)
        }
        Text(
            text = "Use the public project page for non-sensitive bugs only. Never post receipts, phone numbers, email addresses, UPI IDs, transaction references, passwords, or verification codes in a public issue.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PrivacyContent() {
    TrustContent {
        TrustHeader(
            icon = Icons.Default.PrivacyTip,
            title = "Privacy Notice",
            body = "Effective 11 July 2026 · Published independently by $PUBLISHER_NAME"
        )
        TrustSection(
            title = "Data stored on this device",
            body = "The app stores events, member details, transaction records, your self-declared local email label, and reviewed receipt JSON evidence in app-private storage."
        )
        TrustSection(
            title = "Receipt processing",
            body = "Selected or shared receipt images are read on the device with Google ML Kit Latin and Devanagari text recognition. The app stores reviewed JSON evidence, not a cloud OCR result. OCR can be wrong, so review is required."
        )
        TrustSection(
            title = "SDK diagnostics and no advertising",
            body = "This beta has no ads, account service, crash-reporting service, or transaction server. Google states that ML Kit sends limited device/app information, a per-installation identifier, performance, API configuration, input/output size, feature-event, and error metadata for diagnostics and usage analytics. Receipt images and recognized text are processed on-device. Manual release checks run only when you tap Check for Updates."
        )
        TrustSection(
            title = "Sharing you control",
            body = "Android share actions send only the content you choose. Event-copy links can contain an opaque copy key, title, expiry, visibility marker, and local organizer label; anyone receiving a link may forward it. Public/private is not access control."
        )
        TrustSection(
            title = "Permissions",
            body = "The app does not request camera, location, contacts, microphone, SMS, or broad photo-library permission. Android grants temporary access only to a receipt image you select or share."
        )
        TrustSection(
            title = "Retention and deletion",
            body = "Data remains until you delete an event, clear app data, or uninstall the app. Android backup and device-transfer rules exclude the database, receipts, and preferences. There is currently no recovery."
        )
        TrustSection(
            title = "Support and privacy questions",
            body = "Email $SUPPORT_EMAIL for private support or privacy questions. Do not send passwords, verification codes, signing keys, or unredacted receipt data unless it is strictly necessary to investigate your report."
        )
    }
}

@Composable
private fun TermsContent() {
    TrustContent {
        TrustHeader(
            icon = Icons.Default.Gavel,
            title = "Beta Terms",
            body = "Plain-language conditions for using this friend beta."
        )
        TrustSection(
            title = "Use it as a record, not proof by itself",
            body = "You are responsible for checking names, amounts, dates, references, and totals against original payment records. OCR output and event-copy links do not prove that a payment, organizer, or event is genuine."
        )
        TrustSection(
            title = "No money or investment service",
            body = "The app does not receive donations, execute payments, hold funds, offer returns, or manage investments. Never send money solely because an event or event-copy link appears in the app."
        )
        TrustSection(
            title = "Organizer responsibility",
            body = "Only record information you are allowed to use. Obtain consent before storing member contact details, avoid unnecessary personal data, and follow the laws that apply to your event and fundraising activity."
        )
        TrustSection(
            title = "No spam, fraud, or impersonation",
            body = "Do not use Community Ledger to mislead donors, impersonate another organizer, distribute unwanted messages, hide expenses, or create false receipt evidence."
        )
        TrustSection(
            title = "Beta reliability",
            body = "This beta may contain defects and has no cloud recovery. Keep independent records and test updates before relying on them. Stop using the app if its limitations do not fit your needs."
        )
        TrustSection(
            title = "Local deletion",
            body = "You can delete events or uninstall the app. Uninstalling permanently removes local data from that device. The project cannot restore it."
        )
    }
}

@Composable
private fun TrustContent(content: @Composable ColumnScope.() -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                content = content
            )
        }
    }
}

@Composable
private fun TrustHeader(icon: ImageVector, title: String, body: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(body, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun TrustSection(title: String, body: String, icon: ImageVector? = null) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        Text(body, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(2.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    }
}