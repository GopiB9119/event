package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.data.TransactionEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppContent(viewModel: EventViewModel) {
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val navigationStack = viewModel.navigationStack
    
    // Listen to deep link messages and error alerts from our Advanced Privacy Guard
    val deepLinkMessage by viewModel.deepLinkMessage.collectAsStateWithLifecycle()
    val deepLinkError by viewModel.deepLinkError.collectAsStateWithLifecycle()

    if (deepLinkMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissDeepLinkMessage() },
            icon = { Icon(Icons.Default.VerifiedUser, contentDescription = "Verified", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(36.dp)) },
            title = { Text("Secure Access Verified", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center) },
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
            title = { Text("Advanced Privacy Block", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center) },
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
            when (val currentScreen = navigationStack.lastOrNull() ?: Screen.Dashboard) {
                is Screen.Dashboard -> {
                    DashboardScreen(viewModel)
                }
                is Screen.CreateEvent -> {
                    CreateEventScreen(viewModel)
                }
                is Screen.EventDetails -> {
                    EventDetailsScreen(eventId = currentScreen.eventId, viewModel = viewModel)
                }
            }
        }
    }
}

// ----------------------------------------------------
// A. DASHBOARD SCREEN
// ----------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: EventViewModel) {
    val events by viewModel.events.collectAsStateWithLifecycle()
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    var isThemeMenuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // App bar
        CenterAlignedTopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = "Ledger Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Community Ledger",
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        letterSpacing = 0.5.sp
                    )
                }
            },
            actions = {
                Box {
                    IconButton(
                        onClick = { isThemeMenuExpanded = true },
                        modifier = Modifier.testTag("theme_button")
                    ) {
                        Icon(
                            imageVector = when (themeMode) {
                                "Light" -> Icons.Default.LightMode
                                "Dark" -> Icons.Default.DarkMode
                                else -> Icons.Default.SettingsSuggest
                            },
                            contentDescription = "Change Theme"
                        )
                    }
                    DropdownMenu(
                        expanded = isThemeMenuExpanded,
                        onDismissRequest = { isThemeMenuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("☀️ Light Mode") },
                            onClick = {
                                viewModel.setThemeMode("Light")
                                isThemeMenuExpanded = false
                            },
                            leadingIcon = { Icon(Icons.Default.LightMode, "Light") }
                        )
                        DropdownMenuItem(
                            text = { Text("🌙 Dark Mode") },
                            onClick = {
                                viewModel.setThemeMode("Dark")
                                isThemeMenuExpanded = false
                            },
                            leadingIcon = { Icon(Icons.Default.DarkMode, "Dark") }
                        )
                        DropdownMenuItem(
                            text = { Text("⚙️ System Default") },
                            onClick = {
                                viewModel.setThemeMode("System")
                                isThemeMenuExpanded = false
                            },
                            leadingIcon = { Icon(Icons.Default.SettingsSuggest, "System") }
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            )
        )

        // Visual Banner Card for Premium Visual Appeal
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
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
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.75f),
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.85f)
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                // Decorative abstract circular background overlay
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = 40.dp, y = 40.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.08f),
                            shape = CircleShape
                        )
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .background(
                                color = Color.White.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(100.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = "Shield",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "SECURE OFFLINE LEDGER",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                    }

                    Text(
                        text = "Smart Transparency for Community Finances",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        lineHeight = 22.sp
                    )

                    Text(
                        text = "Scan payment screenshots to auto-verify contributions instantly with high precision.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f),
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
                        imageVector = Icons.Default.EventNote,
                        contentDescription = "No Events Icon",
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No ledger events started yet",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Click the '+' button below to create a festival, building pool, charity drive, or tournament tracker.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = "Active Ledger Events",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    items(events) { event ->
                        EventCardItem(
                            event = event,
                            onClick = { viewModel.selectEvent(event.id) },
                            onDelete = { viewModel.deleteEvent(event.id) }
                        )
                    }
                }
            }

            // Floating Action Button to Create Event
            FloatingActionButton(
                onClick = { viewModel.navigateTo(Screen.CreateEvent) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp)
                    .testTag("create_event_fab"),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create Event Button"
                )
            }
        }
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

    val accentColor = if (event.isPrivate) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("event_card_${event.id}")
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
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
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = event.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Created Date",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Created: $dateString",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete Event",
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

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
                                    MaterialTheme.colorScheme.primaryContainer,
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timelapse,
                                contentDescription = "Duration Icon",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Duration: ${event.duration}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.HourglassEmpty,
                                contentDescription = "Ongoing",
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Ongoing Ledger",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }

                    // Privacy Indicator
                    if (event.isPrivate) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Private Event",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Invite Only",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Public,
                                contentDescription = "Public Event",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Public Ledger",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
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
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("Create Ledger Event") },
            navigationIcon = {
                IconButton(onClick = { viewModel.navigateBack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            )
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Event Details",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Event Title
            item {
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        if (it.isNotBlank()) titleError = false
                    },
                    label = { Text("Event Title *") },
                    placeholder = { Text("e.g. Ganesh Festival 2026, Temple Fund") },
                    isError = titleError,
                    supportingText = {
                        if (titleError) {
                            Text("Event title is required", color = MaterialTheme.colorScheme.error)
                        } else {
                            Text("Required field")
                        }
                    },
                    leadingIcon = { Icon(Icons.Default.Celebration, "Celebration Icon") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("event_title_input"),
                    singleLine = true
                )
            }

            // Event Duration
            item {
                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it },
                    label = { Text("Event Duration (Optional)") },
                    placeholder = { Text("e.g. 5 Days, 1 Month, Ongoing") },
                    leadingIcon = { Icon(Icons.Default.Timelapse, "Timelapse Icon") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("event_duration_input"),
                    singleLine = true
                )
            }

            // Public / Private Toggle
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Invite-Only / Private Event",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Only invited members can contribute or view the event transactions.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = isPrivate,
                        onCheckedChange = { isPrivate = it },
                        modifier = Modifier.testTag("event_private_switch")
                    )
                }
            }

            // Custom fields section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Additional Custom Fields",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        IconButton(onClick = { isAddingCustomField = !isAddingCustomField }) {
                            Icon(
                                imageVector = if (isAddingCustomField) Icons.Default.Close else Icons.Default.AddCircle,
                                contentDescription = "Add custom parameter",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Text(
                        text = "Add custom info fields like Venue, Head Priest, Coordinator Name, Rules etc.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Form to add custom field
                    AnimatedVisibility(visible = isAddingCustomField) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                OutlinedTextField(
                                    value = customFieldNameInput,
                                    onValueChange = { customFieldNameInput = it },
                                    label = { Text("Field Name") },
                                    placeholder = { Text("e.g. Venue, Coordinator") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = customFieldValueInput,
                                    onValueChange = { customFieldValueInput = it },
                                    label = { Text("Field Value") },
                                    placeholder = { Text("e.g. Town Hall, Suresh Kumar") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        if (customFieldNameInput.isNotBlank() && customFieldValueInput.isNotBlank()) {
                                            customFieldsList.add(customFieldNameInput.trim() to customFieldValueInput.trim())
                                            customFieldNameInput = ""
                                            customFieldValueInput = ""
                                            isAddingCustomField = false
                                        }
                                    },
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
                            text = "No additional custom fields added yet.",
                            style = MaterialTheme.typography.bodySmall,
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
                                        MaterialTheme.colorScheme.surface,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = pair.first,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = pair.second,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                IconButton(onClick = { customFieldsList.removeAt(index) }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Delete custom field",
                                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Buttons
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
                        .height(52.dp)
                        .testTag("create_event_submit_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Create Event & Show on Dashboard", fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
    val invitedMap by viewModel.invitedMembers.collectAsStateWithLifecycle()
    val invitedList = invitedMap[eventId] ?: emptyList()

    // Dialog state controllers
    var isInviteDialogOpen by remember { mutableStateOf(false) }
    var isManualTransactionDialogOpen by remember { mutableStateOf(false) }
    var isReceiptScannerDialogOpen by remember { mutableStateOf(false) }

    // OCR result verifier modal
    var extractedReceiptToVerify by remember { mutableStateOf<EventViewModel.ParsedReceipt?>(null) }
    var isVerifyReceiptDialogOpen by remember { mutableStateOf(false) }

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
            val pattern = java.util.regex.Pattern.compile("\"([^\"]*)\":\"([^\"]*)\"")
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
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to list")
                    }
                },
                actions = {
                    IconButton(onClick = { isInviteDialogOpen = true }) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = "Invite people",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                )
            )
        },
        floatingActionButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Manual Entry Floating Button
                FloatingActionButton(
                    onClick = { isManualTransactionDialogOpen = true },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    elevation = FloatingActionButtonDefaults.elevation(8.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Manual Entry")
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Big Upload Screenshot Floating Button (as shown in Sketch 3 at the bottom center)
                ExtendedFloatingActionButton(
                    onClick = { isReceiptScannerDialogOpen = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    elevation = FloatingActionButtonDefaults.elevation(8.dp),
                    modifier = Modifier.testTag("upload_screenshot_fab")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Upload Icon")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Upload Screenshot / Image", fontWeight = FontWeight.Bold)
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp), // Spacious bottom padding so FAB never overlaps items
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section 1: Financial Cards Header (Credits, Debits, Balance)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Ledger Balance Summary",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            // A cute visual pill indicating "Active" status
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(100.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Live Sync",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Total Collected",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.65f)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "₹${String.format(Locale.getDefault(), "%,.2f", creditAmount)}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Spent / Debited",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.65f)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "₹${String.format(Locale.getDefault(), "%,.2f", debitAmount)}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

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
                                        text = "Budget Utilized",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                    )
                                    Text(
                                        text = percentageString,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                LinearProgressIndicator(
                                    progress = { ratio },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
                                            shape = RoundedCornerShape(100.dp)
                                        ),
                                    color = if (ratio > 0.85f) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onPrimaryContainer,
                                    trackColor = Color.Transparent
                                )
                            }
                        } else {
                            Text(
                                text = "Start collecting contributions to see utilization stats.",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f),
                                fontStyle = FontStyle.Italic
                            )
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.15f)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (balance >= 0) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                                    contentDescription = "Balance Indicator",
                                    tint = if (balance >= 0) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Available Balance",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            Text(
                                text = "₹${String.format(Locale.getDefault(), "%,.2f", balance)}",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Black,
                                color = if (balance >= 0) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.error
                            )
                        }

                        // Displaying optional custom fields inside the Ledger summary card as requested in Screen 3!
                        if (customFieldsMap.isNotEmpty()) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 12.dp),
                                thickness = 0.5.dp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f)
                            )
                            Text(
                                text = "Event Details & Custom Info",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
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
                                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.08f),
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .padding(horizontal = 8.dp, vertical = 6.dp)
                                        ) {
                                            Column {
                                                Text(
                                                    text = entry.key,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = entry.value,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = MaterialTheme.colorScheme.onPrimaryContainer
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

            // Section 2: Important Smart Guidelines (User's Exact Handwritten Wording from Screen 3)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f)
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.DocumentScanner,
                                contentDescription = "Scanner",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "💡 Image Data Extraction Guide",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "This upload screenshot is the most powerful feature for extracting payment data from Google Pay, PhonePe, Paytm, or Amazon Pay images or transaction URLs.",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.85f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Note: People must upload a clean, high-resolution payment screenshot. Otherwise, the app cannot accept or parse it accurately.",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }

            // Section 3: Invited Members list
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "👥 Invited Members (${invitedList.size})",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        TextButton(onClick = { isInviteDialogOpen = true }) {
                            Icon(Icons.Default.Add, contentDescription = "Invite")
                            Text("Invite", fontSize = 12.sp)
                        }
                    }

                    if (invitedList.isEmpty()) {
                        Text(
                            text = "No one invited yet. Click 'Invite' to register community members (Organizer, Treasurer, Donor, Debtor/Creditor). Only invited members or public entries show in ledger.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            invitedList.forEach { member ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            MaterialTheme.colorScheme.surface,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
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
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = member.name,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Text(
                                                text = "📞 ${member.phone}  |  ✉️ ${member.email}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }

                                    Text(
                                        text = member.role,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = when (member.role) {
                                            "Organizer" -> MaterialTheme.colorScheme.error
                                            "Treasurer" -> MaterialTheme.colorScheme.secondary
                                            else -> MaterialTheme.colorScheme.primary
                                        },
                                        modifier = Modifier
                                            .background(
                                                when (member.role) {
                                                    "Organizer" -> MaterialTheme.colorScheme.errorContainer
                                                    "Treasurer" -> MaterialTheme.colorScheme.secondaryContainer
                                                    else -> MaterialTheme.colorScheme.primaryContainer
                                                },
                                                RoundedCornerShape(4.dp)
                                            )
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
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
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            if (transactions.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ReceiptLong,
                                contentDescription = "Empty Ledger",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Ledger is empty",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Use receipt scanner or manual entry to add donations or debit expenses.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(transactions) { tx ->
                    TransactionItem(
                        tx = tx,
                        onDelete = { viewModel.deleteTransaction(tx.id) }
                    )
                }
            }
        }
    }

    // DIALOGS & SHEET MODALS

    // 1. Invite Members Dialog (Includes Secure WhatsApp Link Generator & local registering)
    if (isInviteDialogOpen) {
        val context = LocalContext.current
        var inviteName by remember { mutableStateOf("") }
        var invitePhone by remember { mutableStateOf("") }
        var inviteEmail by remember { mutableStateOf("") }
        var inviteRole by remember { mutableStateOf("Donor") } // Donor, Debtor, Creditor, Organizer, Treasurer
        var nameErr by remember { mutableStateOf(false) }

        // Tabs: 0 -> Manual Register, 1 -> WhatsApp Link Generator
        var selectedInviteTab by remember { mutableStateOf(1) } // Default to the main secure link feature!

        // Secure Link generation states
        var linkDurationHours by remember { mutableStateOf("2 Hours") } // 1 Hour, 2 Hours, 12 Hours, 24 Hours, Never
        var maxClickLimit by remember { mutableStateOf("Unlimited") } // Unlimited, 1 Member, 5 Members, 10 Members, 25 Members

        val clicksMap by viewModel.linkClicks.collectAsStateWithLifecycle()
        val currentClicks = clicksMap[currentEvent.id] ?: viewModel.getLinkClicks(currentEvent.id)

        // Generate values
        val durationMs = when (linkDurationHours) {
            "1 Hour" -> 3600000L
            "2 Hours" -> 2 * 3600000L
            "12 Hours" -> 12 * 3600000L
            "24 Hours" -> 24 * 3600000L
            else -> 0L
        }
        val expiryVal = if (durationMs == 0L) 0L else (System.currentTimeMillis() + durationMs)
        val maxClicksVal = when (maxClickLimit) {
            "1 Member" -> 1
            "5 Members" -> 5
            "10 Members" -> 10
            "25 Members" -> 25
            else -> 999999
        }

        val sig = viewModel.generateSignature(currentEvent.id, expiryVal, maxClicksVal)
        // Secure Web URL to pass over WhatsApp
        val generatedLink = "https://communityledger.com/join?eventId=${currentEvent.id}&expiry=$expiryVal&maxClicks=$maxClicksVal&signature=$sig&title=${android.net.Uri.encode(currentEvent.title)}"

        AlertDialog(
            onDismissRequest = { isInviteDialogOpen = false },
            title = {
                Column {
                    Text("Add People to Ledger", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Styled segmented tabs for switching modes
                    TabRow(
                        selectedTabIndex = selectedInviteTab,
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth().height(40.dp)
                    ) {
                        Tab(
                            selected = selectedInviteTab == 0,
                            onClick = { selectedInviteTab = 0 },
                            text = { Text("📝 Local Register", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                        )
                        Tab(
                            selected = selectedInviteTab == 1,
                            onClick = { selectedInviteTab = 1 },
                            text = { Text("🔗 Secure Link", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                        )
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (selectedInviteTab == 0) {
                        // LOCAL REGISTER TAB
                        Text(
                            text = "Register a person manually on this device. Once registered, they can donate, request credit, or record debit expenses.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        OutlinedTextField(
                            value = inviteName,
                            onValueChange = {
                                inviteName = it
                                if (it.isNotBlank()) nameErr = false
                            },
                            label = { Text("Person Name *") },
                            isError = nameErr,
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = invitePhone,
                            onValueChange = { invitePhone = it },
                            label = { Text("Phone Number") },
                            placeholder = { Text("e.g. 9876543210") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = inviteEmail,
                            onValueChange = { inviteEmail = it },
                            label = { Text("Email Address") },
                            placeholder = { Text("e.g. ravi@gmail.com") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Role select
                        Text(
                            text = "Register Role",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            listOf("Donor", "Debtor", "Creditor", "Organizer").forEach { role ->
                                FilterChip(
                                    selected = inviteRole == role,
                                    onClick = { inviteRole = role },
                                    label = { Text(role, fontSize = 11.sp) }
                                )
                            }
                        }
                    } else {
                        // SECURE WHATSAPP LINK GENERATOR TAB
                        Text(
                            text = "Generate a secure, tamper-proof WhatsApp invitation link. Advanced Privacy Guard signs this link. Expiry prevents wrong people from joining.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        // Expiry configurations
                        Text(
                            text = "⏳ Link Expiration Time",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            listOf("1 Hour", "2 Hours", "12 Hours", "Never").forEach { hours ->
                                FilterChip(
                                    selected = linkDurationHours == hours,
                                    onClick = { linkDurationHours = hours },
                                    label = { Text(hours, fontSize = 10.sp) }
                                )
                            }
                        }

                        // Member click limit configurations
                        Text(
                            text = "👥 Maximum Join Limit",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            listOf("Unlimited", "1 Member", "5 Members", "10 Members").forEach { limit ->
                                FilterChip(
                                    selected = maxClickLimit == limit,
                                    onClick = { maxClickLimit = limit },
                                    label = { Text(limit, fontSize = 10.sp) }
                                )
                            }
                        }

                        // Live Click Tracker Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Group,
                                    contentDescription = "Clicks count",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Active Link Usage Tracking",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Text(
                                        text = "Total clicked & joined: $currentClicks people",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Sharing Actions Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Copy Link Button
                            OutlinedButton(
                                onClick = {
                                    val clipboardManager = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                    val clipData = android.content.ClipData.newPlainText("Ledger Link", generatedLink)
                                    clipboardManager.setPrimaryClip(clipData)
                                    android.widget.Toast.makeText(context, "Secure link copied!", android.widget.Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Copy Link", fontSize = 12.sp)
                            }

                            // Share on WhatsApp Button
                            Button(
                                onClick = {
                                    val shareIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        val expiryMsg = if (linkDurationHours == "Never") "unlimited access" else "expires in $linkDurationHours"
                                        putExtra(android.content.Intent.EXTRA_TEXT, "Hey! You've been invited to join the shared ledger event '${currentEvent.title}'. Click this secure link ($expiryMsg) to view/edit: $generatedLink")
                                    }
                                    try {
                                        val whatsappIntent = android.content.Intent(shareIntent).apply {
                                            setPackage("com.whatsapp")
                                        }
                                        context.startActivity(whatsappIntent)
                                    } catch (e: Exception) {
                                        // Fallback to standard share chooser
                                        context.startActivity(android.content.Intent.createChooser(shareIntent, "Share Ledger Link"))
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Share, contentDescription = "WhatsApp", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Send Link", fontSize = 12.sp)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                if (selectedInviteTab == 0) {
                    Button(
                        onClick = {
                            if (inviteName.isBlank()) {
                                nameErr = true
                            } else {
                                viewModel.invitePerson(
                                    eventId = eventId,
                                    name = inviteName.trim(),
                                    phone = invitePhone.trim(),
                                    email = inviteEmail.trim(),
                                    role = inviteRole
                                )
                                isInviteDialogOpen = false
                            }
                        }
                    ) {
                        Text("Register")
                    }
                } else {
                    Button(onClick = { isInviteDialogOpen = false }) {
                        Text("Close")
                    }
                }
            },
            dismissButton = {
                if (selectedInviteTab == 0) {
                    TextButton(onClick = { isInviteDialogOpen = false }) {
                        Text("Cancel")
                    }
                }
            }
        )
    }

    // 2. Manual Transaction Entry Dialog
    if (isManualTransactionDialogOpen) {
        var pName by remember { mutableStateOf("") }
        var pPhone by remember { mutableStateOf("") }
        var pEmail by remember { mutableStateOf("") }
        var amtInput by remember { mutableStateOf("") }
        var txType by remember { mutableStateOf("Donated") } // Donated, Credit, Debit, Expense
        var txnId by remember { mutableStateOf("") }
        var notesInput by remember { mutableStateOf("") }

        var amtErr by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { isManualTransactionDialogOpen = false },
            title = { Text("Add Ledger Entry Manually") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    // Type select
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf("Donated", "Expense", "Credit", "Debit").forEach { type ->
                            FilterChip(
                                selected = txType == type,
                                onClick = { txType = type },
                                label = { Text(type, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = if (type == "Donated" || type == "Credit")
                                        MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer
                                )
                            )
                        }
                    }

                    OutlinedTextField(
                        value = amtInput,
                        onValueChange = {
                            amtInput = it
                            if (it.toDoubleOrNull() != null) amtErr = false
                        },
                        label = { Text("Amount (INR ₹) *") },
                        isError = amtErr,
                        placeholder = { Text("e.g. 1500") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = pName,
                        onValueChange = { pName = it },
                        label = { Text("Person Name") },
                        placeholder = { Text("e.g. Ravi Kumar") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = pPhone,
                        onValueChange = { pPhone = it },
                        label = { Text("Phone Number") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = pEmail,
                        onValueChange = { pEmail = it },
                        label = { Text("Email Address") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = txnId,
                        onValueChange = { txnId = it },
                        label = { Text("Transaction / Reference ID") },
                        placeholder = { Text("e.g. UPI Ref 3107...") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = notesInput,
                        onValueChange = { notesInput = it },
                        label = { Text("Expense Details / Notes") },
                        placeholder = { Text("e.g. Flower decorations, GPay transfer") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsedAmt = amtInput.toDoubleOrNull()
                        if (parsedAmt == null || parsedAmt <= 0) {
                            amtErr = true
                        } else {
                            viewModel.addTransaction(
                                eventId = eventId,
                                personName = pName.trim().ifBlank { "Anonymous Donor" },
                                personPhone = pPhone.trim(),
                                personEmail = pEmail.trim(),
                                amount = parsedAmt,
                                type = txType,
                                notes = notesInput.trim().ifBlank { null },
                                transactionId = txnId.trim()
                            )
                            isManualTransactionDialogOpen = false
                        }
                    }
                ) {
                    Text("Add Entry")
                }
            },
            dismissButton = {
                TextButton(onClick = { isManualTransactionDialogOpen = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // 3. Simulated Receipt OCR Picker Dialog (Includes Templates for Google Pay, PhonePe, Paytm, Amazon Pay)
    if (isReceiptScannerDialogOpen) {
        val simulatedReceipts = listOf(
            Triple(
                "Google Pay (GPay) Receipt",
                "GPay successful donation. User Banoth Gopi is registered member.",
                "Google Pay\nPayment successful\nTo: Ganesh Festival Committee\nAmount: ₹1,500.00\nUPI Ref No: 310725987654\nFrom: banothgopikrishna19@gmail.com\nPhone: 9848022338\nDate: 26 June 2026, 11:30 AM"
            ),
            Triple(
                "PhonePe Receipt",
                "PhonePe template, extracts phone and date automatically.",
                "PhonePe\nTransaction Successful\nPaid to: Kanaka Durga Temple Fund\nAmount: ₹5,000.00\nTxn ID: T260626123456789\nSender Phone: 8123456789\nDate: 26 Jun 2026"
            ),
            Triple(
                "Paytm Receipt",
                "Paytm template, demonstrates instant offline regex extraction.",
                "Paytm UPI\nSent ₹ 750.00 successfully\nTo: Blood Donation Camp Organizer\nRef No: 345612890432\nSender Email: ravi@paytm.com\nSender Phone: 7654321098\nDate: 25 June 2026"
            ),
            Triple(
                "Amazon Pay Receipt",
                "Amazon Pay template showing debit/expense purchase receipt.",
                "Amazon Pay\nPayment processed successfully\nAmount: ₹ 3,200\nUPI ID: amzn@upi\nSender Name: Suresh Kumar\nSender Phone: 9000112233\nTransaction Reference: 987654321098\nDetails: Flowers and sound system rental"
            )
        )

        AlertDialog(
            onDismissRequest = { isReceiptScannerDialogOpen = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DocumentScanner, "Scanner", tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Select Screenshot / Upload Receipt")
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Because OCR runs directly on payment apps screenshots, you can simulate a receipt upload below to see our deterministic regex OCR engine automatically extract details instantly!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Select high-quality screenshot templates:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.heightIn(max = 280.dp)
                    ) {
                        items(simulatedReceipts) { (title, desc, rawText) ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        // Run parsing engine
                                        val parsed = viewModel.parseReceiptText(rawText)
                                        extractedReceiptToVerify = parsed
                                        isReceiptScannerDialogOpen = false
                                        isVerifyReceiptDialogOpen = true
                                    },
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = when {
                                            title.contains("Google") -> Icons.Default.AddCard
                                            title.contains("Phone") -> Icons.Default.SendToMobile
                                            title.contains("Paytm") -> Icons.Default.Payments
                                            else -> Icons.Default.ShoppingCart
                                        },
                                        contentDescription = "App Icon",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                        Text(desc, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "⚠️ Bank SMS screenshots are not accepted because of fraud prevention.",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { isReceiptScannerDialogOpen = false }) {
                    Text("Close")
                }
            }
        )
    }

    // 4. Verification Dialog (Mandated Confirmation Dialogue)
    if (isVerifyReceiptDialogOpen && extractedReceiptToVerify != null) {
        val parsed = extractedReceiptToVerify!!

        var vName by remember { mutableStateOf(
            // Try to match registered members with parsed phone/email
            invitedList.firstOrNull { it.phone == parsed.phone || it.email == parsed.email }?.name 
                ?: (if (parsed.phone.isNotBlank()) "User (${parsed.phone.takeLast(4)})" else "Anonymous Donor")
        ) }
        var vPhone by remember { mutableStateOf(parsed.phone) }
        var vEmail by remember { mutableStateOf(parsed.email) }
        var vAmount by remember { mutableStateOf(parsed.amount.toString()) }
        var vTxId by remember { mutableStateOf(parsed.transactionId) }
        var vType by remember { mutableStateOf(if (parsed.paymentApp == "Amazon Pay") "Expense" else "Donated") } // Donated vs Expense
        var vNotes by remember { mutableStateOf("Extracted via ${parsed.paymentApp} OCR on ${parsed.date}") }

        var amtErr by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { isVerifyReceiptDialogOpen = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.VerifiedUser, "Verify", tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Verify Extracted Receipt Details")
                }
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "OCR successfully extracted details offline! Please verify or edit any field below. Since OCR is never 100% accurate, user verification is MANDATORY.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Autodetected badge
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.AppSettingsAlt, "App", tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Detected channel: ${parsed.paymentApp}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    OutlinedTextField(
                        value = vAmount,
                        onValueChange = {
                            vAmount = it
                            if (it.toDoubleOrNull() != null) amtErr = false
                        },
                        label = { Text("Extracted Amount (INR ₹) *") },
                        isError = amtErr,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Dropdown or autocomplete match registered member
                    Text(
                        text = "Match Member",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    if (invitedList.isNotEmpty()) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            invitedList.forEach { member ->
                                FilterChip(
                                    selected = vName == member.name,
                                    onClick = {
                                        vName = member.name
                                        vPhone = member.phone
                                        vEmail = member.email
                                    },
                                    label = { Text(member.name, fontSize = 10.sp) }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = vName,
                        onValueChange = { vName = it },
                        label = { Text("Sender Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = vPhone,
                        onValueChange = { vPhone = it },
                        label = { Text("Phone Number") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = vEmail,
                        onValueChange = { vEmail = it },
                        label = { Text("Email Address") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = vTxId,
                        onValueChange = { vTxId = it },
                        label = { Text("Transaction Reference ID") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = vNotes,
                        onValueChange = { vNotes = it },
                        label = { Text("Ledger Entry Notes") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Type Selection (Donation vs Expense Debit)
                    Text(
                        text = "Post as Ledger Entry Type",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Donated", "Expense", "Credit", "Debit").forEach { type ->
                            FilterChip(
                                selected = vType == type,
                                onClick = { vType = type },
                                label = { Text(type, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = if (type == "Donated" || type == "Credit")
                                        MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer
                                )
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsedAmt = vAmount.toDoubleOrNull()
                        if (parsedAmt == null || parsedAmt <= 0) {
                            amtErr = true
                        } else {
                            viewModel.addTransaction(
                                eventId = eventId,
                                personName = vName,
                                personPhone = vPhone,
                                personEmail = vEmail,
                                amount = parsedAmt,
                                type = vType,
                                notes = vNotes,
                                transactionId = vTxId
                            )
                            isVerifyReceiptDialogOpen = false
                        }
                    },
                    modifier = Modifier.testTag("verify_receipt_confirm_button")
                ) {
                    Text("Confirm & Post to Ledger")
                }
            },
            dismissButton = {
                TextButton(onClick = { isVerifyReceiptDialogOpen = false }) {
                    Text("Reject / Edit")
                }
            }
        )
    }
}

@Composable
fun TransactionItem(
    tx: TransactionEntity,
    onDelete: () -> Unit
) {
    val formattedDate = remember(tx.date) {
        val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
        sdf.format(Date(tx.date))
    }

    val isCredit = tx.type == "Donated" || tx.type == "Credit"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            1.dp,
            if (isCredit) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
            else MaterialTheme.colorScheme.error.copy(alpha = 0.15f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
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
                        .size(36.dp)
                        .background(
                            if (isCredit) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.errorContainer,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isCredit) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                        contentDescription = "Tx Type",
                        tint = if (isCredit) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = tx.personName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
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
                    if (!tx.notes.isNullOrBlank()) {
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
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(top = 2.dp)
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
                    color = if (isCredit) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )

                Text(
                    text = tx.type,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isCredit) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .background(
                            if (isCredit) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer,
                            RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                )

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Delete entry",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

// Simple composable state holder for dialog scrollability
@Composable
fun rememberScrollState() = androidx.compose.foundation.rememberScrollState()
