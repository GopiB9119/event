package com.example.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.EventEntity
import com.example.data.MemberEntity
import com.example.data.TransactionEntity
import com.example.data.normalizeLocalIdentity
import com.example.receipt.ParsedReceipt
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import kotlinx.coroutines.delay
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.regex.Pattern

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppContent(viewModel: EventViewModel) {
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val navigationStack = viewModel.navigationStack
    val pendingSharedReceipt by viewModel.pendingSharedReceipt.collectAsStateWithLifecycle()
    val receiptReviewInterrupted by viewModel.receiptReviewInterrupted.collectAsStateWithLifecycle()
    var isSharedReceiptHintDismissed by remember(pendingSharedReceipt) { mutableStateOf(false) }
    val currentScreen = navigationStack.lastOrNull() ?: Screen.Dashboard
    
    // Listen to invite-link messages and errors.
    val deepLinkMessage by viewModel.deepLinkMessage.collectAsStateWithLifecycle()
    val deepLinkError by viewModel.deepLinkError.collectAsStateWithLifecycle()

    if (receiptReviewInterrupted) {
        AlertDialog(
            onDismissRequest = { viewModel.clearReceiptReviewInProgress() },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                    contentDescription = "Receipt review interrupted",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(32.dp)
                )
            },
            title = { Text("Receipt Review Interrupted", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = "The app closed before receipt review finished. No transaction was saved. Open the correct event and scan or share the receipt again.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(onClick = { viewModel.clearReceiptReviewInProgress() }) {
                    Text("Got It")
                }
            }
        )
    }

    if (pendingSharedReceipt != null && currentScreen is Screen.Dashboard && !isSharedReceiptHintDismissed && !receiptReviewInterrupted) {
        AlertDialog(
            onDismissRequest = { isSharedReceiptHintDismissed = true },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                    contentDescription = "Shared receipt waiting",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
            },
            title = { Text("Receipt Ready to Import", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = "Open the event where this payment belongs. The shared receipt will be scanned and added to that event's review screen.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(onClick = { isSharedReceiptHintDismissed = true }) {
                    Text("Open an Event")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.clearPendingSharedReceipt() }) {
                    Text("Cancel Import")
                }
            }
        )
    }

    if (deepLinkMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissDeepLinkMessage() },
            icon = { Icon(Icons.Default.VerifiedUser, contentDescription = "Verified", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(36.dp)) },
            title = { Text("Event Copy Added", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center) },
            text = { Text(deepLinkMessage ?: "", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center) },
            confirmButton = {
                Button(onClick = { viewModel.dismissDeepLinkMessage() }) {
                    Text("Awesome")
                }
            }
        )
    }

    if (deepLinkError != null) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissDeepLinkError() },
            icon = { Icon(Icons.Default.Security, contentDescription = "Security Alert", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(36.dp)) },
            title = { Text("Event Link Problem", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center) },
            text = { Text(deepLinkError ?: "", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center) },
            confirmButton = {
                Button(
                    onClick = { viewModel.dismissDeepLinkError() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("OK, Understood")
                }
            }
        )
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                is Screen.Dashboard -> {
                    DashboardScreen(viewModel)
                }
                is Screen.CreateEvent -> {
                    CreateEventScreen(viewModel)
                }
                is Screen.TrustCenter -> {
                    TrustCenterScreen(viewModel)
                }
                is Screen.EventDetails -> {
                    EventDetailsScreen(eventId = currentScreen.eventId, viewModel = viewModel)
                }
            }
        }
    }
}

private fun getEventCreator(customFieldsJson: String): String {
    return try {
        val json = JSONObject(customFieldsJson)
        json.optString("creatorEmail", "")
    } catch (e: Exception) {
        ""
    }
}

private enum class EventVisibilityFilter {
    All,
    Public,
    Private
}

private enum class IdentityRequiredAction {
    CreateEvent,
    InviteMembers,
    ScanReceipt,
    ProcessSharedReceipt,
    ReplaceReceipt
}

@Composable
private fun LocalIdentityDialog(
    currentUserEmail: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Boolean
) {
    var emailInput by remember(currentUserEmail) { mutableStateOf(currentUserEmail) }
    var hasError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.testTag("local_identity_dialog"),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Local Identity", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Set a valid email label for event ownership and receipt uploads on this device. This is not sign-in or authentication.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                OutlinedTextField(
                    value = emailInput,
                    onValueChange = {
                        emailInput = it
                        hasError = false
                    },
                    label = { Text("Your Email Address") },
                    isError = hasError,
                    supportingText = {
                        if (hasError) {
                            Text("Enter a valid email address.")
                        }
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("local_identity_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (!onSave(emailInput)) {
                        hasError = true
                    }
                },
                modifier = Modifier.testTag("local_identity_save")
            ) {
                Text("Save Identity")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// ----------------------------------------------------
// A. DASHBOARD SCREEN
// ----------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: EventViewModel) {
    val events by viewModel.events.collectAsStateWithLifecycle()
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val currentUserEmail by viewModel.userEmail.collectAsStateWithLifecycle()
    val localBetaAcknowledged by viewModel.localBetaAcknowledged.collectAsStateWithLifecycle()
    var isThemeMenuExpanded by remember { mutableStateOf(false) }
    var isIdentityDialogOpen by remember { mutableStateOf(false) }
    var pendingIdentityAction by remember { mutableStateOf<IdentityRequiredAction?>(null) }
    var visibilityFilter by remember { mutableStateOf(EventVisibilityFilter.All) }
    val publicEventCount = events.count { !it.isPrivate }
    val privateEventCount = events.count { it.isPrivate }
    val visibleEvents = remember(events, visibilityFilter) {
        when (visibilityFilter) {
            EventVisibilityFilter.All -> events
            EventVisibilityFilter.Public -> events.filter { !it.isPrivate }
            EventVisibilityFilter.Private -> events.filter { it.isPrivate }
        }
    }

    // Multi-step deletion state
    var eventToDelete by remember { mutableStateOf<EventEntity?>(null) }
    var deleteStep by remember { mutableStateOf(1) } // 1: Check Leader, 2: Confirm Title match, 3: Math security puzzle, 4: Warning checklist & confirmation
    var deleteConfirmTitleText by remember { mutableStateOf("") }
    var mathResultText by remember { mutableStateOf("") }
    var num1 by remember { mutableStateOf(0) }
    var num2 by remember { mutableStateOf(0) }
    var checkedWarning1 by remember { mutableStateOf(false) }
    var checkedWarning2 by remember { mutableStateOf(false) }
    var checkedWarning3 by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Ultra-clean soft grey background
    ) {
        // App bar
        CenterAlignedTopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    /*
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = "Ledger Icon",
                        tint = MaterialTheme.colorScheme.primary, // Vibrant Blue
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    */
                    Text(
                        text = "Dashboard",
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface, // Slate-900
                        letterSpacing = (-0.5).sp
                    )
                }
            },
            actions = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { viewModel.navigateTo(Screen.TrustCenter) },
                        modifier = Modifier.testTag("trust_center_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "About, privacy, and terms",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = {
                            pendingIdentityAction = null
                            isIdentityDialogOpen = true
                        },
                        modifier = Modifier.testTag("identity_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "My Identity",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.Transparent
            )
        )

        // Visual Banner Card for Premium Visual Appeal
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF0F1E36), Color(0xFF1E293B))
                        )
                    )
                    .padding(24.dp)
            ) {
                // Technical grid circle overlays matching high-fidelity design
                Canvas(modifier = Modifier.matchParentSize()) {
                    drawCircle(
                        color = Color(0xFF38BDF8).copy(alpha = 0.12f),
                        radius = size.minDimension * 0.5f,
                        center = Offset(size.width * 0.85f, size.height * 0.2f)
                    )
                    drawCircle(
                        color = Color(0xFF38BDF8).copy(alpha = 0.06f),
                        radius = size.minDimension * 0.75f,
                        center = Offset(size.width * 0.15f, size.height * 0.8f)
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .background(
                                color = Color(0xFF38BDF8).copy(alpha = 0.15f),
                                shape = RoundedCornerShape(100.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        /*
                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = "Shield",
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = Color(0xFF38BDF8)
                        )
                        */
                    }

                    Text(
                        text = "Your Local Event Ledger",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.5).sp
                        ),
                        color = Color.White,
                        lineHeight = 22.sp
                    )

                    Text(
                        text = "Data stays on this device. Invite links copy event details but do not sync balances, transactions, or receipts.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.85f),
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Event List Section
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (events.isEmpty()) {
                // Friendly Empty State
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.EventNote,
                        contentDescription = "No Events Icon",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No Active Events",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "You haven't created any events yet. Tap the 'Create Event' button below to start tracking your festival or event activities.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = "Active Events",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                FilterChip(
                                    selected = visibilityFilter == EventVisibilityFilter.All,
                                    onClick = { visibilityFilter = EventVisibilityFilter.All },
                                    label = { Text("All ${events.size}", maxLines = 1) },
                                    modifier = Modifier.weight(1f).testTag("event_filter_all")
                                )
                                FilterChip(
                                    selected = visibilityFilter == EventVisibilityFilter.Public,
                                    onClick = { visibilityFilter = EventVisibilityFilter.Public },
                                    label = { Text("Public $publicEventCount", maxLines = 1) },
                                    modifier = Modifier.weight(1f).testTag("event_filter_public")
                                )
                                FilterChip(
                                    selected = visibilityFilter == EventVisibilityFilter.Private,
                                    onClick = { visibilityFilter = EventVisibilityFilter.Private },
                                    label = { Text("Private $privateEventCount", maxLines = 1) },
                                    modifier = Modifier.weight(1f).testTag("event_filter_private")
                                )
                            }
                        }
                    }
                    if (visibleEvents.isEmpty()) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 36.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = if (visibilityFilter == EventVisibilityFilter.Private) Icons.Default.Lock else Icons.Default.Public,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                    modifier = Modifier.size(42.dp)
                                )
                                Text(
                                    text = "No ${visibilityFilter.name.lowercase(Locale.getDefault())} events yet",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    } else {
                        items(visibleEvents, key = { it.id }) { event ->
                            EventCardItem(
                                event = event,
                                onClick = { viewModel.selectEvent(event.id) },
                                onDelete = { eventToDelete = event }
                            )
                        }
                    }
                }
            }

            // Extended Floating Action Button to Create Event
            ExtendedFloatingActionButton(
                onClick = {
                    if (normalizeLocalIdentity(currentUserEmail) == null) {
                        pendingIdentityAction = IdentityRequiredAction.CreateEvent
                        isIdentityDialogOpen = true
                    } else {
                        viewModel.navigateTo(Screen.CreateEvent)
                    }
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Create Event Icon",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                },
                text = {
                    Text(
                        text = "Create Event",
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.labelLarge.copy(
                            letterSpacing = 0.5.sp
                        ),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(24.dp)
                    .testTag("create_event_fab"),
                containerColor = MaterialTheme.colorScheme.primary, // Vibrant Blue
                contentColor = MaterialTheme.colorScheme.onPrimary,
                elevation = FloatingActionButtonDefaults.elevation(6.dp),
                shape = RoundedCornerShape(percent = 50) // Perfect pill shape
            )
        }
    }

    if (!localBetaAcknowledged) {
        var hasAcceptedLimits by remember { mutableStateOf(false) }
        AlertDialog(
            onDismissRequest = {},
            modifier = Modifier.testTag("local_beta_disclosure_dialog"),
            icon = {
                Icon(
                    imageVector = Icons.Default.PhoneAndroid,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            title = {
                Text("Before You Start", fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Community Ledger is currently a local-device beta.",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "• Events, balances, members, and receipts do not sync between devices.\n" +
                            "• Public/private is a local label, not access control.\n" +
                            "• There is no account recovery, export, or restore yet.\n" +
                            "• Uninstalling the app or losing this device deletes the ledger.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { hasAcceptedLimits = !hasAcceptedLimits }
                            .testTag("local_beta_acknowledgement_row"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = hasAcceptedLimits,
                            onCheckedChange = { hasAcceptedLimits = it },
                            modifier = Modifier.testTag("local_beta_acknowledgement_checkbox")
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "I understand that this beta stores unrecoverable data only on this device.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    enabled = hasAcceptedLimits,
                    onClick = viewModel::acknowledgeLocalBeta,
                    modifier = Modifier.testTag("local_beta_acknowledgement_confirm")
                ) {
                    Text("I Understand")
                }
            }
        )
    }

    // A1: Local Identity Dialog
    if (isIdentityDialogOpen) {
        LocalIdentityDialog(
            currentUserEmail = currentUserEmail,
            onDismiss = {
                pendingIdentityAction = null
                isIdentityDialogOpen = false
            },
            onSave = { email ->
                val saved = viewModel.setMyUserEmail(email)
                if (saved) {
                    val action = pendingIdentityAction
                    pendingIdentityAction = null
                    isIdentityDialogOpen = false
                    if (action == IdentityRequiredAction.CreateEvent) {
                        viewModel.navigateTo(Screen.CreateEvent)
                    }
                }
                saved
            }
        )
    }

    // A2: Multi-step Event Decommissioning / Deletion Dialog Flow
    if (eventToDelete != null) {
        val event = eventToDelete!!
        val creator = getEventCreator(event.customFieldsJson)
        val isLeader = currentUserEmail.equals(creator, ignoreCase = true)

        AlertDialog(
            onDismissRequest = {
                eventToDelete = null
                deleteStep = 1
                deleteConfirmTitleText = ""
                mathResultText = ""
                num1 = 0
                num2 = 0
                checkedWarning1 = false
                checkedWarning2 = false
                checkedWarning3 = false
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isLeader) Icons.Default.Warning else Icons.Default.Lock,
                        contentDescription = "Alert",
                        tint = if (isLeader) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = if (!isLeader) "Access Blocked" else "Delete Event: Step $deleteStep of 3",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (!isLeader) {
                        Text(
                            text = "⛔ Permission Denied!",
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "You are not authorized to decommission this event. Only the Event Leader (who created the event) can initiate deletion.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
                            )
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "Required Leader: $creator",
                                    fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Text(
                                    text = "Your Current Email: $currentUserEmail",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Text(
                            text = "If you are the leader on another device or want to test, you can change your email in the profile identity menu at the top-right.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        when (deleteStep) {
                            1 -> {
                                Text(
                                    text = "Step 1: Confirm Event Name",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Text(
                                    text = "To unlock the decommissioning valve, type the exact event title below:",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                    )
                                ) {
                                    Text(
                                        text = event.title,
                                        modifier = Modifier.padding(8.dp),
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                                OutlinedTextField(
                                    value = deleteConfirmTitleText,
                                    onValueChange = { deleteConfirmTitleText = it },
                                    label = { Text("Type event title") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            2 -> {
                                // Initialize random math puzzle
                                if (num1 == 0 && num2 == 0) {
                                    num1 = (12..29).random()
                                    num2 = (11..28).random()
                                }
                                Text(
                                    text = "Step 2: Human Intention Proof",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Text(
                                    text = "Solve this security equation to verify you are not executing this action accidentally:",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = "🧮  $num1 + $num2 = ?",
                                    fontWeight = FontWeight.ExtraBold,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                                OutlinedTextField(
                                    value = mathResultText,
                                    onValueChange = { mathResultText = it },
                                    label = { Text("Answer") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            3 -> {
                                Text(
                                    text = "Step 3: Acknowledge Decommission Warnings",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Text(
                                    text = "Review and check all safety protocols to release the delete permission:",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Checkbox(
                                            checked = checkedWarning1,
                                            onCheckedChange = { checkedWarning1 = it }
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "I accept that all shared ledger balances will be wiped.",
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Checkbox(
                                            checked = checkedWarning2,
                                            onCheckedChange = { checkedWarning2 = it }
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "I confirm no community members have pending unpaid receipts.",
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Checkbox(
                                            checked = checkedWarning3,
                                            onCheckedChange = { checkedWarning3 = it }
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "I understand this decommission cannot be undone.",
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                if (!isLeader) {
                    Button(
                        onClick = {
                            eventToDelete = null
                        }
                    ) {
                        Text("Acknowledge")
                    }
                } else {
                    when (deleteStep) {
                        1 -> {
                            Button(
                                enabled = deleteConfirmTitleText.trim() == event.title,
                                onClick = {
                                    deleteStep = 2
                                }
                            ) {
                                Text("Next Step")
                            }
                        }
                        2 -> {
                            Button(
                                enabled = mathResultText.trim() == (num1 + num2).toString(),
                                onClick = {
                                    deleteStep = 3
                                }
                            ) {
                                Text("Verify Puzzle")
                            }
                        }
                        3 -> {
                            Button(
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error
                                ),
                                enabled = checkedWarning1 && checkedWarning2 && checkedWarning3,
                                onClick = {
                                    viewModel.deleteEvent(event.id)
                                    eventToDelete = null
                                    deleteStep = 1
                                    deleteConfirmTitleText = ""
                                    mathResultText = ""
                                    num1 = 0
                                    num2 = 0
                                    checkedWarning1 = false
                                    checkedWarning2 = false
                                    checkedWarning3 = false
                                }
                            ) {
                                Text("PERMANENTLY DELETE", color = MaterialTheme.colorScheme.onError)
                            }
                        }
                    }
                }
            },
            dismissButton = {
                if (isLeader) {
                    TextButton(
                        onClick = {
                            eventToDelete = null
                            deleteStep = 1
                            deleteConfirmTitleText = ""
                            mathResultText = ""
                            num1 = 0
                            num2 = 0
                            checkedWarning1 = false
                            checkedWarning2 = false
                            checkedWarning3 = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            }
        )
    }
}

@Composable
fun EventCardItem(
    event: EventEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val dateString = remember(event.createdDate) {
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        sdf.format(Date(event.createdDate))
    }

    val accentColor = if (event.isPrivate) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f) else MaterialTheme.colorScheme.primary // Slate grey for private, Vibrant Blue for public

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("event_card_${event.id}")
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            // Asymmetric Left Accent Status Bar
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(accentColor)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = event.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Created Date",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Created: $dateString",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .size(36.dp)
                            .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete Event",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Duration Badge if present
                    if (event.duration != null) {
                        Row(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f), // soft blue
                                    RoundedCornerShape(20.dp)
                                )
                                .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timelapse,
                                contentDescription = "Duration Icon",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Duration: ${event.duration}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant, // soft slate
                                    RoundedCornerShape(20.dp)
                                )
                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.HourglassEmpty,
                                contentDescription = "Ongoing",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Ongoing Event",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }

                    // Privacy Indicator
                    if (event.isPrivate) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Private Event",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Invite Only",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Public,
                                contentDescription = "Public Event",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Public Event",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// B. EVENT CREATION SCREEN
// ----------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(viewModel: EventViewModel) {
    var title by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var isPrivate by remember { mutableStateOf(false) }

    // Custom extra fields
    val customFieldsList = remember { mutableStateListOf<Pair<String, String>>() }
    var customFieldNameInput by remember { mutableStateOf("") }
    var customFieldValueInput by remember { mutableStateOf("") }
    var isAddingCustomField by remember { mutableStateOf(false) }

    var titleError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Ultra-clean soft grey background
    ) {
        TopAppBar(
            title = { 
                Text(
                    text = "Create Event",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                ) 
            },
            navigationIcon = {
                IconButton(onClick = { viewModel.navigateBack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                        contentDescription = "Go Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Event Title with modern Vibrant Utility Design Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(
                        width = 2.dp,
                        color = if (titleError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Celebration,
                                    contentDescription = "Event Title Icon",
                                    tint = if (titleError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Event Title",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                            // Required badge
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = if (titleError) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (titleError) "REQUIRED" else "MANDATORY",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (titleError) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Give your ledger event a clear, recognizable name.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = title,
                            onValueChange = {
                                title = it
                                if (it.isNotBlank()) titleError = false
                            },
                            placeholder = { Text("e.g. Ganesh Chaturthi") },
                            isError = titleError,
                            supportingText = {
                                if (titleError) {
                                    Text("Event title is required", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("event_title_input"),
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                                errorBorderColor = MaterialTheme.colorScheme.error,
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f),
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.05f),
                                errorContainerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f),
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }
            }

            // Event Duration with modern Vibrant Utility Design Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Timelapse,
                                    contentDescription = "Duration Icon",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Event Duration",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                            // Optional badge
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "OPTIONAL",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Specify how long this event will remain active.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = duration,
                            onValueChange = { duration = it },
                            placeholder = { Text("e.g. how many days?") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("event_duration_input"),
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f),
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.05f),
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }
            }

            // Public / Private Toggle styled cleanly as a prominent state card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isPrivate) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(
                        width = 2.dp,
                        color = if (isPrivate) MaterialTheme.colorScheme.primary.copy(alpha = 0.4f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = if (isPrivate) Icons.Default.Lock else Icons.Default.Public,
                                contentDescription = "Privacy Status",
                                tint = if (isPrivate) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                modifier = Modifier
                                    .padding(top = 2.dp)
                                    .size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (isPrivate) "Private Event" else "Public Event",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (isPrivate) "Marked private in this app and in invite links. Share only with trusted people; this is not server authorization." else "Marked public in this app. Anyone who receives the link can add this ledger to their device.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Switch(
                            checked = isPrivate,
                            onCheckedChange = { isPrivate = it },
                            modifier = Modifier.testTag("event_private_switch"),
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                                checkedTrackColor = MaterialTheme.colorScheme.primary,
                                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        )
                    }
                }
            }

            // Custom fields section inside a modern card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Assignment,
                                    contentDescription = "Custom Info Icon",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Custom Info Fields",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                            IconButton(onClick = { isAddingCustomField = !isAddingCustomField }) {
                                Icon(
                                    imageVector = if (isAddingCustomField) Icons.Default.Close else Icons.Default.AddCircle,
                                    contentDescription = "Add custom info field",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Add custom info fields like Venue, Coordinator Name, Rules, Contact info etc.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Form to add custom field with high-contrast inputs
                        AnimatedVisibility(visible = isAddingCustomField) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                                ),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                                modifier = Modifier.padding(vertical = 12.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    OutlinedTextField(
                                        value = customFieldNameInput,
                                        onValueChange = { customFieldNameInput = it },
                                        label = { Text("Field Name") },
                                        placeholder = { Text("e.g. Venue, Coordinator") },
                                        singleLine = true,
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    OutlinedTextField(
                                        value = customFieldValueInput,
                                        onValueChange = { customFieldValueInput = it },
                                        label = { Text("Field Value") },
                                        placeholder = { Text("e.g. Town Hall, Suresh Kumar") },
                                        singleLine = true,
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Button(
                                        onClick = {
                                            if (customFieldNameInput.isNotBlank() && customFieldValueInput.isNotBlank()) {
                                                customFieldsList.add(customFieldNameInput.trim() to customFieldValueInput.trim())
                                                customFieldNameInput = ""
                                                customFieldValueInput = ""
                                                isAddingCustomField = false
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = MaterialTheme.colorScheme.onPrimary
                                        ),
                                        shape = RoundedCornerShape(percent = 50),
                                        modifier = Modifier.align(Alignment.End)
                                    ) {
                                        Text("Add Field")
                                    }
                                }
                            }
                        }

                        // Display added custom fields
                        if (customFieldsList.isEmpty()) {
                            Text(
                                text = "No custom parameters defined yet.",
                                style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        } else {
                            Spacer(modifier = Modifier.height(8.dp))
                            customFieldsList.forEachIndexed { index, pair ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .background(
                                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                            RoundedCornerShape(12.dp)
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = pair.first,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = pair.second,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    IconButton(
                                        onClick = { customFieldsList.removeAt(index) },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Delete custom field",
                                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Submission Action Button (Styled as high-visibility Vibrant Blue Pill Button)
            item {
                Button(
                    onClick = {
                        if (title.isBlank()) {
                            titleError = true
                        } else {
                            viewModel.createEvent(
                                title = title.trim(),
                                duration = duration.trim().takeIf { it.isNotBlank() },
                                isPrivate = isPrivate,
                                customFields = customFieldsList.toMap()
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("create_event_submit_button"),
                    shape = RoundedCornerShape(percent = 50), // Perfect pill shape
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary, // Vibrant Blue signature accent
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Create Event",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success Icon",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// C. EVENT DETAILS SCREEN (SCREEN C IN SKETCHES)
// ----------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsScreen(eventId: Int, viewModel: EventViewModel) {
    val event by viewModel.selectedEvent.collectAsStateWithLifecycle()
    val transactions by viewModel.selectedEventTransactions.collectAsStateWithLifecycle()
    val invitedList by viewModel.selectedEventMembers.collectAsStateWithLifecycle()
    val currentUserEmail by viewModel.userEmail.collectAsStateWithLifecycle()
    val pendingSharedReceipt by viewModel.pendingSharedReceipt.collectAsStateWithLifecycle()

    // Dialog state controllers
    var isInviteDialogOpen by remember { mutableStateOf(false) }
    var isIdentityDialogOpen by remember { mutableStateOf(false) }
    var pendingIdentityAction by remember { mutableStateOf<IdentityRequiredAction?>(null) }
    var replacementPendingIdentity by remember { mutableStateOf<TransactionEntity?>(null) }
    var selectedMemberForProfile by remember { mutableStateOf<MemberEntity?>(null) }

    // OCR result verifier modal
    var extractedReceiptToVerify by remember { mutableStateOf<ParsedReceipt?>(null) }
    var isVerifyReceiptDialogOpen by remember { mutableStateOf(false) }
    var transactionPendingReceiptReview by remember { mutableStateOf<TransactionEntity?>(null) }

    var selectedImageUriForPipeline by remember { mutableStateOf<Uri?>(null) }
    var isProcessingPipelineActive by remember { mutableStateOf(false) }
    var receiptProcessingToken by remember { mutableStateOf(0) }
    var clearPendingSharedReceiptAfterProcessing by remember { mutableStateOf(false) }

    var replacingTransaction by remember { mutableStateOf<TransactionEntity?>(null) }
    var selectedImageUriForReplacementPipeline by remember { mutableStateOf<Uri?>(null) }
    var isReplacementPipelineActive by remember { mutableStateOf(false) }
    var replacementProcessingToken by remember { mutableStateOf(0) }

    val receiptImagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.markReceiptReviewInProgress()
            isVerifyReceiptDialogOpen = false
            extractedReceiptToVerify = null
            transactionPendingReceiptReview = null
            selectedImageUriForPipeline = uri
            clearPendingSharedReceiptAfterProcessing = false
            receiptProcessingToken++
            isProcessingPipelineActive = true
        }
    }

    LaunchedEffect(pendingSharedReceipt, eventId, currentUserEmail) {
        val sharedReceipt = pendingSharedReceipt ?: return@LaunchedEffect
        if (normalizeLocalIdentity(currentUserEmail) == null) {
            pendingIdentityAction = IdentityRequiredAction.ProcessSharedReceipt
            isIdentityDialogOpen = true
            return@LaunchedEffect
        }
        if (sharedReceipt.imageUri != null) {
            viewModel.markReceiptReviewInProgress()
            isVerifyReceiptDialogOpen = false
            extractedReceiptToVerify = null
            transactionPendingReceiptReview = null
            selectedImageUriForPipeline = sharedReceipt.imageUri
            clearPendingSharedReceiptAfterProcessing = true
            receiptProcessingToken++
            isProcessingPipelineActive = true
            return@LaunchedEffect
        }

        if (sharedReceipt.text.isNotBlank()) {
            viewModel.markReceiptReviewInProgress()
            isVerifyReceiptDialogOpen = false
            extractedReceiptToVerify = null
            transactionPendingReceiptReview = null
            clearPendingSharedReceiptAfterProcessing = false
            val parsed = viewModel.parseReceiptText(sharedReceipt.text).copy(
                extractionMethod = "Shared receipt text",
                rawTextPreview = sharedReceipt.text.lineSequence()
                    .map { it.trim() }
                    .filter { it.isNotBlank() }
                    .take(40)
                    .joinToString("\n")
            )
            extractedReceiptToVerify = parsed
            transactionPendingReceiptReview = null
            isVerifyReceiptDialogOpen = true
            viewModel.clearPendingSharedReceipt()
        }
    }

    if (event == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val currentEvent = event!!

    // Calculations
    val creditAmount = transactions.filter { it.type == "Donated" || it.type == "Credit" }.sumOf { it.amount }
    val debitAmount = transactions.filter { it.type == "Debit" || it.type == "Expense" }.sumOf { it.amount }
    val balance = creditAmount - debitAmount
    // Custom Fields parsing
    val customFieldsMap = remember(currentEvent.customFieldsJson) {
        val json = currentEvent.customFieldsJson
        if (json.isBlank() || json == "{}") {
            emptyMap()
        } else {
            val clean = json.trim().removePrefix("{").removeSuffix("}")
            val map = mutableMapOf<String, String>()
            val pattern = Pattern.compile("\"([^\"]*)\":\"([^\"]*)\"")
            val matcher = pattern.matcher(clean)
            while (matcher.find()) {
                val key = matcher.group(1) ?: ""
                val value = matcher.group(2) ?: ""
                map[key] = value
            }
            map
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = currentEvent.title,
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        letterSpacing = (-0.5).sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to list",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (normalizeLocalIdentity(currentUserEmail) == null) {
                            pendingIdentityAction = IdentityRequiredAction.InviteMembers
                            isIdentityDialogOpen = true
                        } else {
                            isInviteDialogOpen = true
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = "Share event copy",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        floatingActionButton = {
            // Big Upload Screenshot Floating Button (as shown in Sketch 3 at the bottom center)
            ExtendedFloatingActionButton(
                onClick = {
                    if (normalizeLocalIdentity(currentUserEmail) == null) {
                        pendingIdentityAction = IdentityRequiredAction.ScanReceipt
                        isIdentityDialogOpen = true
                    } else {
                        receiptImagePickerLauncher.launch("image/*")
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                elevation = FloatingActionButtonDefaults.elevation(6.dp),
                shape = RoundedCornerShape(percent = 50), // Perfect pill shape
                modifier = Modifier
                    .testTag("upload_screenshot_fab")
                    .padding(bottom = 16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Upload Icon", tint = MaterialTheme.colorScheme.onPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Scan Receipt",
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.labelLarge.copy(
                        letterSpacing = 0.5.sp
                    ),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background) // Ultra-clean soft grey background
                .padding(paddingValues),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 100.dp), // Spacious bottom padding so FAB never overlaps items
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section 1: Financial Cards Header (Credits, Debits, Balance)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        // Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            /*
                             * Header row for the ledger summary card.
                             *
                             * Purpose: visually anchors the card using an icon and a concise
                             * label so users immediately recognize the section as the
                             * live ledger dashboard. The icon provides a quick affordance
                             * while the label uses bold, small-caps-like styling for emphasis.
                             *
                             * Visual details:
                             * - Small dashboard icon colored with `primary` to signal live info
                             * - Horizontal spacing of 8.dp between icon and text for balance
                             * - `Text` uses `labelMedium` with `FontWeight.Black` and
                             *   increased `letterSpacing` to match the compact, high-contrast header
                             * - Color uses `primary` to align with the real-time status badge
                             *
                             * Accessibility: Keep `contentDescription` on the icon so screen
                             * readers announce "Dashboard" and the label text remains readable
                             * at small sizes.
                             */
                            // Pulsing Badge
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(100.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .background(Color(0xFF10B981), CircleShape) // Emerald Green
                                    )
                                    /*
                                     * Status badge label: "REAL-TIME"
                                     *
                                     * Purpose: succinctly indicate that the dashboard data
                                     * is updated live. The string is styled to remain
                                     * compact and legible at small sizes.
                                     *
                                     * Visual details mirrored from the UI:
                                     * - `style`: `MaterialTheme.typography.labelSmall`
                                     * - `fontWeight`: `FontWeight.Bold` for emphasis
                                     * - `color`: `MaterialTheme.colorScheme.primary` to match
                                     *   the real-time state coloring
                                     * - `fontSize`: `9.sp` to fit within the badge layout
                                     *
                                     * Accessibility note: keep the surrounding icon's
                                     * `contentDescription` and ensure color is not the
                                     * only cue by providing text content.
                                     */
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Available Balance Card (Hero Block)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color(0xFF0F1E36), Color(0xFF1E293B))
                                    )
                                )
                                .padding(20.dp)
                        ) {
                            // Technical grid circle overlays matching high-fidelity design
                            Canvas(modifier = Modifier.matchParentSize()) {
                                drawCircle(
                                    color = Color(0xFF38BDF8).copy(alpha = 0.12f),
                                    radius = size.minDimension * 0.5f,
                                    center = Offset(size.width * 0.85f, size.height * 0.2f)
                                )
                                drawCircle(
                                    color = Color(0xFF38BDF8).copy(alpha = 0.06f),
                                    radius = size.minDimension * 0.75f,
                                    center = Offset(size.width * 0.15f, size.height * 0.8f)
                                )
                            }
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Available Balance",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White.copy(alpha = 0.8f)
                                    )
                                    Icon(
                                        imageVector = if (balance >= 0) Icons.AutoMirrored.Filled.TrendingUp else Icons.AutoMirrored.Filled.TrendingDown,
                                        contentDescription = "Trend",
                                        tint = if (balance >= 0) Color(0xFF34D399) else Color(0xFFF87171),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "₹${String.format(Locale.getDefault(), "%,.2f", balance)}",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = (-0.5).sp
                                    ),
                                    color = if (balance >= 0) Color.White else Color(0xFFF87171)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Two column asymmetric grid: Collected & Spent
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Total Collected Card
                            Card(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                ),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowUpward,
                                            contentDescription = "Collected",
                                            tint = Color(0xFF10B981),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = "Total Collected",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "₹${String.format(Locale.getDefault(), "%,.2f", creditAmount)}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Black,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    
                                }
                            }

                            // Spent / Debited Card
                            Card(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                ),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowDownward,
                                            contentDescription = "Spent",
                                            tint = Color(0xFFEF4444),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = "Total Spent",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "₹${String.format(Locale.getDefault(), "%,.2f", debitAmount)}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFFEF4444)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Progress bar indicating Budget Spent Ratio
                        if (creditAmount > 0) {
                            val ratio = (debitAmount / creditAmount).toFloat().coerceIn(0f, 1f)
                            val percentageString = "${(ratio * 100).toInt()}%"

                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Collection Utilization Meter",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = percentageString,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Black,
                                        color = if (ratio > 0.85f) Color(0xFFEF4444) else MaterialTheme.colorScheme.primary
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                LinearProgressIndicator(
                                    progress = { ratio },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    color = if (ratio > 0.85f) Color(0xFFEF4444) else MaterialTheme.colorScheme.primary,
                                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            }
                        } else {
                            /*
                             * Informational placeholder shown when there are no collected contributions.
                             *
                             * Purpose: In the ledger details card this message tells the user that the
                             * collection utilization meter has no data yet and prompts them to start
                             * adding contributions. It uses a small, subtle typography style and an
                             * italicized, semi-transparent color to indicate secondary information.
                             *
                             * Visual details:
                             * - `style`: `MaterialTheme.typography.labelSmall` (small label text)
                             * - `color`: `onSurfaceVariant` with reduced alpha (muted/secondary)
                             * - `fontStyle`: `FontStyle.Italic` to emphasize guidance
                             * - `modifier`: `Modifier.fillMaxWidth()` so the text is centered across the card
                             * - `textAlign`: `TextAlign.Center` to keep the line visually balanced
                             *
                             * When real contributions exist the UI renders the utilization meter and
                             * this placeholder is not shown.
                             */
                        }

                        // Displaying optional custom fields inside the Ledger summary card as requested in Screen 3!
                        if (customFieldsMap.isNotEmpty()) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 12.dp),
                                thickness = 0.5.dp,
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                            )
                            Text(
                                text = "Event Details & Custom Info",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                            customFieldsMap.entries.chunked(2).forEach { rowEntries ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    rowEntries.forEach { entry ->
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .background(
                                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                                    shape = RoundedCornerShape(12.dp)
                                                )
                                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                        ) {
                                            Column {
                                                Text(
                                                    text = entry.key,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = entry.value,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }
                                    }
                                    if (rowEntries.size < 2) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                            }
                        }
                    }
                }
            }

            // Section 3: Member list
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surface,
                            RoundedCornerShape(20.dp)
                        )
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "👥 Members (${invitedList.size})",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.padding(start = 4.dp)
                        )
                        TextButton(
                            onClick = {
                                if (normalizeLocalIdentity(currentUserEmail) == null) {
                                    pendingIdentityAction = IdentityRequiredAction.InviteMembers
                                    isIdentityDialogOpen = true
                                } else {
                                    isInviteDialogOpen = true
                                }
                            },
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Share event copy", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Share Copy", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (invitedList.isEmpty()) {
                        Text(
                            text = "Add members to link their contributions and expenses",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
                        )
                    } else {
                        Spacer(modifier = Modifier.height(8.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            invitedList.forEach { member ->
                                // Transaction calculations for this person
                                val memberTx = transactions.filter { tx ->
                                    tx.memberId == member.id ||
                                        (tx.memberId == null && (
                                            tx.personName.equals(member.name, ignoreCase = true) ||
                                                (member.phone.isNotBlank() && tx.personPhone == member.phone) ||
                                                (member.email.isNotBlank() && tx.personEmail == member.email)
                                        ))
                                }
                                val credits = memberTx.filter { it.type == "Donated" || it.type == "Credit" }
                                val debits = memberTx.filter { it.type == "Debit" || it.type == "Expense" }

                                val creditTimes = credits.size
                                val debitTimes = debits.size
                                val totalCredit = credits.sumOf { it.amount }
                                val totalDebit = debits.sumOf { it.amount }
                                val receiptUploadCount = memberTx.count { tx ->
                                    tx.notes?.trim()?.startsWith("{") == true
                                }

                                Card(
                                    onClick = { selectedMemberForProfile = member },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                    ),
                                    shape = RoundedCornerShape(16.dp),
                                    border = BorderStroke(
                                        1.dp, 
                                        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                                    )
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(36.dp)
                                                        .background(
                                                            MaterialTheme.colorScheme.primaryContainer,
                                                            CircleShape
                                                        ),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = member.name.take(1).uppercase(),
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        fontWeight = FontWeight.Bold,
                                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Column {
                                                    Text(
                                                        text = member.name,
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        fontWeight = FontWeight.Bold,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                    Text(
                                                        text = "📞 ${member.phone.ifBlank { "No phone" }}  |  Role: ${member.role}",
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                    Text(
                                                        text = "Receipt uploads: $receiptUploadCount",
                                                        style = MaterialTheme.typography.labelSmall,
                                                        fontWeight = FontWeight.Bold,
                                                        color = MaterialTheme.colorScheme.primary
                                                    )
                                                }
                                            }

                                            // Role label & arrow
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.ChevronRight,
                                                    contentDescription = "View Profile",
                                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))
                                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                                        Spacer(modifier = Modifier.height(8.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                                                    contentDescription = "Credit count",
                                                    tint = Color(0xFF10B981),
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "Credited: $creditTimes times (₹${String.format(Locale.getDefault(), "%,.0f", totalCredit)})",
                                                    style = MaterialTheme.typography.labelMedium,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = Color(0xFF10B981)
                                                )
                                            }

                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Filled.TrendingDown,
                                                    contentDescription = "Debit count",
                                                    tint = Color(0xFFEF4444),
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "Debited: $debitTimes times (₹${String.format(Locale.getDefault(), "%,.0f", totalDebit)})",
                                                    style = MaterialTheme.typography.labelMedium,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = Color(0xFFEF4444)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Section 4: Donation & Expense Ledger List
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📜 Transaction Ledger (${transactions.size})",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            if (transactions.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                                contentDescription = "Empty Ledger",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Ledger is empty",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Upload payment screenshots or share images to post transactions to the ledger.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(transactions) { tx ->
                    val eventCreatorEmail = getEventCreator(currentEvent.customFieldsJson)
                    TransactionItem(
                        tx = tx,
                        currentUserEmail = currentUserEmail,
                        eventCreatorEmail = eventCreatorEmail,
                        onDelete = { viewModel.deleteTransaction(tx.id) },
                        onReplaceScreenshot = {
                            if (normalizeLocalIdentity(currentUserEmail) == null) {
                                replacementPendingIdentity = tx
                                pendingIdentityAction = IdentityRequiredAction.ReplaceReceipt
                                isIdentityDialogOpen = true
                            } else {
                                replacingTransaction = tx
                            }
                        }
                    )
                }
            }
        }
    }

    // DIALOGS & SHEET MODALS

    if (isIdentityDialogOpen) {
        LocalIdentityDialog(
            currentUserEmail = currentUserEmail,
            onDismiss = {
                if (pendingIdentityAction == IdentityRequiredAction.ProcessSharedReceipt) {
                    viewModel.clearPendingSharedReceipt()
                }
                pendingIdentityAction = null
                replacementPendingIdentity = null
                isIdentityDialogOpen = false
            },
            onSave = { email ->
                val saved = viewModel.setMyUserEmail(email)
                if (saved) {
                    val action = pendingIdentityAction
                    val replacement = replacementPendingIdentity
                    pendingIdentityAction = null
                    replacementPendingIdentity = null
                    isIdentityDialogOpen = false
                    when (action) {
                        IdentityRequiredAction.InviteMembers -> isInviteDialogOpen = true
                        IdentityRequiredAction.ScanReceipt -> receiptImagePickerLauncher.launch("image/*")
                        IdentityRequiredAction.ReplaceReceipt -> replacingTransaction = replacement
                        IdentityRequiredAction.ProcessSharedReceipt,
                        IdentityRequiredAction.CreateEvent,
                        null -> Unit
                    }
                }
                saved
            }
        )
    }

    // 1. Share Event Copy Dialog (Includes link generation and local member registration)
    if (isInviteDialogOpen) {
        val context = LocalContext.current

        // Invite link generation states
        var linkDurationHours by remember { mutableStateOf("2 Hours") } // 10 Minutes, 1 Hour, 2 Hours, 1 Day
        var personalizedNote by remember { mutableStateOf("") }
        val linkDurationOptions = listOf(
            "10 Minutes" to "10Min",
            "1 Hour" to "1H",
            "2 Hours" to "2H",
            "1 Day" to "1D"
        )

        val clicksMap by viewModel.linkClicks.collectAsStateWithLifecycle()
        val currentClicks = clicksMap[currentEvent.id] ?: viewModel.getLinkClicks(currentEvent.id)

        // Generate values (10 Min, 1 Hour, 2 Hours, 1 Day expiration)
        val durationMs = when (linkDurationHours) {
            "10 Minutes" -> 600000L
            "1 Hour" -> 3600000L
            "2 Hours" -> 2 * 3600000L
            "1 Day" -> 24 * 3600000L
            else -> 2 * 3600000L
        }
        val expiryVal = System.currentTimeMillis() + durationMs

        val creatorEmail = viewModel.getMyUserEmail()
        val encodedEventId = viewModel.encodeEventId(currentEvent.id)
        val checksum = viewModel.generateInviteChecksum(
            eventId = currentEvent.id,
            expiry = expiryVal,
            creatorEmail = creatorEmail,
            isPrivate = currentEvent.isPrivate
        )
        val generatedLink = "https://gopib9119.github.io/event/join/?eventId=${Uri.encode(encodedEventId)}&expiry=$expiryVal&checksum=$checksum&title=${Uri.encode(currentEvent.title)}&creatorEmail=${Uri.encode(creatorEmail)}&private=${currentEvent.isPrivate}"

        val formattedExpiry = remember(expiryVal) {
            SimpleDateFormat("dd MMM hh:mm a", Locale.getDefault()).format(Date(expiryVal))
        }

        // Live Dynamic WhatsApp message builder (without ledger summary statistics)
        val shareMessage = remember(
            currentEvent.title,
            personalizedNote,
            creatorEmail,
            currentEvent.isPrivate,
            formattedExpiry,
            linkDurationHours,
            generatedLink
        ) {
            buildString {
                append(" *Event Copy Link* \n\n")
                if (personalizedNote.isNotBlank()) {
                    append("“${personalizedNote.trim()}”\n\n")
                }
                append("An organizer shared this event ledger shell:\n")
                append("*Event Name:* *${currentEvent.title}*\n\n")
                
                append("*Organizer:* ${creatorEmail.ifBlank { "Community Member" }}\n")
                append("*Visibility:* ${if (currentEvent.isPrivate) "Private marker" else "Public marker"}\n")
                append("*Link Expires:* $formattedExpiry ($linkDurationHours)\n")
                append("*Local only:* This link adds event details. Ledger entries and balances do not sync between devices.\n")
                
                append("Open the link below to add an independent local copy:\n")
                append("🔗 $generatedLink\n\n")
                
    
            }
        }

        AlertDialog(
            onDismissRequest = { isInviteDialogOpen = false },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Invite Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Share Event Copy",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "This link adds an event shell on another device; it does not sync ledger entries or balances. It preserves the public/private marker and includes a checksum for accidental changes, not security.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // 1. Personalized Note Input
                    OutlinedTextField(
                        value = personalizedNote,
                        onValueChange = { personalizedNote = it },
                        placeholder = { Text("Add message... (Optional)") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("personalized_note_input"),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Note",
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )

                    // 2. Link Duration Choice
                    Text(
                        text = "⏳ Link Time Limit",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        linkDurationOptions.forEach { (duration, label) ->
                            FilterChip(
                                selected = linkDurationHours == duration,
                                onClick = { linkDurationHours = duration },
                                label = { Text(label, fontSize = 11.sp) }
                            )
                        }
                    }

                    Text(
                        text = "Expires at: $formattedExpiry.",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF2E7D32)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Sharing Actions Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Copy Link Button
                        OutlinedButton(
                            onClick = {
                                val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clipData = ClipData.newPlainText("Ledger Link", shareMessage)
                                clipboardManager.setPrimaryClip(clipData)
                                Toast.makeText(context, "Event copy link copied", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f).testTag("copy_invite_link_button")
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp)) 
                            Text("Copy Link", fontSize = 11.sp, maxLines = 1)
                        }

                        // Share on WhatsApp Button
                        Button(
                            onClick = {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, shareMessage)
                                }
                                try {
                                    val whatsappIntent = Intent(shareIntent).apply {
                                        setPackage("com.whatsapp")
                                    }
                                    context.startActivity(whatsappIntent)
                                } catch (e: Exception) {
                                    context.startActivity(Intent.createChooser(shareIntent, "Share Ledger Link"))
                                }
                            },
                            modifier = Modifier.weight(1.2f).testTag("send_invite_link_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF25D366),
                                contentColor = Color.White
                            )
                        ) {
                            Icon(Icons.Default.Share, contentDescription = "WhatsApp", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Send Link", fontSize = 11.sp, maxLines = 1)
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { isInviteDialogOpen = false }) {
                    Text("Close")
                }
            }
        )
    }

    // 2. Member Payment & Contribution Profile Dialog
    if (selectedMemberForProfile != null) {
        val member = selectedMemberForProfile!!
        // Filter transactions associated with this member's details
        val memberTx = transactions.filter { tx ->
            tx.memberId == member.id ||
                (tx.memberId == null && (
                    tx.personName.equals(member.name, ignoreCase = true) ||
                        (member.phone.isNotBlank() && tx.personPhone == member.phone) ||
                        (member.email.isNotBlank() && tx.personEmail == member.email)
                ))
        }
        val credits = memberTx.filter { it.type == "Donated" || it.type == "Credit" }
        val debits = memberTx.filter { it.type == "Debit" || it.type == "Expense" }

        val creditTimes = credits.size
        val debitTimes = debits.size
        val totalCredit = credits.sumOf { it.amount }
        val totalDebit = debits.sumOf { it.amount }

        AlertDialog(
            onDismissRequest = { selectedMemberForProfile = null },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = member.name.take(1).uppercase(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = member.name, 
                            fontWeight = FontWeight.Bold, 
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Role: ${member.role}", 
                            style = MaterialTheme.typography.labelSmall, 
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Contact details card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "📞 Phone: ${member.phone.ifBlank { "Not registered" }}", 
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "✉️ Email: ${member.email.ifBlank { "Not registered" }}", 
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Text(
                        text = "📊 OCR Verified Contribution Frequency", 
                        fontWeight = FontWeight.Bold, 
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Grid-like layout for credit and debit metrics
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.TrendingUp, 
                                    contentDescription = "Credits", 
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Credited Money", style = MaterialTheme.typography.labelSmall)
                                Text(
                                    text = "$creditTimes times", 
                                    fontWeight = FontWeight.Bold, 
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "₹${String.format(Locale.getDefault(), "%,.0f", totalCredit)}", 
                                    fontWeight = FontWeight.Bold, 
                                    style = MaterialTheme.typography.bodyLarge, 
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.25f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.TrendingDown, 
                                    contentDescription = "Debits", 
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Debited Money", style = MaterialTheme.typography.labelSmall)
                                Text(
                                    text = "$debitTimes times", 
                                    fontWeight = FontWeight.Bold, 
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "₹${String.format(Locale.getDefault(), "%,.0f", totalDebit)}", 
                                    fontWeight = FontWeight.Bold, 
                                    style = MaterialTheme.typography.bodyLarge, 
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }

                    Text(
                        text = "📜 Scanned Receipt Details (${memberTx.size})", 
                        fontWeight = FontWeight.Bold, 
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    if (memberTx.isEmpty()) {
                        Text(
                            text = "No screenshot transactions found for this member yet. Share or upload receipt images to post directly.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            memberTx.forEach { tx ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), 
                                            RoundedCornerShape(8.dp)
                                        )
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        val formattedDate = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(tx.date))
                                        Text(
                                            text = tx.notes ?: "Receipt Post", 
                                            style = MaterialTheme.typography.bodySmall, 
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = formattedDate, 
                                            style = MaterialTheme.typography.labelSmall, 
                                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "${if (tx.type == "Donated" || tx.type == "Credit") "+" else "-"} ₹${String.format(Locale.getDefault(), "%,.0f", tx.amount)}",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (tx.type == "Donated" || tx.type == "Credit") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { selectedMemberForProfile = null }
                ) {
                    Text("Close Profile")
                }
            }
        )
    }

    // Real-time Image Processing Pipeline Terminal
    if (isProcessingPipelineActive && selectedImageUriForPipeline != null) {
        val selectedUri = selectedImageUriForPipeline!!
        val context = LocalContext.current
        var currentStep by remember { mutableStateOf(0) }
        val logs = remember { mutableStateListOf<String>() }

        LaunchedEffect(selectedUri, receiptProcessingToken) {
            logs.clear()
            extractedReceiptToVerify = null
            isVerifyReceiptDialogOpen = false
            logs.add("⏳ [INIT] Preparing local OCR cleanup pipeline...")
            delay(800)
            currentStep = 1
            logs.add("🔒 [PRIVACY] Stripping sensitive EXIF metadata, GPS details, and camera tags...")
            // Call the real EXIF stripper
            val cleanBitmap = viewModel.stripImageMetadataAndProcess(context, selectedUri)
            delay(1000)
            currentStep = 2
            if (cleanBitmap != null) {
                logs.add("✨ [PRIVACY] OCR bitmap prepared without image metadata.")
            } else {
                logs.add("⚠️ [PRIVACY] Using processed image stream for OCR.")
            }
            delay(600)
            currentStep = 3
            logs.add("🎨 [PREPROCESS] Applying grayscale filter and 1.5x color matrix contrast...")
            delay(800)
            currentStep = 4
            logs.add("🔍 [OCR] Reading text directly from the selected image...")
            val parsedResult = viewModel.extractReceiptFromUri(context, selectedUri)
            delay(1000)
            currentStep = 5
            logs.add("⚡ [METHOD] Extraction Method used: ${parsedResult.extractionMethod}")
            logs.add("🎯 [EXTRACT] Amount: ₹${parsedResult.amount}, ID: ${parsedResult.transactionId}")
            delay(800)
            currentStep = 6
            logs.add("🧾 [REVIEW] Extraction ready. Please verify before saving to ledger...")
            delay(800)
            extractedReceiptToVerify = parsedResult
            transactionPendingReceiptReview = null
            if (clearPendingSharedReceiptAfterProcessing) {
                viewModel.clearPendingSharedReceipt()
                clearPendingSharedReceiptAfterProcessing = false
            }
            isProcessingPipelineActive = false
            isVerifyReceiptDialogOpen = true
        }

        AlertDialog(
            onDismissRequest = {
                isProcessingPipelineActive = false
                selectedImageUriForPipeline = null
                if (clearPendingSharedReceiptAfterProcessing) {
                    viewModel.clearPendingSharedReceipt()
                    clearPendingSharedReceiptAfterProcessing = false
                }
                viewModel.clearReceiptReviewInProgress()
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.SettingsSuggest,
                        contentDescription = "Processing",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Image Processing", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Reading text from the selected payment screenshot. Nothing is saved until you confirm the extracted values.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Terminal-style Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 160.dp, max = 240.dp)
                            .background(Color(0xFF1E1E1E), RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFF333333), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            items(logs) { log ->
                                Text(
                                    text = log,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    color = if (log.contains("🎯") || log.contains("✨")) Color(0xFF4CAF50) else if (log.contains("⚠️")) Color(0xFFFFEB3B) else Color(0xFF00FF00)
                                )
                            }
                        }
                    }

                    // Progress Loader Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LinearProgressIndicator(
                            progress = { currentStep.toFloat() / 6f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    }
                    Text(
                        text = "Scrubbing original headers to block camera, location, and device metadata leaking into ledger archives...",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {}
        )
    }

    // 3b. Replace Screenshot / Receipt Correction Dialog
    if (replacingTransaction != null) {
        val context = LocalContext.current
        val txToReplace = replacingTransaction!!
        val imagePickerLauncherForReplacement = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            if (uri != null) {
                viewModel.markReceiptReviewInProgress()
                isVerifyReceiptDialogOpen = false
                extractedReceiptToVerify = null
                selectedImageUriForReplacementPipeline = uri
                replacementProcessingToken++
                isReplacementPipelineActive = true
            }
        }


        AlertDialog(
            onDismissRequest = { replacingTransaction = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ChangeCircle, "Replace", tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Re-upload Screenshot")
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "re-upload a corrected screenshot in place of the existing one for transaction ID: ${txToReplace.id}.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Updated Screenshot:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    OutlinedButton(
                        onClick = { 
                            imagePickerLauncherForReplacement.launch("image/*")
                        },
                        modifier = Modifier.fillMaxWidth().testTag("upload_real_replacement_button"),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.CloudUpload, contentDescription = "Upload")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Select Corrected Image from Device")
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { replacingTransaction = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Real-time Image Processing Pipeline Terminal for screenshot replacements
    if (isReplacementPipelineActive && selectedImageUriForReplacementPipeline != null && replacingTransaction != null) {
        val selectedUri = selectedImageUriForReplacementPipeline!!
        val context = LocalContext.current
        val txToReplace = replacingTransaction!!
        var currentStep by remember { mutableStateOf(0) }
        val logs = remember { mutableStateListOf<String>() }

        LaunchedEffect(selectedUri, replacementProcessingToken) {
            logs.clear()
            extractedReceiptToVerify = null
            isVerifyReceiptDialogOpen = false
            logs.add("⏳ [INIT] Preparing local OCR cleanup pipeline...")
            delay(600)
            currentStep = 1
            logs.add("🔒 [PRIVACY] Stripping sensitive EXIF metadata, GPS details, and camera tags...")
            val cleanBitmap = viewModel.stripImageMetadataAndProcess(context, selectedUri)
            delay(800)
            currentStep = 2
            if (cleanBitmap != null) {
                logs.add("✨ [PRIVACY] OCR bitmap prepared without image metadata.")
            } else {
                logs.add("⚠️ [PRIVACY] Using processed image stream for OCR.")
            }
            delay(500)
            currentStep = 3
            logs.add("🎨 [PREPROCESS] Boosting contrast and threshold levels for OCR readability...")
            delay(600)
            currentStep = 4
            logs.add("🔍 [OCR] Reading text directly from the replacement image...")
            val parsedResult = viewModel.extractReceiptFromUri(context, selectedUri)
            delay(800)
            currentStep = 5
            logs.add("⚡ [METHOD] Extraction Method used: ${parsedResult.extractionMethod}")
            logs.add("🎯 [EXTRACT] Extraction completed: Amount: ₹${parsedResult.amount}, ID: ${parsedResult.transactionId}")
            delay(600)
            currentStep = 6
            logs.add("🧾 [REVIEW] Replacement extraction ready. Please verify before saving...")
            delay(600)
            extractedReceiptToVerify = parsedResult
            transactionPendingReceiptReview = txToReplace
            isReplacementPipelineActive = false
            replacingTransaction = null
            isVerifyReceiptDialogOpen = true
        }

        AlertDialog(
            onDismissRequest = {
                isReplacementPipelineActive = false
                selectedImageUriForReplacementPipeline = null
                viewModel.clearReceiptReviewInProgress()
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.SettingsSuggest,
                        contentDescription = "Processing",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Processing Replacement Image", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Running local OCR cleanup to replace screenshot on entry ID: ${txToReplace.id}...",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 140.dp, max = 220.dp)
                            .background(Color(0xFF1E1E1E), RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFF333333), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            items(logs) { log ->
                                Text(
                                    text = log,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    color = if (log.contains("🎯") || log.contains("✨")) Color(0xFF4CAF50) else if (log.contains("⚠️")) Color(0xFFFFEB3B) else Color(0xFF00FF00)
                                )
                            }
                        }
                    }

                    LinearProgressIndicator(
                        progress = { currentStep.toFloat() / 6f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.primaryContainer
                    )
                }
            },
            confirmButton = {}
        )
    }

    // 4. Review Dialog
    if (isVerifyReceiptDialogOpen && extractedReceiptToVerify != null) {
        val parsed = extractedReceiptToVerify!!
        val replacementTarget = transactionPendingReceiptReview
        val receiptType = replacementTarget?.type ?: "Donated"
        val extractedPhone = parsed.phone.ifBlank { replacementTarget?.personPhone ?: "" }
        val extractedDate = parsed.date
        fun cleanJsonString(json: JSONObject?, key: String): String {
            if (json == null || json.isNull(key)) return ""
            return json.optString(key).takeIf { it.isNotBlank() && it != "null" } ?: ""
        }

        fun transactionReceiptJson(tx: TransactionEntity): JSONObject? {
            return try {
                val noteText = tx.notes?.trim().orEmpty()
                if (noteText.startsWith("{")) JSONObject(noteText) else null
            } catch (e: Exception) {
                null
            }
        }

        val duplicateReceipt = remember(transactions, parsed, extractedPhone, extractedDate, replacementTarget) {
            val newReference = parsed.transactionId.trim()
            val newPaidTo = parsed.payeeName.trim()
            val newUpiId = parsed.upiId.trim()
            val newPaymentApp = parsed.paymentApp.takeIf { it != "Unknown UPI" }.orEmpty()

            transactions.firstOrNull { tx ->
                if (replacementTarget?.id == tx.id) return@firstOrNull false

                val existingJson = transactionReceiptJson(tx)
                val existingReference = cleanJsonString(existingJson, "upiReferenceOrTransactionId")
                    .ifBlank { tx.transactionId.trim() }

                if (newReference.isNotBlank() && existingReference.equals(newReference, ignoreCase = true)) {
                    return@firstOrNull true
                }

                if (parsed.amount <= 0.0) return@firstOrNull false
                val existingAmount = if (existingJson != null && !existingJson.isNull("amount")) {
                    existingJson.optDouble("amount", tx.amount)
                } else {
                    tx.amount
                }
                val amountMatches = kotlin.math.abs(existingAmount - parsed.amount) < 0.01
                if (!amountMatches) return@firstOrNull false

                val existingDate = cleanJsonString(existingJson, "date")
                val existingPaymentApp = cleanJsonString(existingJson, "paymentApp")
                val existingPaidTo = cleanJsonString(existingJson, "paidTo")
                val existingUpiId = cleanJsonString(existingJson, "upiId")
                val dateMatches = extractedDate.isNotBlank() && existingDate.equals(extractedDate, ignoreCase = true)
                val appMatches = newPaymentApp.isNotBlank() && existingPaymentApp.equals(newPaymentApp, ignoreCase = true)
                val receiverMatches =
                    (newUpiId.isNotBlank() && existingUpiId.equals(newUpiId, ignoreCase = true)) ||
                        (newPaidTo.isNotBlank() && existingPaidTo.equals(newPaidTo, ignoreCase = true))

                listOf(dateMatches, appMatches, receiverMatches).count { it } >= 2
            }
        }
        val duplicateReason = duplicateReceipt?.let { tx ->
            val existingJson = transactionReceiptJson(tx)
            val existingReference = cleanJsonString(existingJson, "upiReferenceOrTransactionId")
                .ifBlank { tx.transactionId.trim() }
            if (parsed.transactionId.isNotBlank() && existingReference.equals(parsed.transactionId, ignoreCase = true)) {
                "Same UPI reference / transaction ID already exists."
            } else {
                "Same amount with matching receipt details already exists."
            }
        }
        val canSaveReceipt = parsed.amount > 0.0 && parsed.confidence >= 60 && duplicateReceipt == null
        val extractionLooksEmpty = parsed.amount <= 0.0 && parsed.transactionId.isBlank() && parsed.phone.isBlank() && parsed.email.isBlank()
        val hasOcrText = parsed.rawTextPreview.isNotBlank()
        val calculationBucket = if (receiptType == "Donated" || receiptType == "Credit") "Total Collected" else "Total Spent"
        val calculationOperation = if (receiptType == "Donated" || receiptType == "Credit") "add" else "subtract"
        val receiptJsonText = remember(parsed, replacementTarget, currentUserEmail) {
            JSONObject().apply {
                put("amount", if (parsed.amount > 0.0) parsed.amount else JSONObject.NULL)
                put("currency", "INR")
                put("calculationAmount", if (parsed.amount > 0.0) parsed.amount else JSONObject.NULL)
                put("calculationBucket", calculationBucket)
                put("calculationOperation", calculationOperation)
                put("paidTo", parsed.payeeName.ifBlank { JSONObject.NULL })
                put("upiId", parsed.upiId.ifBlank { JSONObject.NULL })
                put("upiReferenceOrTransactionId", parsed.transactionId.ifBlank { JSONObject.NULL })
                put("paymentApp", parsed.paymentApp.takeIf { it != "Unknown UPI" } ?: JSONObject.NULL)
                put("date", extractedDate.ifBlank { JSONObject.NULL })
                put("phone", extractedPhone.ifBlank { JSONObject.NULL })
                put("email", parsed.email.ifBlank { JSONObject.NULL })
                put("ledgerType", receiptType)
                put("uploaderEmail", currentUserEmail)
                put("extractionMethod", parsed.extractionMethod)
                put("confidence", parsed.confidence)
                put("warnings", org.json.JSONArray(parsed.validationWarnings))
                put("duplicateCheck", JSONObject().apply {
                    put("status", if (duplicateReceipt == null) "clear" else "possible_duplicate")
                    put("matchedTransactionRowId", duplicateReceipt?.id ?: JSONObject.NULL)
                    put("reason", duplicateReason ?: JSONObject.NULL)
                })
            }.toString(2)
        }

        AlertDialog(
            onDismissRequest = {
                isVerifyReceiptDialogOpen = false
                extractedReceiptToVerify = null
                transactionPendingReceiptReview = null
                viewModel.clearReceiptReviewInProgress()
            },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                        contentDescription = "Extracted receipt",
                        tint = if (extractionLooksEmpty || duplicateReceipt != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = "Extracted Receipt",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Surface(
                        color = if (extractionLooksEmpty) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.35f) else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = when {
                                    duplicateReceipt != null -> "Possible duplicate receipt"
                                    extractionLooksEmpty && hasOcrText -> "OCR text found"
                                    extractionLooksEmpty -> "No receipt data found"
                                    else -> "Check extracted values"
                                },
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (extractionLooksEmpty || duplicateReceipt != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = when {
                                    duplicateReceipt != null -> "This receipt looks already saved in this event. Saving is blocked to protect ledger totals."
                                    extractionLooksEmpty && hasOcrText -> "Text was read from the image, but amount/reference was not confidently parsed. Use the OCR preview below to correct it."
                                    extractionLooksEmpty -> "OCR could not read text from this image. It will not affect ledger calculations until an amount is detected."
                                    !canSaveReceipt -> "OCR detected an amount, but confidence is too low for ledger calculation. Re-upload a clearer receipt."
                                    else -> "Amount will be used for $calculationBucket calculations. Verify before saving."
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.55f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DocumentScanner,
                                    contentDescription = "OCR method",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = parsed.extractionMethod,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Text(
                                text = "Confidence: ${parsed.confidence}%",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (canSaveReceipt) Color(0xFF10B981) else MaterialTheme.colorScheme.error
                            )
                            if (parsed.validationWarnings.isNotEmpty()) {
                                Text(
                                    text = parsed.validationWarnings.joinToString("\n") { "• $it" },
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            if (duplicateReason != null) {
                                Text(
                                    text = "• $duplicateReason",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }

                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Extracted JSON",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = receiptJsonText,
                                style = MaterialTheme.typography.labelSmall,
                                fontFamily = FontFamily.Monospace,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 40,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                }
            },
            confirmButton = {
                Button(
                    enabled = canSaveReceipt,
                    onClick = {
                        val cleanAmount = parsed.amount.takeIf { it > 0.0 } ?: return@Button
                        val uploaderName = currentUserEmail.substringBefore("@")
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                        val receiptOwnerName = parsed.payeeName.ifBlank { uploaderName }
                        val storedReceiptPath = viewModel.saveReceiptJsonFile(
                            eventId = replacementTarget?.eventId ?: eventId,
                            personName = receiptOwnerName,
                            uploaderEmail = currentUserEmail,
                            receiptJsonText = receiptJsonText
                        )

                        if (replacementTarget == null) {
                            viewModel.addReceiptTransaction(
                                eventId = eventId,
                                personName = receiptOwnerName,
                                personPhone = extractedPhone,
                                personEmail = currentUserEmail,
                                amount = cleanAmount,
                                type = receiptType,
                                notes = if (storedReceiptPath.isNullOrBlank()) receiptJsonText else JSONObject(receiptJsonText).apply { put("receiptFilePath", storedReceiptPath) }.toString(2),
                                transactionId = parsed.transactionId,
                                uploaderEmail = currentUserEmail
                            )
                        } else {
                            viewModel.replaceReceiptTransaction(
                                txId = replacementTarget.id,
                                eventId = replacementTarget.eventId,
                                personName = replacementTarget.personName,
                                personPhone = extractedPhone,
                                personEmail = replacementTarget.personEmail,
                                amount = cleanAmount,
                                type = receiptType,
                                notes = if (storedReceiptPath.isNullOrBlank()) receiptJsonText else JSONObject(receiptJsonText).apply { put("receiptFilePath", storedReceiptPath) }.toString(2),
                                transactionId = parsed.transactionId,
                                uploaderEmail = currentUserEmail,
                                existingMemberId = replacementTarget.memberId
                            )
                        }

                        isVerifyReceiptDialogOpen = false
                        extractedReceiptToVerify = null
                        transactionPendingReceiptReview = null
                        viewModel.clearReceiptReviewInProgress()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("verify_receipt_confirm_button")
                ) {
                    Text(if (replacementTarget == null) "Save to Ledger" else "Update Transaction", fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
fun TransactionItem(
    tx: TransactionEntity,
    currentUserEmail: String,
    eventCreatorEmail: String,
    onDelete: () -> Unit,
    onReplaceScreenshot: () -> Unit
) {
    val formattedDate = remember(tx.date) {
        val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
        sdf.format(Date(tx.date))
    }

    val isCredit = tx.type == "Donated" || tx.type == "Credit"
    val isMyPost = currentUserEmail.equals(tx.uploaderEmail, ignoreCase = true)
    val canDelete = isMyPost || currentUserEmail.equals(eventCreatorEmail, ignoreCase = true)
    val receiptJson = remember(tx.notes) {
        try {
            val noteText = tx.notes?.trim().orEmpty()
            if (noteText.startsWith("{")) JSONObject(noteText) else null
        } catch (e: Exception) {
            null
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Circle Indicator
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            if (isCredit) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
                            else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.15f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isCredit) Icons.AutoMirrored.Filled.TrendingUp else Icons.AutoMirrored.Filled.TrendingDown,
                        contentDescription = "Tx Type",
                        tint = if (isCredit) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = tx.personName,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    if (tx.personPhone.isNotBlank()) {
                        Text(
                            text = "📞 ${tx.personPhone}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = formattedDate,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                    if (receiptJson != null) {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .padding(top = 6.dp)
                                .fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Text(
                                    text = "Receipt JSON",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "app: ${receiptJson.optString("paymentApp", "Unknown")}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "calc: ${if (isCredit) "+" else "-"}₹${String.format(Locale.getDefault(), "%,.2f", receiptJson.optDouble("calculationAmount", tx.amount))} → ${receiptJson.optString("calculationBucket", if (isCredit) "Total Collected" else "Total Spent")}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCredit) Color(0xFF10B981) else Color(0xFFEF4444),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                val paidTo = receiptJson.optString("paidTo")
                                if (paidTo.isNotBlank() && paidTo != "null") {
                                    Text(
                                        text = "paid to: $paidTo",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                val upiId = receiptJson.optString("upiId")
                                if (upiId.isNotBlank() && upiId != "null") {
                                    Text(
                                        text = "upi: $upiId",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontFamily = FontFamily.Monospace,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                Text(
                                    text = "ref: ${receiptJson.optString("upiReferenceOrTransactionId", "N/A")}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontFamily = FontFamily.Monospace,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    } else if (!tx.notes.isNullOrBlank()) {
                        Text(
                            text = tx.notes,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    if (tx.transactionId.isNotBlank()) {
                        Text(
                            text = "ID: ${tx.transactionId}",
                            style = MaterialTheme.typography.labelSmall,
                            fontFamily = FontFamily.Monospace,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    
                    // Real-time Visual uploader tracking
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            imageVector = if (isMyPost) Icons.Default.Person else Icons.Default.CloudUpload,
                            contentDescription = "Uploader Identity",
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isMyPost) "Uploaded by me" else "Uploaded by: ${tx.uploaderEmail.substringBefore("@")}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            fontWeight = if (isMyPost) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${if (isCredit) "+" else "-"} ₹${String.format(Locale.getDefault(), "%,.2f", tx.amount)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isCredit) Color(0xFF10B981) else Color(0xFFEF4444)
                )

                Text(
                    text = tx.type,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isCredit) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .background(
                            if (isCredit) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f) else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.15f),
                            RoundedCornerShape(20.dp)
                        )
                        .border(
                            1.dp,
                            (if (isCredit) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error).copy(alpha = 0.25f),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )

                if (canDelete) {
                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        IconButton(
                            onClick = onReplaceScreenshot,
                            modifier = Modifier
                                .size(28.dp)
                                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f), CircleShape)
                                .testTag("replace_screenshot_icon_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChangeCircle,
                                contentDescription = "Replace Screenshot",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        IconButton(
                            onClick = onDelete,
                            modifier = Modifier
                                .size(28.dp)
                                .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Delete entry",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

