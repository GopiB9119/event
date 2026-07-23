package com.communityledger.app.ui

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
import androidx.room.withTransaction
import com.communityledger.app.BuildConfig
import com.communityledger.app.data.AppDatabase
import com.communityledger.app.data.EventEntity
import com.communityledger.app.data.EventLedgerSummary
import com.communityledger.app.data.MemberEntity
import com.communityledger.app.data.EventRepository
import com.communityledger.app.data.TransactionEntity
import com.communityledger.app.data.SharedSyncRepository
import com.communityledger.app.data.calculateLedgerPresentationSummary
import com.communityledger.app.data.isValidLedgerTransaction
import com.communityledger.app.data.normalizeLocalIdentity
import com.communityledger.app.data.transactionsForLedgerMember
import com.communityledger.app.receipt.ParsedReceipt
import com.communityledger.app.receipt.ReceiptEvidenceStore
import com.communityledger.app.receipt.ReceiptParser
import com.communityledger.app.update.UpdateCheckResult
import com.communityledger.app.update.UpdateChecker
import com.communityledger.app.shared.SharedInvite
import com.communityledger.app.shared.SharedMutationResult
import com.communityledger.app.shared.SharedPrivateEvidence
import com.communityledger.app.shared.SharedRole
import com.communityledger.app.shared.SubmitSharedEntryRequest
import com.communityledger.app.shared.createSharedBackend
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import org.json.JSONObject
import kotlin.coroutines.resume

sealed class Screen {
    object Dashboard : Screen()
    object CreateEvent : Screen()
    object SharedEvents : Screen()
    object TrustCenter : Screen()
    data class EventDetails(val eventId: Int) : Screen()
}

private const val PREF_RECEIPT_REVIEW_IN_PROGRESS = "receipt_review_in_progress"
private const val PREF_INSTALLATION_ID = "installation_id"

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
            ).addMigrations(
                AppDatabase.MIGRATION_2_3,
                AppDatabase.MIGRATION_3_4,
                AppDatabase.MIGRATION_4_5,
                AppDatabase.MIGRATION_5_6
            )
        }
        builder.build()
    }

    private val repository: EventRepository by lazy {
        EventRepository(database.eventDao())
    }

    private val sharedSyncRepository: SharedSyncRepository by lazy {
        SharedSyncRepository(database.sharedSyncDao())
    }

    private val sharedWorkspace: SharedWorkspaceController by lazy {
        SharedWorkspaceController(
            backend = createSharedBackend(application.applicationContext),
            eventRepository = repository,
            sharedSyncRepository = sharedSyncRepository,
            scope = viewModelScope
        )
    }

    private val receiptEvidenceStore by lazy {
        ReceiptEvidenceStore(application.filesDir)
    }

    init {
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            val references = runCatching {
                repository.captureReceiptEvidenceReferencesForReconciliation()
            }.getOrNull() ?: return@launch
            receiptEvidenceStore.reconcile(references)
        }
    }

    // Theme state (persisted in SharedPreferences)
    private val sharedPrefs = application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    init {
        val savedSharedName = sharedPrefs.getString("shared_display_name", "").orEmpty().trim()
        if (BuildConfig.SHARED_BACKEND_ENABLED && savedSharedName.isNotEmpty()) {
            viewModelScope.launch {
                sharedWorkspace.connect(savedSharedName)
            }
        }
    }

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
        if (!BuildConfig.DIRECT_UPDATE_ENABLED) return
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

    val sharedBackendEnabled: Boolean
        get() = BuildConfig.SHARED_BACKEND_ENABLED

    val sharedAccount by lazy { sharedWorkspace.account }
    val sharedPublicEvents by lazy { sharedWorkspace.publicEvents }
    val selectedSharedMode by lazy { sharedWorkspace.selectedMode }
    val selectedSharedLiveEvent by lazy { sharedWorkspace.liveEvent }
    val sharedBusy by lazy { sharedWorkspace.busy }
    val sharedError by lazy { sharedWorkspace.error }
    val lastSharedInvite by lazy { sharedWorkspace.lastInvite }

    fun clearSharedError() = sharedWorkspace.clearError()
    fun clearSharedInvite() = sharedWorkspace.clearInvite()

    fun connectSharedProfile(displayName: String) {
        viewModelScope.launch {
            if (sharedWorkspace.connect(displayName)) {
                sharedPrefs.edit().putString("shared_display_name", displayName.trim()).apply()
            }
        }
    }

    fun getSharedDisplayName(): String = sharedPrefs.getString("shared_display_name", "").orEmpty()

    fun createSharedEvent(
        title: String,
        duration: String?,
        isPrivate: Boolean,
        customInfo: Map<String, String>
    ) {
        viewModelScope.launch {
            val localEventId = sharedWorkspace.createSharedEvent(
                title,
                duration,
                isPrivate,
                customInfo
            ) ?: return@launch
            selectEvent(localEventId)
        }
    }

    fun acceptSharedInvite(inviteToken: String) {
        viewModelScope.launch {
            val localEventId = sharedWorkspace.acceptInvite(inviteToken) ?: return@launch
            selectEvent(localEventId)
        }
    }

    fun joinPublicSharedEvent(remoteEventId: String) {
        viewModelScope.launch {
            val localEventId = sharedWorkspace.joinPublicEvent(remoteEventId) ?: return@launch
            selectEvent(localEventId)
        }
    }

    fun createSelectedSharedInvite(role: SharedRole, expiresInSeconds: Int) {
        val link = (selectedSharedMode.value as? SelectedSharedMode.Linked)?.link ?: return
        viewModelScope.launch {
            sharedWorkspace.createInvite(link.remoteEventId, role, expiresInSeconds)
        }
    }

    fun enterSelectedSharedEvent() = sharedWorkspace.enterSelectedSharedEvent()
    fun leaveSelectedSharedEvent() = sharedWorkspace.leaveSelectedSharedEvent()

    suspend fun submitSelectedSharedEntry(request: SubmitSharedEntryRequest): SharedMutationResult? =
        sharedWorkspace.submitEntry(request)

    suspend fun reviewSelectedSharedEntry(
        entryId: String,
        confirm: Boolean,
        reason: String?
    ): SharedMutationResult? = sharedWorkspace.reviewEntry(entryId, confirm, reason)

    suspend fun loadSelectedSharedEvidence(entryId: String): SharedPrivateEvidence? =
        sharedWorkspace.loadPrivateEvidence(entryId)

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

    val selectedEventLedgerSummary: StateFlow<EventLedgerSummary> = combine(
        selectedEventMembers,
        selectedEventTransactions
    ) { members, transactions ->
        calculateLedgerPresentationSummary(members, transactions)
    }
        .flowOn(kotlinx.coroutines.Dispatchers.Default)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            EventLedgerSummary(0.0, 0.0, emptyMap(), hasValidTotals = true)
        )

    fun transactionsForSelectedEventMember(memberId: Int): Flow<List<TransactionEntity>> = combine(
        selectedEventMembers,
        selectedEventTransactions
    ) { members, transactions ->
        transactionsForLedgerMember(members, transactions, memberId)
    }.flowOn(kotlinx.coroutines.Dispatchers.Default)

    fun selectEvent(eventId: Int) {
        _selectedEventId.value = eventId
        sharedWorkspace.selectLocalEvent(eventId)
        repairMemberLinksForEvent(eventId)
        navigateTo(Screen.EventDetails(eventId))
    }

    private fun repairMemberLinksForEvent(eventId: Int) {
        if (normalizeLocalIdentity(getMyUserEmail()) == null) return
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
                eventKey = newEventKey(),
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
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            if (sharedSyncRepository.getEventLink(eventId) != null) {
                sharedWorkspace.reportError(
                    "A linked shared event cannot be deleted as a local ledger. Leaving or closing it requires a server-authorized action."
                )
                return@launch
            }
            val deletedReferences = repository.deleteEventWithReceiptReferences(eventId)
            deletedReferences.forEach { reference ->
                receiptEvidenceStore.deleteReferencedEvidence(reference.eventId, reference.notes)
            }
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
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            val deletedReference = repository.deleteTransactionWithReceiptReference(txId)
            if (deletedReference != null) {
                receiptEvidenceStore.deleteReferencedEvidence(deletedReference.eventId, deletedReference.notes)
            }
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
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            val previousNotes = repository.getTransactionByIdOnce(txId)?.notes
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
            val insertedId = repository.insertTransaction(tx)
            if (insertedId != -1L && previousNotes != tx.notes) {
                receiptEvidenceStore.deleteReferencedEvidence(eventId, previousNotes)
            }
        }
    }

    suspend fun persistReceiptTransactionWithEvidence(
        txId: Int?,
        eventId: Int,
        personName: String,
        personPhone: String,
        personEmail: String,
        amount: Double,
        type: String,
        receiptJsonText: String,
        transactionId: String = "",
        uploaderEmail: String = getMyUserEmail(),
        existingMemberId: Int? = null
    ): Boolean = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        val cleanUploaderEmail = normalizeLocalIdentity(uploaderEmail) ?: return@withContext false
        if (!isValidLedgerTransaction(eventId, amount, type)) return@withContext false

        val storedReceiptPath = saveReceiptJsonFile(
            eventId = eventId,
            personName = personName,
            uploaderEmail = cleanUploaderEmail,
            receiptJsonText = receiptJsonText
        ) ?: return@withContext false
        val evidenceFile = java.io.File(storedReceiptPath)

        try {
            val cleanName = personName.trim().ifBlank { cleanUploaderEmail.substringBefore("@").ifBlank { "Anonymous" } }
            val cleanPhone = personPhone.trim()
            val cleanEmail = personEmail.trim()
            val storedReceiptJson = JSONObject(receiptJsonText).apply {
                put("receiptFilePath", storedReceiptPath)
            }.toString(2)

            val previousNotes = txId?.let { repository.getTransactionByIdOnce(it)?.notes }
            val insertedId = database.withTransaction {
                val memberId = existingMemberId ?: resolveOrCreateMemberId(
                    eventId = eventId,
                    name = cleanName,
                    phone = cleanPhone,
                    email = cleanEmail,
                    role = if (type == "Expense" || type == "Debit") "Vendor" else "Donor"
                )
                val result = repository.insertTransaction(
                    TransactionEntity(
                    id = txId ?: 0,
                    eventId = eventId,
                    memberId = memberId,
                    personName = cleanName,
                    personPhone = cleanPhone,
                    personEmail = cleanEmail,
                    amount = amount,
                    type = type,
                    transactionId = transactionId.trim(),
                    notes = storedReceiptJson,
                    uploaderEmail = cleanUploaderEmail
                    )
                )
                if (result == -1L) throw ReceiptTransactionRejectedException()
                result
            }
            if (insertedId != -1L) {
                if (previousNotes != storedReceiptJson) {
                    receiptEvidenceStore.deleteReferencedEvidence(eventId, previousNotes)
                }
                true
            } else {
                false
            }
        } catch (error: Exception) {
            evidenceFile.delete()
            error.printStackTrace()
            false
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
        return receiptEvidenceStore.writeReceipt(
            eventId = eventId,
            personName = personName,
            uploaderEmail = cleanUploaderEmail,
            receiptJsonText = receiptJsonText
        )
    }

    fun invitePerson(eventId: Int, name: String, phone: String, email: String, role: String): Boolean {
        if (normalizeLocalIdentity(getMyUserEmail()) == null || eventId <= 0 || name.isBlank()) {
            return false
        }
        viewModelScope.launch {
            resolveOrCreateMemberId(
                eventId = eventId,
                name = name.trim(),
                phone = phone.trim(),
                email = email.trim(),
                role = role.ifBlank { "Donor" }
            )
        }
        return true
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

    private val _deepLinkIdentityRequired = MutableStateFlow(false)
    val deepLinkIdentityRequired = _deepLinkIdentityRequired.asStateFlow()
    private var pendingEventCopyUri: android.net.Uri? = null

    fun dismissDeepLinkMessage() {
        _deepLinkMessage.value = null
    }

    fun dismissDeepLinkError() {
        _deepLinkError.value = null
    }

    fun dismissPendingEventCopy() {
        pendingEventCopyUri = null
        _deepLinkIdentityRequired.value = false
    }

    fun resumePendingEventCopy(): Boolean {
        if (normalizeLocalIdentity(getMyUserEmail()) == null) return false
        val uri = pendingEventCopyUri ?: return false
        pendingEventCopyUri = null
        _deepLinkIdentityRequired.value = false
        handleDeepLink(uri)
        return true
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

    fun getEventShareKey(event: EventEntity): String {
        return event.eventKey?.takeIf { it.isNotBlank() }
            ?: sha256Hex("${getOrCreateInstallationId()}:${event.id}:${event.createdDate}").take(32)
    }

    fun generateEventCopyChecksum(
        eventKey: String,
        expiry: Long,
        creatorEmail: String,
        isPrivate: Boolean,
        title: String
    ): String {
        val raw = "eventKey=${eventKey.trim()}&expiry=$expiry&creatorEmail=${creatorEmail.trim()}&private=$isPrivate&title=${title.trim()}"
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
            sha256Hex(raw).take(16)
        } catch (e: Exception) {
            raw.hashCode().toString()
        }
    }

    private fun sha256Hex(raw: String): String {
        val digest = java.security.MessageDigest.getInstance("SHA-256")
        return digest.digest(raw.toByteArray(Charsets.UTF_8))
            .joinToString("") { "%02x".format(it) }
    }

    private fun newEventKey(): String = UUID.randomUUID().toString().replace("-", "")

    private fun getOrCreateInstallationId(): String {
        sharedPrefs.getString(PREF_INSTALLATION_ID, null)?.takeIf { it.isNotBlank() }?.let { return it }
        val installationId = newEventKey()
        sharedPrefs.edit().putString(PREF_INSTALLATION_ID, installationId).commit()
        return installationId
    }

    private fun legacyEventKey(eventId: Int, creatorEmail: String, isPrivate: Boolean): String {
        return sha256Hex("legacy:$eventId:${creatorEmail.trim().lowercase(Locale.ROOT)}:$isPrivate").take(32)
    }

    fun handleDeepLink(uri: android.net.Uri) {
        _deepLinkError.value = null
        _deepLinkMessage.value = null
        viewModelScope.launch {
            try {
                val eventKeyParam = uri.getQueryParameter("eventKey")
                val eventIdStr = uri.getQueryParameter("eventId")
                val expiryStr = uri.getQueryParameter("expiry")
                val checksum = uri.getQueryParameter("checksum") ?: uri.getQueryParameter("signature")
                val title = uri.getQueryParameter("title") ?: "Shared Ledger Event"
                val creatorEmail = uri.getQueryParameter("creatorEmail") ?: ""
                val hasPrivateMarker = uri.getQueryParameter("private") != null
                val linkIsPrivate = uri.getQueryParameter("private")?.equals("true", ignoreCase = true) == true

                if ((eventKeyParam == null && eventIdStr == null) || expiryStr == null || checksum == null) {
                    _deepLinkError.value = "Malformed event-copy link: required link components are missing."
                    return@launch
                }

                val expiry = expiryStr.toLongOrNull() ?: 0L
                val eventKey = if (eventKeyParam != null) {
                    val normalizedEventKey = eventKeyParam.trim().lowercase(Locale.ROOT)
                    if (!normalizedEventKey.matches(Regex("[0-9a-f]{32}"))) {
                        _deepLinkError.value = "Invalid event-copy link: event key is not valid."
                        return@launch
                    }
                    val expectedChecksum = generateEventCopyChecksum(
                        eventKey = normalizedEventKey,
                        expiry = expiry,
                        creatorEmail = creatorEmail,
                        isPrivate = linkIsPrivate,
                        title = title
                    )
                    if (checksum != expectedChecksum) {
                        _deepLinkError.value = "Invalid event-copy link: link details do not match their checksum."
                        return@launch
                    }
                    normalizedEventKey
                } else {
                    val legacyEventId = decodeEventId(eventIdStr.orEmpty()) ?: eventIdStr?.toIntOrNull() ?: 0
                    if (legacyEventId <= 0) {
                        _deepLinkError.value = "Invalid event-copy link: event id is not valid."
                        return@launch
                    }
                    val expectedChecksum = generateInviteChecksum(legacyEventId, expiry, creatorEmail, linkIsPrivate)
                    val legacyChecksum = generateLegacyInviteChecksum(legacyEventId, expiry, creatorEmail)
                    if (checksum != expectedChecksum && (hasPrivateMarker || checksum != legacyChecksum)) {
                        _deepLinkError.value = "Invalid event-copy link: link details do not match their checksum."
                        return@launch
                    }
                    legacyEventKey(legacyEventId, creatorEmail, linkIsPrivate)
                }

                if (expiry != 0L && System.currentTimeMillis() > expiry) {
                    val formattedTime = SimpleDateFormat("dd MMM hh:mm a", Locale.getDefault()).format(Date(expiry))
                    _deepLinkError.value = "Expired event-copy link: this link expired at $formattedTime."
                    return@launch
                }

                if (normalizeLocalIdentity(getMyUserEmail()) == null) {
                    pendingEventCopyUri = uri
                    _deepLinkIdentityRequired.value = true
                    return@launch
                }

                val existingEvent = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                    repository.getEventByEventKeyOnce(eventKey)
                }
                val localEvent = if (existingEvent != null) {
                    existingEvent
                } else {
                    val json = org.json.JSONObject()
                    json.put("creatorEmail", creatorEmail)
                    json.put("visibility", if (linkIsPrivate) "private" else "public")
                    val newEvent = EventEntity(
                        eventKey = eventKey,
                        title = title,
                        createdDate = System.currentTimeMillis(),
                        isPrivate = linkIsPrivate,
                        customFieldsJson = json.toString()
                    )
                    val insertedId = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                        repository.insertEventIfAbsent(newEvent)
                    }
                    if (insertedId == -1L) {
                        val concurrentlyInserted = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                            repository.getEventByEventKeyOnce(eventKey)
                        }
                        if (concurrentlyInserted == null) {
                            _deepLinkError.value = "Cannot add this event copy because its event identity is already in use."
                            return@launch
                        }
                        concurrentlyInserted
                    } else {
                        newEvent.copy(id = insertedId.toInt())
                    }
                }

                // Success: increment local link clicks and open the matching event.
                incrementLinkClicks(localEvent.id)
                selectEvent(localEvent.id)

                _deepLinkMessage.value = "Added an independent copy of '${localEvent.title}'. You were not added as a member; entries and balances do not sync."
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

private class ReceiptTransactionRejectedException : IllegalStateException()
