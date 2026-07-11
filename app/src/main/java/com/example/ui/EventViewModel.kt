package com.example.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.BuildConfig
import com.example.data.AppDatabase
import com.example.data.EventEntity
import com.example.data.MemberEntity
import com.example.data.EventRepository
import com.example.data.TransactionEntity
import com.example.data.isValidLedgerTransaction
import com.example.data.normalizeLocalIdentity
import com.example.receipt.ParsedReceipt
import com.example.receipt.ReceiptParser
import com.example.update.UpdateCheckResult
import com.example.update.UpdateChecker
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import org.json.JSONObject
import kotlin.coroutines.resume

sealed class Screen {
    object Dashboard : Screen()
    object CreateEvent : Screen()
    object TrustCenter : Screen()
    data class EventDetails(val eventId: Int) : Screen()
}

private const val PREF_RECEIPT_REVIEW_IN_PROGRESS = "receipt_review_in_progress"

class EventViewModel(application: Application) : AndroidViewModel(application) {

    private enum class OcrScript {
        LATIN,
        DEVANAGARI
    }


    // Database Setup
    private val database: AppDatabase by lazy {
        val isTest = try {
            Class.forName("org.robolectric.Robolectric")
            true
        } catch (e: Exception) {
            try {
                Class.forName("org.junit.Test")
                true
            } catch (e2: Exception) {
                false
            }
        }

        val builder = if (isTest) {
            Room.inMemoryDatabaseBuilder(
                application.applicationContext,
                AppDatabase::class.java
            ).allowMainThreadQueries()
        } else {
            Room.databaseBuilder(
                application.applicationContext,
                AppDatabase::class.java,
                "community_ledger_db"
            ).addMigrations(AppDatabase.MIGRATION_2_3, AppDatabase.MIGRATION_3_4)
        }
        builder.build()
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

    private val _localBetaAcknowledged = MutableStateFlow(
        sharedPrefs.getBoolean("local_beta_acknowledged", false)
    )
    val localBetaAcknowledged: StateFlow<Boolean> = _localBetaAcknowledged.asStateFlow()

    fun acknowledgeLocalBeta() {
        _localBetaAcknowledged.value = true
        sharedPrefs.edit().putBoolean("local_beta_acknowledged", true).apply()
    }

    private val _updateCheckResult = MutableStateFlow<UpdateCheckResult>(UpdateCheckResult.Idle)
    val updateCheckResult: StateFlow<UpdateCheckResult> = _updateCheckResult.asStateFlow()

    fun checkForUpdates() {
        if (_updateCheckResult.value == UpdateCheckResult.Checking) return
        _updateCheckResult.value = UpdateCheckResult.Checking
        viewModelScope.launch {
            _updateCheckResult.value = UpdateChecker.check(
                currentVersionCode = BuildConfig.VERSION_CODE,
                currentVersionName = BuildConfig.VERSION_NAME
            )
        }
    }

    private val _receiptReviewInterrupted = MutableStateFlow(
        sharedPrefs.getBoolean(PREF_RECEIPT_REVIEW_IN_PROGRESS, false)
    )
    val receiptReviewInterrupted: StateFlow<Boolean> = _receiptReviewInterrupted.asStateFlow()

    fun markReceiptReviewInProgress() {
        sharedPrefs.edit().putBoolean(PREF_RECEIPT_REVIEW_IN_PROGRESS, true).commit()
    }

    fun clearReceiptReviewInProgress() {
        sharedPrefs.edit().putBoolean(PREF_RECEIPT_REVIEW_IN_PROGRESS, false).commit()
        _receiptReviewInterrupted.value = false
    }

    // User Identity Configuration
    private val _userEmail = MutableStateFlow(
        normalizeLocalIdentity(sharedPrefs.getString("user_email", "").orEmpty()).orEmpty()
    )
    val userEmail: StateFlow<String> = _userEmail.asStateFlow()

    fun getMyUserEmail(): String {
        return _userEmail.value
    }

    fun setMyUserEmail(email: String): Boolean {
        val cleanEmail = normalizeLocalIdentity(email) ?: return false
        _userEmail.value = cleanEmail
        sharedPrefs.edit().putString("user_email", cleanEmail).apply()
        return true
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
    
    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedEvent: StateFlow<EventEntity?> = _selectedEventId
        .flatMapLatest { id ->
            if (id != null) repository.getEventById(id) else flowOf(null)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedEventTransactions: StateFlow<List<TransactionEntity>> = _selectedEventId
        .flatMapLatest { id ->
            if (id != null) repository.getTransactionsForEvent(id) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedEventMembers: StateFlow<List<MemberEntity>> = _selectedEventId
        .flatMapLatest { id ->
            if (id != null) repository.getMembersForEvent(id) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectEvent(eventId: Int) {
        _selectedEventId.value = eventId
        repairMemberLinksForEvent(eventId)
        navigateTo(Screen.EventDetails(eventId))
    }

    private fun repairMemberLinksForEvent(eventId: Int) {
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            val unlinkedTransactions = repository.getUnlinkedTransactionsForEvent(eventId)
            unlinkedTransactions.forEach { tx ->
                val memberId = resolveOrCreateMemberId(
                    eventId = eventId,
                    name = tx.personName,
                    phone = tx.personPhone,
                    email = tx.personEmail,
                    role = if (tx.type == "Expense" || tx.type == "Debit") "Vendor" else "Donor"
                )
                repository.updateTransactionMemberId(tx.id, memberId)
            }
        }
    }

    // Event Creation state
    fun createEvent(title: String, duration: String?, isPrivate: Boolean, customFields: Map<String, String>) {
        val creatorEmail = normalizeLocalIdentity(getMyUserEmail()) ?: return
        if (title.isBlank()) return
        viewModelScope.launch {
            val json = org.json.JSONObject()
            json.put("creatorEmail", creatorEmail)
            customFields.forEach { (k, v) ->
                json.put(k, v)
            }

            val newEvent = EventEntity(
                title = title,
                duration = duration?.takeIf { it.isNotBlank() },
                isPrivate = isPrivate,
                customFieldsJson = json.toString()
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
        transactionId: String = "",
        uploaderEmail: String = getMyUserEmail(),
        memberId: Int? = null
    ) {
        val cleanUploaderEmail = normalizeLocalIdentity(uploaderEmail) ?: return
        if (!isValidLedgerTransaction(eventId, amount, type)) return
        viewModelScope.launch {
            val tx = TransactionEntity(
                eventId = eventId,
                memberId = memberId,
                personName = personName.trim().ifBlank { "Anonymous" },
                personPhone = personPhone.trim(),
                personEmail = personEmail.trim(),
                amount = amount,
                type = type,
                transactionId = transactionId.trim(),
                notes = notes?.trim(),
                uploaderEmail = cleanUploaderEmail
            )
            repository.insertTransaction(tx)
        }
    }

    fun deleteTransaction(txId: Int) {
        viewModelScope.launch {
            repository.deleteTransaction(txId)
        }
    }

    fun replaceTransaction(
        txId: Int,
        eventId: Int,
        personName: String,
        personPhone: String,
        personEmail: String,
        amount: Double,
        type: String,
        notes: String? = null,
        transactionId: String = "",
        uploaderEmail: String = getMyUserEmail(),
        memberId: Int? = null
    ) {
        val cleanUploaderEmail = normalizeLocalIdentity(uploaderEmail) ?: return
        if (!isValidLedgerTransaction(eventId, amount, type)) return
        viewModelScope.launch {
            val tx = TransactionEntity(
                id = txId,
                eventId = eventId,
                memberId = memberId,
                personName = personName.trim().ifBlank { "Anonymous" },
                personPhone = personPhone.trim(),
                personEmail = personEmail.trim(),
                amount = amount,
                type = type,
                transactionId = transactionId.trim(),
                notes = notes?.trim(),
                uploaderEmail = cleanUploaderEmail
            )
            repository.insertTransaction(tx)
        }
    }

    fun addReceiptTransaction(
        eventId: Int,
        personName: String,
        personPhone: String,
        personEmail: String,
        amount: Double,
        type: String,
        notes: String? = null,
        transactionId: String = "",
        uploaderEmail: String = getMyUserEmail()
    ) {
        val cleanUploaderEmail = normalizeLocalIdentity(uploaderEmail) ?: return
        if (!isValidLedgerTransaction(eventId, amount, type)) return
        viewModelScope.launch {
            val cleanName = personName.trim().ifBlank { cleanUploaderEmail.substringBefore("@").ifBlank { "Anonymous" } }
            val cleanPhone = personPhone.trim()
            val cleanEmail = personEmail.trim()
            val memberId = resolveOrCreateMemberId(
                eventId = eventId,
                name = cleanName,
                phone = cleanPhone,
                email = cleanEmail,
                role = if (type == "Expense" || type == "Debit") "Vendor" else "Donor"
            )
            repository.insertTransaction(
                TransactionEntity(
                    eventId = eventId,
                    memberId = memberId,
                    personName = cleanName,
                    personPhone = cleanPhone,
                    personEmail = cleanEmail,
                    amount = amount,
                    type = type,
                    transactionId = transactionId.trim(),
                    notes = notes?.trim(),
                    uploaderEmail = cleanUploaderEmail
                )
            )
        }
    }

    fun replaceReceiptTransaction(
        txId: Int,
        eventId: Int,
        personName: String,
        personPhone: String,
        personEmail: String,
        amount: Double,
        type: String,
        notes: String? = null,
        transactionId: String = "",
        uploaderEmail: String = getMyUserEmail(),
        existingMemberId: Int? = null
    ) {
        val cleanUploaderEmail = normalizeLocalIdentity(uploaderEmail) ?: return
        if (!isValidLedgerTransaction(eventId, amount, type)) return
        viewModelScope.launch {
            val cleanName = personName.trim().ifBlank { cleanUploaderEmail.substringBefore("@").ifBlank { "Anonymous" } }
            val cleanPhone = personPhone.trim()
            val cleanEmail = personEmail.trim()
            val memberId = existingMemberId ?: resolveOrCreateMemberId(
                eventId = eventId,
                name = cleanName,
                phone = cleanPhone,
                email = cleanEmail,
                role = if (type == "Expense" || type == "Debit") "Vendor" else "Donor"
            )
            repository.insertTransaction(
                TransactionEntity(
                    id = txId,
                    eventId = eventId,
                    memberId = memberId,
                    personName = cleanName,
                    personPhone = cleanPhone,
                    personEmail = cleanEmail,
                    amount = amount,
                    type = type,
                    transactionId = transactionId.trim(),
                    notes = notes?.trim(),
                    uploaderEmail = cleanUploaderEmail
                )
            )
        }
    }

    fun saveReceiptJsonFile(
        eventId: Int,
        personName: String,
        uploaderEmail: String,
        receiptJsonText: String
    ): String? {
        val cleanUploaderEmail = normalizeLocalIdentity(uploaderEmail) ?: return null
        if (eventId <= 0) return null
        return try {
            val safePerson = safeFileSegment(personName.ifBlank { cleanUploaderEmail.substringBefore("@").ifBlank { "unknown" } })
            val safeUploader = safeFileSegment(cleanUploaderEmail)
            val receiptJson = JSONObject(receiptJsonText)
            receiptJson.put("eventId", eventId)
            receiptJson.put("storedAt", System.currentTimeMillis())

            val directory = java.io.File(
                getApplication<Application>().filesDir,
                "receipts/event_$eventId/person_$safePerson/uploader_$safeUploader"
            )
            if (!directory.exists()) {
                directory.mkdirs()
            }

            val txPart = receiptJson.optString("upiReferenceOrTransactionId").takeIf { it.isNotBlank() && it != "null" }
                ?: System.currentTimeMillis().toString()
            val file = java.io.File(directory, "receipt_${safeFileSegment(txPart)}.json")
            file.writeText(receiptJson.toString(2))
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun safeFileSegment(value: String): String {
        return value.trim()
            .lowercase(Locale.US)
            .replace(Regex("[^a-z0-9._-]+"), "_")
            .trim('_')
            .ifBlank { "unknown" }
            .take(80)
    }

    fun invitePerson(eventId: Int, name: String, phone: String, email: String, role: String) {
        viewModelScope.launch {
            resolveOrCreateMemberId(
                eventId = eventId,
                name = name.trim().ifBlank { "Unnamed Member" },
                phone = phone.trim(),
                email = email.trim(),
                role = role.ifBlank { "Donor" }
            )
        }
    }

    private suspend fun resolveOrCreateMemberId(
        eventId: Int,
        name: String,
        phone: String,
        email: String,
        role: String
    ): Int {
        val normalizedName = normalizeMemberName(name)
        val existing = repository.findMatchingMember(eventId, normalizedName, phone, email)
        if (existing != null) return existing.id

        return repository.insertMember(
            MemberEntity(
                eventId = eventId,
                name = name.trim().ifBlank { "Unnamed Member" },
                normalizedName = normalizedName,
                phone = phone.trim(),
                email = email.trim(),
                role = role.ifBlank { "Donor" }
            )
        ).toInt()
    }

    private fun normalizeMemberName(name: String): String {
        return name.trim()
            .lowercase(Locale.US)
            .replace(Regex("\\s+"), " ")
    }

    data class SharedReceipt(
        val imageUri: Uri? = null,
        val text: String = "",
        val sourcePackage: String = ""
    )

    private val _pendingSharedReceipt = MutableStateFlow<SharedReceipt?>(null)
    val pendingSharedReceipt = _pendingSharedReceipt.asStateFlow()

    fun clearPendingSharedReceipt() {
        _pendingSharedReceipt.value = null
    }

    fun handleSharedReceiptIntent(intent: Intent?) {
        if (intent == null) return
        val action = intent.action ?: return
        if (action != Intent.ACTION_SEND && action != Intent.ACTION_SEND_MULTIPLE) return

        val imageUri = extractSharedImageUri(intent)
        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT).orEmpty()
            .ifBlank { intent.getCharSequenceExtra(Intent.EXTRA_TEXT)?.toString().orEmpty() }

        if (imageUri != null || sharedText.isNotBlank()) {
            _pendingSharedReceipt.value = SharedReceipt(
                imageUri = imageUri,
                text = sharedText,
                sourcePackage = intent.`package`.orEmpty()
            )
        }
    }

    @Suppress("DEPRECATION")
    private fun extractSharedImageUri(intent: Intent): Uri? {
        val singleUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
        } else {
            intent.getParcelableExtra(Intent.EXTRA_STREAM) as? Uri
        }
        if (singleUri != null) return singleUri

        val multipleUris = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM, Uri::class.java)
        } else {
            intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM)
        }
        if (!multipleUris.isNullOrEmpty()) return multipleUris.firstOrNull()

        val clipData = intent.clipData
        for (index in 0 until (clipData?.itemCount ?: 0)) {
            clipData?.getItemAt(index)?.uri?.let { return it }
        }

        return null
    }

    fun parseReceiptText(text: String): ParsedReceipt = ReceiptParser.parse(text)

    // Invitation link state. Links use an integrity checksum only; they are not cryptographic security.
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

    fun encodeEventId(eventId: Int): String {
        return try {
            android.util.Base64.encodeToString(
                "event:$eventId".toByteArray(Charsets.UTF_8),
                android.util.Base64.URL_SAFE or android.util.Base64.NO_WRAP or android.util.Base64.NO_PADDING
            )
        } catch (e: Exception) {
            eventId.toString()
        }
    }

    fun decodeEventId(encodedStr: String): Int? {
        return try {
            val decoded = android.util.Base64.decode(
                encodedStr,
                android.util.Base64.URL_SAFE or android.util.Base64.NO_WRAP or android.util.Base64.NO_PADDING
            )
            String(decoded, Charsets.UTF_8).removePrefix("event:").toIntOrNull()
        } catch (e: Exception) {
            encodedStr.toIntOrNull()
        }
    }

    fun generateInviteChecksum(
        eventId: Int,
        expiry: Long,
        creatorEmail: String = "",
        isPrivate: Boolean = false
    ): String {
        val raw = "eventId=$eventId&expiry=$expiry&creatorEmail=${creatorEmail.trim()}&private=$isPrivate"
        return checksumFor(raw)
    }

    private fun generateLegacyInviteChecksum(
        eventId: Int,
        expiry: Long,
        creatorEmail: String = ""
    ): String {
        val raw = "eventId=$eventId&expiry=$expiry&creatorEmail=${creatorEmail.trim()}"
        return checksumFor(raw)
    }

    private fun checksumFor(raw: String): String {
        return try {
            val digest = java.security.MessageDigest.getInstance("SHA-256")
            val hash = digest.digest(raw.toByteArray(Charsets.UTF_8))
            hash.joinToString("") { "%02x".format(it) }.take(16)
        } catch (e: Exception) {
            raw.hashCode().toString()
        }
    }

    fun handleDeepLink(uri: android.net.Uri) {
        _deepLinkError.value = null
        _deepLinkMessage.value = null
        viewModelScope.launch {
            try {
                val eventIdStr = uri.getQueryParameter("eventId")
                val expiryStr = uri.getQueryParameter("expiry")
                val checksum = uri.getQueryParameter("checksum") ?: uri.getQueryParameter("signature")
                val title = uri.getQueryParameter("title") ?: "Shared Ledger Event"
                val creatorEmail = uri.getQueryParameter("creatorEmail") ?: ""
                val hasPrivateMarker = uri.getQueryParameter("private") != null
                val linkIsPrivate = uri.getQueryParameter("private")?.equals("true", ignoreCase = true) == true

                if (eventIdStr == null || expiryStr == null || checksum == null) {
                    _deepLinkError.value = "Malformed event-copy link: required link components are missing."
                    return@launch
                }

                val eventId = decodeEventId(eventIdStr) ?: eventIdStr.toIntOrNull() ?: 0
                val expiry = expiryStr.toLongOrNull() ?: 0L
                if (eventId <= 0) {
                    _deepLinkError.value = "Invalid event-copy link: event id is not valid."
                    return@launch
                }

                val expectedChecksum = generateInviteChecksum(eventId, expiry, creatorEmail, linkIsPrivate)
                val legacyChecksum = generateLegacyInviteChecksum(eventId, expiry, creatorEmail)
                if (checksum != expectedChecksum && (hasPrivateMarker || checksum != legacyChecksum)) {
                    _deepLinkError.value = "Invalid event-copy link: link details do not match their checksum."
                    return@launch
                }

                if (expiry != 0L && System.currentTimeMillis() > expiry) {
                    val formattedTime = SimpleDateFormat("dd MMM hh:mm a", Locale.getDefault()).format(Date(expiry))
                    _deepLinkError.value = "Expired event-copy link: this link expired at $formattedTime."
                    return@launch
                }

                // Read Room directly. The reactive events flow may still hold its empty initial
                // value when a launch intent arrives before the dashboard starts collecting it.
                val existingEvent = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                    repository.getEventByIdOnce(eventId)
                }
                if (existingEvent != null) {
                    val existingCreatorEmail = try {
                        JSONObject(existingEvent.customFieldsJson).optString("creatorEmail", "")
                    } catch (e: Exception) {
                        ""
                    }
                    val titleMatches = existingEvent.title == title
                    val creatorMatches = creatorEmail.isBlank() ||
                        existingCreatorEmail.isBlank() ||
                        existingCreatorEmail.equals(creatorEmail, ignoreCase = true)
                    if (!titleMatches || !creatorMatches) {
                        _deepLinkError.value = "Cannot add this event copy: its link ID conflicts with a different ledger already stored on this device."
                        return@launch
                    }
                } else {
                    val json = org.json.JSONObject()
                    json.put("creatorEmail", creatorEmail)
                    json.put("visibility", if (linkIsPrivate) "private" else "public")
                    val newEvent = EventEntity(
                        id = eventId,
                        title = title,
                        createdDate = System.currentTimeMillis(),
                        isPrivate = linkIsPrivate,
                        customFieldsJson = json.toString()
                    )
                    val insertedId = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                        repository.insertEventIfAbsent(newEvent)
                    }
                    if (insertedId == -1L) {
                        _deepLinkError.value = "Cannot add this event copy because its local ledger ID is already in use."
                        return@launch
                    }
                }

                // Success: increment local link clicks and open the matching event.
                incrementLinkClicks(eventId)
                selectEvent(eventId)

                _deepLinkMessage.value = "Added '$title' to this device. Ledger entries do not sync between devices."
            } catch (e: Exception) {
                _deepLinkError.value = "Event-copy link check failed: ${e.localizedMessage}"
            }
        }
    }

    /**
     * Complete offline image processing pipeline.
     * 1. Safely decodes the Bitmap from the Android Uri. This automatically strips EXIF metadata (GPS, device ID, camera specs).
     * 2. Creates a clean, metadata-free Bitmap representation in-memory.
     * 3. Applies a grayscale and high-contrast threshold filter to enhance pattern recognition readability.
     */
    fun stripImageMetadataAndProcess(context: Context, imageUri: Uri): Bitmap? {
        return try {
            // Step 1: Decode image stream to Bitmap (This natively discards any associated EXIF headers)
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            if (originalBitmap == null) return null

            // Step 2: Create a clean target Bitmap to isolate raw pixel content from original file structure
            val width = originalBitmap.width
            val height = originalBitmap.height
            val cleanBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(cleanBitmap)
            val paint = Paint()

            // Step 3: Apply Grayscale and High Contrast Matrix to optimize OCR thresholding
            val colorMatrix = ColorMatrix().apply {
                setSaturation(0f) // Convert to complete grayscale
                // Increase contrast by 1.5x to separate background artifacts from text patterns
                val scale = 1.5f
                val translate = -128f * scale + 128f
                val matrix = floatArrayOf(
                    scale, 0f, 0f, 0f, translate,
                    0f, scale, 0f, 0f, translate,
                    0f, 0f, scale, 0f, translate,
                    0f, 0f, 0f, 1f, 0f
                )
                postConcat(ColorMatrix(matrix))
            }
            paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
            canvas.drawBitmap(originalBitmap, 0f, 0f, paint)

            cleanBitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun decodeBitmapFromUri(context: Context, imageUri: Uri): Bitmap? {
        return try {
            context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun Bitmap.scaledForOcr(): Bitmap {
        val largestSide = maxOf(width, height)
        val scale = when {
            largestSide < 1600 -> 1600f / largestSide
            largestSide > 3200 -> 3200f / largestSide
            else -> 1f
        }
        if (scale == 1f) return this

        val scaledWidth = (width * scale).toInt().coerceAtLeast(1)
        val scaledHeight = (height * scale).toInt().coerceAtLeast(1)
        return Bitmap.createScaledBitmap(this, scaledWidth, scaledHeight, true)
    }

    private fun ParsedReceipt.hasUsefulReceiptData(): Boolean {
        return amount > 0.0 || transactionId.isNotBlank()
    }

    private fun looksLikePaymentReceiptText(text: String): Boolean {
        val lowerText = text.lowercase()
        val receiptKeywords = listOf(
            "upi",
            "utr",
            "txn",
            "transaction",
            "reference",
            "ref no",
            "paid",
            "sent",
            "payment",
            "success",
            "successful",
            "completed",
            "received",
            "debited",
            "credited",
            "bank",
            "approved",
            "approval code",
            "authorization code",
            "receipt",
            "purchase",
            "google pay",
            "gpay",
            "phonepe",
            "paytm",
            "amazon pay",
            "samsung pay",
            "samsung wallet",
            "₹",
            "rs.",
            "inr"
        )
        return receiptKeywords.any { lowerText.contains(it) }
    }

    private suspend fun recognizeTextFromImage(inputImage: InputImage, script: OcrScript): String? {
        return suspendCancellableCoroutine { continuation ->
            val recognizer = when (script) {
                OcrScript.LATIN -> TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                OcrScript.DEVANAGARI -> TextRecognition.getClient(
                    DevanagariTextRecognizerOptions.Builder().build()
                )
            }
            continuation.invokeOnCancellation { recognizer.close() }

            recognizer.process(inputImage)
                .addOnSuccessListener { result ->
                    recognizer.close()
                    if (continuation.isActive) {
                        continuation.resume(result.text.takeIf { it.isNotBlank() })
                    }
                }
                .addOnFailureListener { error ->
                    recognizer.close()
                    error.printStackTrace()
                    if (continuation.isActive) {
                        continuation.resume(null)
                    }
                }
                .addOnCanceledListener {
                    recognizer.close()
                    if (continuation.isActive) {
                        continuation.resume(null)
                    }
                }
        }
    }

    private suspend fun recognizeReceiptText(context: Context, uri: Uri): String? {
        val recognizedTexts = mutableListOf<String>()

        suspend fun addRecognizedText(inputImage: InputImage) {
            OcrScript.entries.forEach { script ->
                val recognizedText = recognizeTextFromImage(inputImage, script)
                if (!recognizedText.isNullOrBlank()) {
                    recognizedTexts += recognizedText
                }
            }
        }

        try {
            addRecognizedText(InputImage.fromFilePath(context, uri))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val originalBitmap = decodeBitmapFromUri(context, uri)
        if (originalBitmap != null) {
            try {
                addRecognizedText(InputImage.fromBitmap(originalBitmap, 0))

                val scaledBitmap = originalBitmap.scaledForOcr()
                if (scaledBitmap !== originalBitmap) {
                    addRecognizedText(InputImage.fromBitmap(scaledBitmap, 0))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val processedBitmap = stripImageMetadataAndProcess(context, uri)
        if (processedBitmap != null) {
            try {
                addRecognizedText(InputImage.fromBitmap(processedBitmap, 0))

                val scaledProcessedBitmap = processedBitmap.scaledForOcr()
                if (scaledProcessedBitmap !== processedBitmap) {
                    addRecognizedText(InputImage.fromBitmap(scaledProcessedBitmap, 0))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val mergedText = recognizedTexts
            .asSequence()
            .flatMap { it.lineSequence() }
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .distinctBy { it.lowercase() }
            .joinToString("\n")

        return mergedText.takeIf { it.isNotBlank() }
    }

    private suspend fun extractReceiptWithOnDeviceOcr(context: Context, uri: Uri): ParsedReceipt? {
        val recognizedText = recognizeReceiptText(context, uri) ?: return null
        val parsedReceipt = parseReceiptText(recognizedText)
        val rawTextPreview = recognizedText.lineSequence()
                .map { it.trim() }
                .filter { it.isNotBlank() }
            .take(40)
                .joinToString("\n")

        if (!parsedReceipt.hasUsefulReceiptData() || !looksLikePaymentReceiptText(recognizedText)) {
            return parsedReceipt.copy(
                extractionMethod = "OCR text found - review manually",
                rawTextPreview = rawTextPreview
            )
        }

        return parsedReceipt.copy(
            extractionMethod = "On-device OCR (ML Kit Latin + Devanagari)",
            rawTextPreview = rawTextPreview
        )
    }

    /**
     * Extract receipt data from the uploaded image using on-device OCR only.
     */
    suspend fun extractReceiptFromUri(context: Context, uri: Uri): ParsedReceipt = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        extractReceiptWithOnDeviceOcr(context, uri) ?: ParsedReceipt(
            extractionMethod = "No receipt data found"
        )
    }
}
