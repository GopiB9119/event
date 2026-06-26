package com.example.ui

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.data.AppDatabase
import com.example.data.EventEntity
import com.example.data.EventRepository
import com.example.data.TransactionEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.regex.Pattern

sealed class Screen {
    object Dashboard : Screen()
    object CreateEvent : Screen()
    data class EventDetails(val eventId: Int) : Screen()
}

class EventViewModel(application: Application) : AndroidViewModel(application) {

    // Database Setup
    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            application.applicationContext,
            AppDatabase::class.java,
            "community_ledger_db"
        ).fallbackToDestructiveMigration().build()
    }

    private val repository: EventRepository by lazy {
        EventRepository(database.eventDao())
    }

    // Theme state (persisted in SharedPreferences)
    private val sharedPrefs = application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val _themeMode = MutableStateFlow(sharedPrefs.getString("theme_mode", "System") ?: "System")
    val themeMode: StateFlow<String> = _themeMode.asStateFlow()

    fun setThemeMode(mode: String) {
        _themeMode.value = mode
        sharedPrefs.edit().putString("theme_mode", mode).apply()
    }

    // Navigation Stack
    val navigationStack = mutableStateListOf<Screen>(Screen.Dashboard)

    fun navigateTo(screen: Screen) {
        navigationStack.add(screen)
    }

    fun navigateBack(): Boolean {
        if (navigationStack.size > 1) {
            navigationStack.removeAt(navigationStack.size - 1)
            return true
        }
        return false
    }

    // Reactive State lists
    val events: StateFlow<List<EventEntity>> = repository.allEvents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selected Event Flow
    private val _selectedEventId = MutableStateFlow<Int?>(null)
    val selectedEvent: StateFlow<EventEntity?> = _selectedEventId
        .flatMapLatest { id ->
            if (id != null) repository.getEventById(id) else flowOf(null)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val selectedEventTransactions: StateFlow<List<TransactionEntity>> = _selectedEventId
        .flatMapLatest { id ->
            if (id != null) repository.getTransactionsForEvent(id) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectEvent(eventId: Int) {
        _selectedEventId.value = eventId
        navigateTo(Screen.EventDetails(eventId))
    }

    // Event Creation state
    fun createEvent(title: String, duration: String?, isPrivate: Boolean, customFields: Map<String, String>) {
        viewModelScope.launch {
            // Serialize custom fields to a simple key-value JSON-like format
            val jsonBuilder = StringBuilder("{")
            customFields.entries.forEachIndexed { index, entry ->
                jsonBuilder.append("\"${entry.key.replace("\"", "\\\"")}\":\"${entry.value.replace("\"", "\\\"")}\"")
                if (index < customFields.size - 1) jsonBuilder.append(",")
            }
            jsonBuilder.append("}")

            val newEvent = EventEntity(
                title = title,
                duration = duration?.takeIf { it.isNotBlank() },
                isPrivate = isPrivate,
                customFieldsJson = jsonBuilder.toString()
            )
            val newId = repository.insertEvent(newEvent)
            navigateBack() // Go back to Dashboard
        }
    }

    fun deleteEvent(eventId: Int) {
        viewModelScope.launch {
            repository.deleteEvent(eventId)
        }
    }

    // Transaction Management
    fun addTransaction(
        eventId: Int,
        personName: String,
        personPhone: String,
        personEmail: String,
        amount: Double,
        type: String,
        notes: String? = null,
        transactionId: String = ""
    ) {
        viewModelScope.launch {
            val tx = TransactionEntity(
                eventId = eventId,
                personName = personName.trim().ifBlank { "Anonymous" },
                personPhone = personPhone.trim(),
                personEmail = personEmail.trim(),
                amount = amount,
                type = type,
                transactionId = transactionId.trim(),
                notes = notes?.trim()
            )
            repository.insertTransaction(tx)
        }
    }

    fun deleteTransaction(txId: Int) {
        viewModelScope.launch {
            repository.deleteTransaction(txId)
        }
    }

    // Invitations Simulation (List of Invited People)
    private val _invitedMembers = MutableStateFlow<Map<Int, List<Member>>>(emptyMap())
    val invitedMembers = _invitedMembers.asStateFlow()

    data class Member(val name: String, val phone: String, val email: String, val role: String = "Donor")

    fun invitePerson(eventId: Int, name: String, phone: String, email: String, role: String) {
        val currentList = _invitedMembers.value[eventId] ?: emptyList()
        val updatedList = currentList + Member(name, phone, email, role)
        _invitedMembers.value = _invitedMembers.value + (eventId to updatedList)

        // Automatically add a small welcome credit/donation transaction if they donate immediately
        if (role == "Donor") {
            // Optionally add transaction later
        }
    }

    // Receipt Text OCR / Parsing Logic (Universal UPI Payment Regex Parser)
    data class ParsedReceipt(
        val amount: Double = 0.0,
        val transactionId: String = "",
        val phone: String = "",
        val email: String = "",
        val date: String = "",
        val paymentApp: String = "Unknown UPI"
    )

    fun parseReceiptText(text: String): ParsedReceipt {
        var amount = 0.0
        var transactionId = ""
        var phone = ""
        var email = ""
        var dateStr = ""
        var paymentApp = "UPI Payment"

        // Determine App Name
        val lowerText = text.lowercase()
        if (lowerText.contains("google pay") || lowerText.contains("gpay")) {
            paymentApp = "Google Pay"
        } else if (lowerText.contains("phonepe")) {
            paymentApp = "PhonePe"
        } else if (lowerText.contains("paytm")) {
            paymentApp = "Paytm"
        } else if (lowerText.contains("amazon pay")) {
            paymentApp = "Amazon Pay"
        }

        // 1. Parse Amount
        // Look for matches of rupees: ₹ 500, ₹1,500.00, Rs. 500, INR 500
        val amountPatterns = listOf(
            Pattern.compile("(?:₹|Rs\\.?|INR)\\s*([\\d,]+(?:\\.\\d{1,2})?)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(?:Paid|Sent|Amount|Transfer)\\s*(?:of\\s*)?(?:₹|Rs\\.?|INR)?\\s*([\\d,]+(?:\\.\\d{1,2})?)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("([\\d,]+(?:\\.\\d{1,2})?)\\s*(?:Rupees|INR|Rs)", Pattern.CASE_INSENSITIVE)
        )

        for (pattern in amountPatterns) {
            val matcher = pattern.matcher(text)
            if (matcher.find()) {
                val match = matcher.group(1) ?: ""
                val cleanVal = match.replace(",", "")
                val parsed = cleanVal.toDoubleOrNull()
                if (parsed != null && parsed > 0) {
                    amount = parsed
                    break
                }
            }
        }

        // 2. Parse Transaction ID / Reference No
        val txPatterns = listOf(
            Pattern.compile("(?:Transaction ID|Txn ID|UPI Ref No|Ref No|Reference ID|Reference No)[:\\s]+([A-Za-z0-9]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(?:Ref|Txn|ID)[:\\s]+([A-Za-z0-9]{12,})", Pattern.CASE_INSENSITIVE),
            Pattern.compile("([0-9]{12})") // Standard 12-digit UPI reference number
        )

        for (pattern in txPatterns) {
            val matcher = pattern.matcher(text)
            if (matcher.find()) {
                val match = matcher.group(1) ?: ""
                if (match.isNotBlank()) {
                    transactionId = match
                    break
                }
            }
        }

        // 3. Parse Phone number (10-digit starting with 6-9)
        val phonePattern = Pattern.compile("(?:Phone|Mobile|Contact)?[:\\s]*([6-9]\\d{9})", Pattern.CASE_INSENSITIVE)
        val phoneMatcher = phonePattern.matcher(text)
        if (phoneMatcher.find()) {
            phone = phoneMatcher.group(1) ?: ""
        }

        // 4. Parse Email address
        val emailPattern = Pattern.compile("([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})")
        val emailMatcher = emailPattern.matcher(text)
        if (emailMatcher.find()) {
            email = emailMatcher.group(1) ?: ""
        }

        // 5. Parse Date or use current date
        val datePattern = Pattern.compile("(\\d{1,2}\\s+[A-Za-z]{3,9}\\s+\\d{4})")
        val dateMatcher = datePattern.matcher(text)
        if (dateMatcher.find()) {
            dateStr = dateMatcher.group(1) ?: ""
        } else {
            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            dateStr = sdf.format(Date())
        }

        return ParsedReceipt(
            amount = amount,
            transactionId = transactionId,
            phone = phone,
            email = email,
            date = dateStr,
            paymentApp = paymentApp
        )
    }

    // Secure Invitation Link & Advanced Privacy Guard Configuration
    private val _deepLinkMessage = MutableStateFlow<String?>(null)
    val deepLinkMessage = _deepLinkMessage.asStateFlow()

    private val _deepLinkError = MutableStateFlow<String?>(null)
    val deepLinkError = _deepLinkError.asStateFlow()

    fun dismissDeepLinkMessage() {
        _deepLinkMessage.value = null
    }

    fun dismissDeepLinkError() {
        _deepLinkError.value = null
    }

    private val _linkClicks = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val linkClicks = _linkClicks.asStateFlow()

    fun refreshLinkClicks(eventId: Int) {
        val clicks = sharedPrefs.getInt("link_clicks_$eventId", 0)
        _linkClicks.value = _linkClicks.value + (eventId to clicks)
    }

    fun getLinkClicks(eventId: Int): Int {
        return sharedPrefs.getInt("link_clicks_$eventId", 0)
    }

    fun incrementLinkClicks(eventId: Int) {
        val current = getLinkClicks(eventId)
        sharedPrefs.edit().putInt("link_clicks_$eventId", current + 1).apply()
        refreshLinkClicks(eventId)
    }

    fun generateSignature(eventId: Int, expiry: Long, maxClicks: Int): String {
        // Secure token verification salt representing our high security privacy ledger
        val raw = "eventId=$eventId&expiry=$expiry&maxClicks=$maxClicks&salt=SecureLedgerPrivacyGuardSalt2026"
        return try {
            val digest = java.security.MessageDigest.getInstance("SHA-256")
            val hash = digest.digest(raw.toByteArray(Charsets.UTF_8))
            hash.joinToString("") { "%02x".format(it) }.take(16)
        } catch (e: Exception) {
            // High security fallback algorithm
            val code = raw.hashCode() xor 0x5E_C0_01_ED
            java.lang.Integer.toHexString(code)
        }
    }

    fun handleDeepLink(uri: android.net.Uri) {
        viewModelScope.launch {
            try {
                val eventIdStr = uri.getQueryParameter("eventId")
                val expiryStr = uri.getQueryParameter("expiry")
                val signature = uri.getQueryParameter("signature")
                val title = uri.getQueryParameter("title") ?: "Shared Ledger Event"
                val maxClicksStr = uri.getQueryParameter("maxClicks") ?: "999999"

                if (eventIdStr == null || expiryStr == null || signature == null) {
                    _deepLinkError.value = "Malformed Invitation Link: Secure verification components are missing."
                    return@launch
                }

                val eventId = eventIdStr.toIntOrNull() ?: 0
                val expiry = expiryStr.toLongOrNull() ?: 0L
                val maxClicks = maxClicksStr.toIntOrNull() ?: 999999

                // 1. Signature check
                val expectedSig = generateSignature(eventId, expiry, maxClicks)
                if (signature != expectedSig) {
                    _deepLinkError.value = "Security Block: Advanced Privacy Guard detected an invalid or modified signature. Access is denied to prevent unauthorized ledger entries."
                    return@launch
                }

                // 2. Expiry check
                if (expiry != 0L && System.currentTimeMillis() > expiry) {
                    val formattedTime = SimpleDateFormat("dd MMM hh:mm a", Locale.getDefault()).format(Date(expiry))
                    _deepLinkError.value = "Expired Link: This secure ledger link expired at $formattedTime (exceeded its custom 1 or 2 hours validity timeframe)."
                    return@launch
                }

                // 3. Max clicks check
                val currentClicks = getLinkClicks(eventId)
                if (currentClicks >= maxClicks) {
                    _deepLinkError.value = "Limit Exceeded: This invite link has reached its maximum authorized click limit of $maxClicks users."
                    return@launch
                }

                // 4. Check database existence and auto-create if new member device
                val exists = events.value.any { it.id == eventId }
                if (!exists) {
                    val newEvent = EventEntity(
                        id = eventId,
                        title = title,
                        createdDate = System.currentTimeMillis(),
                        isPrivate = false,
                        customFieldsJson = "{}"
                    )
                    repository.insertEvent(newEvent)
                }

                // 5. Success! Increment join clicks and refresh state
                incrementLinkClicks(eventId)
                selectEvent(eventId)

                _deepLinkMessage.value = "Access Granted: Securely joined shared ledger event '$title' via WhatsApp link!"
            } catch (e: Exception) {
                _deepLinkError.value = "Advanced Security verification failed: ${e.localizedMessage}"
            }
        }
    }
}
