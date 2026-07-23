package com.communityledger.app.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.ClipDescription
import android.content.Context
import android.net.Uri
import android.os.PersistableBundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.communityledger.app.data.SharedEventLinkEntity
import com.communityledger.app.shared.SharedEntry
import com.communityledger.app.shared.SharedEntryStatus
import com.communityledger.app.shared.SharedMember
import com.communityledger.app.shared.SharedPresence
import com.communityledger.app.shared.SharedPresenceState
import com.communityledger.app.shared.SharedPrivateEvidence
import com.communityledger.app.shared.SharedRole
import com.communityledger.app.receipt.ParsedReceipt
import com.communityledger.app.shared.SharedReceiptPreparation
import com.communityledger.app.shared.prepareSharedReceiptSubmission
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedEventsScreen(viewModel: EventViewModel) {
    val account by viewModel.sharedAccount.collectAsStateWithLifecycle()
    val publicEvents by viewModel.sharedPublicEvents.collectAsStateWithLifecycle()
    val busy by viewModel.sharedBusy.collectAsStateWithLifecycle()
    val error by viewModel.sharedError.collectAsStateWithLifecycle()
    var displayName by remember { mutableStateOf(viewModel.getSharedDisplayName()) }
    var eventTitle by remember { mutableStateOf("") }
    var eventDuration by remember { mutableStateOf("") }
    var isPrivate by remember { mutableStateOf(true) }
    var inviteToken by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shared events", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = viewModel::navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            if (error != null) {
                item {
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.CloudOff, contentDescription = null)
                            Text(error.orEmpty(), modifier = Modifier.weight(1f))
                            TextButton(onClick = viewModel::clearSharedError) { Text("Dismiss") }
                        }
                    }
                }
            }

            item {
                Text("Shared profile", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = displayName,
                    onValueChange = { displayName = it.take(80) },
                    label = { Text("Display name") },
                    singleLine = true,
                    enabled = !busy,
                    modifier = Modifier.fillMaxWidth().testTag("shared_display_name")
                )
                Spacer(Modifier.height(10.dp))
                Button(
                    enabled = !busy && displayName.isNotBlank(),
                    onClick = { viewModel.connectSharedProfile(displayName) },
                    modifier = Modifier.fillMaxWidth().testTag("shared_connect_button")
                ) {
                    if (busy) CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
                    else Icon(Icons.Default.Person, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(if (account == null) "Connect" else "Connected as ${account?.displayName}")
                }
            }

            if (account != null) {
                item {
                    HorizontalDivider()
                    Spacer(Modifier.height(16.dp))
                    Text("Create shared event", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = eventTitle,
                        onValueChange = { eventTitle = it.take(120) },
                        label = { Text("Event title") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("shared_event_title")
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = eventDuration,
                        onValueChange = { eventDuration = it.take(160) },
                        label = { Text("Duration") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(if (isPrivate) "Private" else "Public", fontWeight = FontWeight.SemiBold)
                        Switch(checked = isPrivate, onCheckedChange = { isPrivate = it })
                    }
                    Button(
                        enabled = !busy && eventTitle.isNotBlank(),
                        onClick = {
                            viewModel.createSharedEvent(
                                eventTitle,
                                eventDuration.takeIf(String::isNotBlank),
                                isPrivate,
                                emptyMap()
                            )
                        },
                        modifier = Modifier.fillMaxWidth().testTag("shared_create_event_button")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Create")
                    }
                }

                item {
                    HorizontalDivider()
                    Spacer(Modifier.height(16.dp))
                    Text("Join with invite", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = inviteToken,
                        onValueChange = { inviteToken = it.take(100) },
                        label = { Text("Invite token") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("shared_invite_token")
                    )
                    Spacer(Modifier.height(10.dp))
                    OutlinedButton(
                        enabled = !busy && inviteToken.isNotBlank(),
                        onClick = { viewModel.acceptSharedInvite(inviteToken) },
                        modifier = Modifier.fillMaxWidth().testTag("shared_accept_invite_button")
                    ) {
                        Icon(Icons.Default.Link, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Join event")
                    }
                }

                item {
                    HorizontalDivider()
                    Spacer(Modifier.height(16.dp))
                    Text("Public events", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
                if (publicEvents.isEmpty()) {
                    item {
                        Text(
                            "No public events available.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    items(publicEvents, key = { it.id }) { event ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(Icons.Default.Public, contentDescription = null)
                                Column(Modifier.weight(1f)) {
                                    Text(event.title, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                    Text("${event.activeMemberCount} joined", style = MaterialTheme.typography.bodySmall)
                                }
                                Button(
                                    enabled = !busy,
                                    onClick = { viewModel.joinPublicSharedEvent(event.id) }
                                ) { Text("Join") }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedEventDetailsScreen(
    link: SharedEventLinkEntity,
    viewModel: EventViewModel
) {
    val live by viewModel.selectedSharedLiveEvent.collectAsStateWithLifecycle()
    val account by viewModel.sharedAccount.collectAsStateWithLifecycle()
    val busy by viewModel.sharedBusy.collectAsStateWithLifecycle()
    val error by viewModel.sharedError.collectAsStateWithLifecycle()
    val invite by viewModel.lastSharedInvite.collectAsStateWithLifecycle()
    val pendingSharedReceipt by viewModel.pendingSharedReceipt.collectAsStateWithLifecycle()
    val event = live.event
    var showInviteDialog by remember { mutableStateOf(false) }
    var selectedReceiptUri by remember { mutableStateOf<Uri?>(null) }
    var isProcessingReceipt by remember { mutableStateOf(false) }
    var reviewedReceipt by remember { mutableStateOf<ParsedReceipt?>(null) }
    var reviewEntry by remember { mutableStateOf<SharedEntry?>(null) }
    var reviewEvidence by remember { mutableStateOf<SharedPrivateEvidence?>(null) }
    var isLoadingReviewEvidence by remember { mutableStateOf(false) }
    var clearPendingReceiptAfterProcessing by remember { mutableStateOf(false) }
    var actionMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val sharedActionScope = rememberCoroutineScope()
    val receiptPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            viewModel.markReceiptReviewInProgress()
            clearPendingReceiptAfterProcessing = false
            selectedReceiptUri = uri
        }
    }

    LaunchedEffect(pendingSharedReceipt, event?.id, account?.uid, reviewedReceipt) {
        val sharedReceipt = pendingSharedReceipt ?: return@LaunchedEffect
        if (event == null || account == null || reviewedReceipt != null || selectedReceiptUri != null) {
            return@LaunchedEffect
        }
        viewModel.markReceiptReviewInProgress()
        if (sharedReceipt.imageUri != null) {
            clearPendingReceiptAfterProcessing = true
            selectedReceiptUri = sharedReceipt.imageUri
        } else if (sharedReceipt.text.isNotBlank()) {
            reviewedReceipt = viewModel.parseReceiptText(sharedReceipt.text).copy(
                extractionMethod = "Shared receipt text",
                rawTextPreview = sharedReceipt.text.lineSequence()
                    .map(String::trim)
                    .filter(String::isNotEmpty)
                    .take(40)
                    .joinToString("\n")
            )
            viewModel.clearPendingSharedReceipt()
        }
    }

    LaunchedEffect(selectedReceiptUri) {
        val uri = selectedReceiptUri ?: return@LaunchedEffect
        isProcessingReceipt = true
        reviewedReceipt = viewModel.extractReceiptFromUri(context, uri)
        isProcessingReceipt = false
        selectedReceiptUri = null
        if (clearPendingReceiptAfterProcessing) {
            clearPendingReceiptAfterProcessing = false
            viewModel.clearPendingSharedReceipt()
        }
    }

    DisposableEffect(lifecycleOwner, link.remoteEventId, account?.uid) {
        val observer = LifecycleEventObserver { _, lifecycleEvent ->
            when (lifecycleEvent) {
                Lifecycle.Event.ON_START -> viewModel.enterSelectedSharedEvent()
                Lifecycle.Event.ON_STOP -> viewModel.leaveSelectedSharedEvent()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            viewModel.enterSelectedSharedEvent()
        }
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            viewModel.leaveSelectedSharedEvent()
        }
    }

    val currentActionMessage = actionMessage
    if (currentActionMessage != null) {
        AlertDialog(
            onDismissRequest = { actionMessage = null },
            title = { Text("Shared ledger updated") },
            text = { Text(currentActionMessage) },
            confirmButton = { Button(onClick = { actionMessage = null }) { Text("OK") } }
        )
    }

    val currentInvite = invite
    if (currentInvite != null) {
        SharedInviteResultDialog(currentInvite.inviteToken, viewModel::clearSharedInvite)
    }

    if (showInviteDialog) {
        SharedInviteRoleDialog(
            busy = busy,
            onDismiss = { showInviteDialog = false },
            onCreate = { role ->
                viewModel.createSelectedSharedInvite(role, 3600)
                showInviteDialog = false
            }
        )
    }

    if (isProcessingReceipt) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Reading receipt") },
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(Modifier.size(22.dp), strokeWidth = 2.dp)
                    Spacer(Modifier.width(12.dp))
                    Text("On-device OCR is processing the selected image.")
                }
            },
            confirmButton = {}
        )
    }

    val receipt = reviewedReceipt
    val currentAccount = account
    if (receipt != null && event != null && currentAccount != null) {
        SharedReceiptReviewDialog(
            receipt = receipt,
            remoteEventId = link.remoteEventId,
            eventRevision = event.revision,
            accountUid = currentAccount.uid,
            role = link.role,
            members = live.members,
            busy = busy,
            onDismiss = {
                reviewedReceipt = null
                viewModel.clearReceiptReviewInProgress()
            },
            onSubmit = { preparation ->
                val request = preparation.request ?: return@SharedReceiptReviewDialog
                sharedActionScope.launch {
                    val result = viewModel.submitSelectedSharedEntry(request)
                    if (result != null) {
                        reviewedReceipt = null
                        viewModel.clearReceiptReviewInProgress()
                        actionMessage = when (result.status) {
                            SharedEntryStatus.PENDING -> "Receipt submitted for organizer review. Confirmed totals have not changed."
                            SharedEntryStatus.CONFIRMED -> "Receipt confirmed by the server and totals updated."
                            SharedEntryStatus.REJECTED -> "Receipt was rejected. Confirmed totals did not change."
                            null -> "Receipt submission completed."
                        }
                    }
                }
            }
        )
    }

    val pendingReview = reviewEntry
    if (pendingReview != null) {
        SharedEntryReviewDialog(
            entry = pendingReview,
            evidence = reviewEvidence,
            evidenceLoading = isLoadingReviewEvidence,
            busy = busy,
            onDismiss = {
                reviewEntry = null
                reviewEvidence = null
                isLoadingReviewEvidence = false
            },
            onDecision = { confirm, reason ->
                sharedActionScope.launch {
                    val result = viewModel.reviewSelectedSharedEntry(pendingReview.id, confirm, reason)
                    if (result != null) {
                        reviewEntry = null
                        reviewEvidence = null
                        actionMessage = if (confirm) {
                            "Receipt confirmed. Server totals and member history were updated."
                        } else {
                            "Receipt rejected. Confirmed totals did not change."
                        }
                    }
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        event?.title ?: "Shared event",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = viewModel::navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (link.role == "organizer") {
                        IconButton(onClick = { showInviteDialog = true }) {
                            Icon(Icons.Default.Share, contentDescription = "Create shared invite")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        floatingActionButton = {
            if (link.role != "viewer" && event != null) {
                ExtendedFloatingActionButton(
                    onClick = { receiptPicker.launch("image/*") },
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text("Scan receipt") },
                    modifier = Modifier.testTag("shared_scan_receipt")
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        if (event == null && error == null) {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(paddingValues),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 104.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            if (error != null) {
                item {
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(error.orEmpty(), Modifier.weight(1f))
                            IconButton(onClick = {
                                viewModel.clearSharedError()
                                viewModel.connectSharedProfile(viewModel.getSharedDisplayName())
                            }) {
                                Icon(Icons.Default.Refresh, contentDescription = "Reconnect shared event")
                            }
                        }
                    }
                }
            }

            if (event != null) {
                item { SharedTotalsSection(event) }
                item {
                    Text(
                        "Joined people (${event.activeMemberCount})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "${event.organizerCount} organizer · ${event.contributorCount} contributor · ${event.viewerCount} viewer",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (live.members.isEmpty()) {
                    item { Text("Member list is loading.", color = MaterialTheme.colorScheme.onSurfaceVariant) }
                } else {
                    items(live.members, key = SharedMember::uid) { member ->
                        SharedMemberRow(member, live.presence[member.uid])
                    }
                }
                item {
                    HorizontalDivider()
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Server ledger (${live.entries.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (live.entries.isEmpty()) {
                    item {
                        Text(
                            "No server-confirmed or visible pending entries.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    items(live.entries, key = SharedEntry::id) { entry ->
                        SharedEntryRow(
                            entry = entry,
                            canReview = link.role == "organizer" && entry.status == SharedEntryStatus.PENDING,
                            onReview = {
                                reviewEntry = entry
                                reviewEvidence = null
                                isLoadingReviewEvidence = true
                                sharedActionScope.launch {
                                    reviewEvidence = viewModel.loadSelectedSharedEvidence(entry.id)
                                    isLoadingReviewEvidence = false
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SharedTotalsSection(event: com.communityledger.app.shared.SharedEvent) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Server confirmed", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            Text(if (event.fromCache) "Cached" else "Live", style = MaterialTheme.typography.labelMedium)
        }
        Text(formatMinor(event.availableBalanceMinor), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SharedAmountBlock("Collected", event.totalCollectedMinor, Color(0xFF16835D), Modifier.weight(1f))
            SharedAmountBlock("Spent", event.totalSpentMinor, Color(0xFFB3261E), Modifier.weight(1f))
        }
        Text("${event.confirmedReceiptCount} confirmed receipts · revision ${event.revision}", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun SharedAmountBlock(label: String, value: Long, color: Color, modifier: Modifier) {
    Surface(modifier, color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp), border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)) {
        Column(Modifier.padding(12.dp)) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(formatMinor(value), fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
private fun SharedMemberRow(member: SharedMember, presence: SharedPresence?) {
    val presenceLabel = presenceLabel(presence)
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(member.displayName.take(1).uppercase(), fontWeight = FontWeight.Bold)
        }
        Column(Modifier.weight(1f)) {
            Text(member.displayName, fontWeight = FontWeight.SemiBold)
            Text(
                "${member.role.displayLabel} · $presenceLabel",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text("${member.confirmedReceiptCount}", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SharedEntryRow(entry: SharedEntry, canReview: Boolean, onReview: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(entry.ledgerPersonDisplayName, fontWeight = FontWeight.Bold)
                Text(
                    "${entry.ledgerType} · ${entry.status.displayLabel}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text("Submitted by ${entry.submittedByDisplayName}", style = MaterialTheme.typography.labelSmall)
            }
            Text(formatMinor(entry.amountMinor), fontWeight = FontWeight.Black)
        }
        if (canReview) {
            HorizontalDivider()
            TextButton(onClick = onReview, modifier = Modifier.fillMaxWidth()) {
                Text("Review pending entry")
            }
        }
    }
}

@Composable
private fun SharedReceiptReviewDialog(
    receipt: ParsedReceipt,
    remoteEventId: String,
    eventRevision: Long,
    accountUid: String,
    role: String,
    members: List<SharedMember>,
    busy: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (SharedReceiptPreparation) -> Unit
) {
    val operationKey = remember(receipt, remoteEventId) { com.communityledger.app.shared.newSharedOperationKey() }
    val ownMember = members.firstOrNull { it.uid == accountUid }
    var selectedMemberUid by remember(ownMember?.uid, members) { mutableStateOf(ownMember?.uid) }
    var memberSearch by remember { mutableStateOf("") }
    var ledgerType by remember { mutableStateOf("Donated") }
    val selectedMember = members.firstOrNull { it.uid == selectedMemberUid }
    val preparation = remember(receipt, remoteEventId, eventRevision, selectedMemberUid, ledgerType, operationKey) {
        prepareSharedReceiptSubmission(
            receipt = receipt,
            eventId = remoteEventId,
            expectedRevision = eventRevision,
            ledgerPersonUid = selectedMemberUid,
            ledgerType = ledgerType,
            idempotencyKey = operationKey
        )
    }
    val moneyIn = ledgerType == "Donated" || ledgerType == "Credit"
    val personName = selectedMember?.displayName ?: "Not selected"
    val visibleMembers = remember(members, memberSearch) {
        members.filter { it.displayName.contains(memberSearch.trim(), ignoreCase = true) }.take(20)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Review shared receipt", fontWeight = FontWeight.Bold) },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().heightIn(max = 520.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item { SharedDetailRow("Amount", formatReceiptAmount(receipt.amount)) }
                item { SharedDetailRow("Payment mode", receipt.paymentApp.takeUnless { it == "Unknown UPI" } ?: "Not detected") }
                item { SharedDetailRow("From", if (moneyIn) personName else "Event ledger") }
                item { SharedDetailRow("To", if (moneyIn) "Event ledger" else personName) }
                item { SharedDetailRow("Date", receipt.date.ifBlank { "Not detected" }) }
                item { SharedDetailRow("Reference", receipt.transactionId.ifBlank { "Not detected" }) }
                item {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = moneyIn,
                            onClick = { ledgerType = "Donated" },
                            label = { Text("Money in") },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = !moneyIn,
                            onClick = { ledgerType = "Expense" },
                            label = { Text("Money out") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                if (role == "organizer") {
                    item {
                        OutlinedTextField(
                            value = memberSearch,
                            onValueChange = { memberSearch = it.take(80) },
                            label = { Text("Ledger person") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    items(visibleMembers, key = SharedMember::uid) { member ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedMemberUid = member.uid }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedMemberUid == member.uid,
                                onClick = { selectedMemberUid = member.uid }
                            )
                            Text(member.displayName)
                        }
                    }
                } else {
                    item {
                        Text(
                            "Contributor submissions are attributed to your own membership.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                if (!preparation.canSubmit) {
                    item {
                        Surface(
                            color = MaterialTheme.colorScheme.errorContainer,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                preparation.blockingReasons.firstOrNull() ?: "Receipt cannot be submitted.",
                                modifier = Modifier.padding(10.dp),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                enabled = preparation.canSubmit && !busy,
                onClick = { onSubmit(preparation) },
                modifier = Modifier.testTag("shared_submit_receipt")
            ) { Text("Submit") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun SharedEntryReviewDialog(
    entry: SharedEntry,
    evidence: SharedPrivateEvidence?,
    evidenceLoading: Boolean,
    busy: Boolean,
    onDismiss: () -> Unit,
    onDecision: (Boolean, String?) -> Unit
) {
    var reject by remember { mutableStateOf(false) }
    var reason by remember { mutableStateOf("") }
    val evidenceMatchesEntry = evidence != null &&
        evidence.entryId == entry.id &&
        evidence.amountEvidenceSource == entry.amountEvidenceSource &&
        evidence.amountEvidenceConfidence == entry.amountEvidenceConfidence
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Review entry") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                SharedDetailRow("Person", entry.ledgerPersonDisplayName)
                SharedDetailRow("Amount", formatMinor(entry.amountMinor))
                SharedDetailRow("Direction", entry.ledgerType)
                when {
                    evidenceLoading -> Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
                        Spacer(Modifier.width(8.dp))
                        Text("Loading receipt evidence")
                    }
                    evidence != null -> {
                        SharedDetailRow("Payment mode", evidence.paymentApp ?: "Not detected")
                        SharedDetailRow("Date", evidence.paymentDate ?: "Not detected")
                        SharedDetailRow("Reference", evidence.paymentReference ?: "Not detected")
                        SharedDetailRow("Counterparty", evidence.counterparty ?: "Not detected")
                        SharedDetailRow(
                            "Amount evidence",
                            "${evidence.amountEvidenceSource} (${evidence.amountEvidenceConfidence}%)"
                        )
                        if (evidence.warnings.isNotEmpty()) {
                            Text(
                                evidence.warnings.joinToString(prefix = "Warnings: ", separator = "; "),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    else -> Text(
                        "Receipt evidence is unavailable. Confirmation is blocked.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(selected = !reject, onClick = { reject = false }, label = { Text("Confirm") })
                    FilterChip(selected = reject, onClick = { reject = true }, label = { Text("Reject") })
                }
                if (reject) {
                    OutlinedTextField(
                        value = reason,
                        onValueChange = { reason = it.take(500) },
                        label = { Text("Rejection reason") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                enabled = !busy && if (reject) reason.isNotBlank() else evidenceMatchesEntry,
                onClick = { onDecision(!reject, reason.trim().takeIf(String::isNotEmpty)) }
            ) { Text(if (reject) "Reject" else "Confirm") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun SharedDetailRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.End)
    }
}

private fun formatReceiptAmount(value: Double): String = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("en-IN"))
    .format(BigDecimal.valueOf(value))

@Composable
private fun SharedInviteRoleDialog(
    busy: Boolean,
    onDismiss: () -> Unit,
    onCreate: (SharedRole) -> Unit
) {
    var role by remember { mutableStateOf(SharedRole.CONTRIBUTOR) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create invite") },
        text = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = role == SharedRole.CONTRIBUTOR,
                    onClick = { role = SharedRole.CONTRIBUTOR },
                    label = { Text("Contributor") }
                )
                FilterChip(
                    selected = role == SharedRole.VIEWER,
                    onClick = { role = SharedRole.VIEWER },
                    label = { Text("Viewer") }
                )
            }
        },
        confirmButton = {
            Button(enabled = !busy, onClick = { onCreate(role) }) { Text("Create") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun SharedInviteResultDialog(inviteToken: String, onDismiss: () -> Unit) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Invite ready") },
        text = {
            Text(inviteToken, style = MaterialTheme.typography.bodySmall)
        },
        confirmButton = {
            Button(onClick = {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Community Ledger shared invite", inviteToken).apply {
                    description.extras = PersistableBundle().apply {
                        putBoolean(ClipDescription.EXTRA_IS_SENSITIVE, true)
                    }
                }
                clipboard.setPrimaryClip(clip)
                onDismiss()
            }) {
                Icon(Icons.Default.ContentCopy, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Copy")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Close") } }
    )
}

private fun formatMinor(value: Long): String = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("en-IN"))
    .format(BigDecimal.valueOf(value, 2))

private fun presenceLabel(presence: SharedPresence?): String {
    if (presence == null) return "Unavailable"
    val age = (System.currentTimeMillis() - (presence.updatedAtEpochMillis ?: 0L)).coerceAtLeast(0L)
    return when {
        presence.state == SharedPresenceState.ACTIVE && age <= 120_000L -> "Active now"
        presence.state != SharedPresenceState.UNAVAILABLE && age <= 10 * 60_000L -> "Recently active"
        else -> "Unavailable"
    }
}

private val SharedRole.displayLabel: String
    get() = name.lowercase().replaceFirstChar { it.uppercase() }

private val SharedEntryStatus.displayLabel: String
    get() = name.lowercase().replaceFirstChar { it.uppercase() }