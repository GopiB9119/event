package com.communityledger.app.shared

import android.content.Context
import android.util.Base64
import com.communityledger.app.BuildConfig
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal class FirebaseEmulatorSharedBackend private constructor(
    private val app: FirebaseApp,
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val database: FirebaseDatabase,
    private val callableTransport: EmulatorCallableTransport
) : SharedBackend {
    override val enabled: Boolean = true

    override suspend fun authenticate(displayName: String): SharedAccount {
        val cleanName = displayName.trim()
        require(cleanName.isNotEmpty() && cleanName.length <= 80) {
            "Shared profile name must contain 1 to 80 characters."
        }
        val user = auth.currentUser ?: auth.signInAnonymously().awaitResult().user
            ?: throw SharedBackendException("unauthenticated", "Emulator authentication did not return an account.")
        if (user.displayName != cleanName) {
            user.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(cleanName).build()
            ).awaitResult()
            user.getIdToken(true).awaitResult()
        }
        return SharedAccount(user.uid, cleanName)
    }

    override suspend fun createEvent(request: CreateSharedEventRequest): SharedMutationResult {
        val result = callableTransport.call(
            "createSharedEvent",
            mapOf(
                "idempotencyKey" to request.idempotencyKey,
                "title" to request.title,
                "duration" to request.duration,
                "visibilityPolicy" to if (request.isPrivate) "private" else "public",
                "customInfo" to request.customInfo
            )
        )
        return result.toMutationResult()
    }

    override suspend fun createInvite(
        eventId: String,
        role: SharedRole,
        expiresInSeconds: Int,
        idempotencyKey: String
    ): SharedInvite {
        val result = callableTransport.call(
            "createSharedInvite",
            mapOf(
                "idempotencyKey" to idempotencyKey,
                "eventId" to eventId,
                "role" to role.wireValue,
                "expiresInSeconds" to expiresInSeconds
            )
        )
        return SharedInvite(
            eventId = result.requireString("eventId"),
            inviteToken = result.requireString("inviteToken"),
            role = result.requireRole("role"),
            expiresAtEpochMillis = result.requireLong("expiresAtMillis")
        )
    }

    override suspend fun acceptInvite(inviteToken: String, idempotencyKey: String): SharedMutationResult {
        return callableTransport.call(
            "acceptSharedInvite",
            mapOf("idempotencyKey" to idempotencyKey, "inviteToken" to inviteToken)
        ).toMutationResult()
    }

    override suspend fun joinPublicEvent(eventId: String, idempotencyKey: String): SharedMutationResult {
        return callableTransport.call(
            "joinPublicSharedEvent",
            mapOf("idempotencyKey" to idempotencyKey, "eventId" to eventId)
        ).toMutationResult()
    }

    override suspend fun submitEntry(request: SubmitSharedEntryRequest): SharedMutationResult {
        return callableTransport.call(
            "submitSharedEntry",
            mapOf(
                "idempotencyKey" to request.idempotencyKey,
                "eventId" to request.eventId,
                "ledgerPersonUid" to request.ledgerPersonUid,
                "ledgerType" to request.ledgerType,
                "amountMinor" to request.amountMinor,
                "amountEvidenceSource" to request.amountEvidenceSource,
                "amountEvidenceConfidence" to request.amountEvidenceConfidence,
                "expectedRevision" to request.expectedRevision,
                "paymentReference" to request.paymentReference,
                "paymentDate" to request.paymentDate,
                "paymentApp" to request.paymentApp,
                "counterparty" to request.counterparty,
                "confidence" to request.confidence,
                "warnings" to request.warnings
            )
        ).toMutationResult()
    }

    override suspend fun reviewEntry(
        eventId: String,
        entryId: String,
        expectedRevision: Long,
        confirm: Boolean,
        reason: String?,
        idempotencyKey: String
    ): SharedMutationResult {
        return callableTransport.call(
            "reviewSharedEntry",
            mapOf(
                "idempotencyKey" to idempotencyKey,
                "eventId" to eventId,
                "entryId" to entryId,
                "expectedRevision" to expectedRevision,
                "decision" to if (confirm) "confirm" else "reject",
                "reason" to reason
            )
        ).toMutationResult()
    }

    override suspend fun publishPresence(eventId: String, state: SharedPresenceState): Long {
        val result = callableTransport.call("refreshSharedPresence", mapOf("eventId" to eventId))
        val expiresAt = result.requireLong("expiresAt")
        val uid = auth.currentUser?.uid
            ?: throw SharedBackendException("unauthenticated", "Shared account is not authenticated.")
        database.getReference("presence/$eventId/$uid")
            .setValue(mapOf("state" to state.wireValue, "updatedAt" to ServerValue.TIMESTAMP))
            .awaitResult()
        return expiresAt
    }

    override suspend fun loadPrivateEvidence(eventId: String, entryId: String): SharedPrivateEvidence {
        val document = firestore.collection("events").document(eventId)
            .collection("privateEvidence").document(entryId)
            .get()
            .awaitResult()
        if (!document.exists()) {
            throw SharedBackendException("not-found", "Receipt evidence was not found.")
        }
        return document.toSharedPrivateEvidence(eventId, entryId)
    }

    override fun observePublicEvents(): Flow<List<SharedPublicEvent>> = callbackFlow {
        val registration = firestore.collection("publicEvents")
            .whereEqualTo("visibilityPolicy", "public")
            .whereEqualTo("status", "active")
            .limit(MAX_PUBLIC_EVENTS)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                runCatching {
                    snapshot?.documents.orEmpty().map(DocumentSnapshot::toSharedPublicEvent)
                }.onSuccess { trySend(it) }.onFailure { close(it) }
            }
        awaitClose(registration::remove)
    }

    override fun observeEvent(eventId: String): Flow<SharedEvent> = callbackFlow {
        val registration = firestore.collection("events").document(eventId)
            .addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                runCatching {
                    val document = snapshot?.takeIf(DocumentSnapshot::exists)
                        ?: throw SharedBackendException("not-found", "Shared event was not found.")
                    document.toSharedEvent()
                }.onSuccess { trySend(it) }.onFailure { close(it) }
            }
        awaitClose(registration::remove)
    }

    override fun observeMembers(eventId: String): Flow<List<SharedMember>> = callbackFlow {
        val registration = firestore.collection("events").document(eventId)
            .collection("members")
            .whereEqualTo("status", "active")
            .limit(MAX_MEMBERS)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                runCatching {
                    snapshot?.documents.orEmpty().map(DocumentSnapshot::toSharedMember)
                        .sortedBy { it.displayName.lowercase() }
                }.onSuccess { trySend(it) }.onFailure { close(it) }
            }
        awaitClose(registration::remove)
    }

    override fun observeEntries(
        eventId: String,
        role: SharedRole,
        accountUid: String
    ): Flow<List<SharedEntry>> {
        val entries = firestore.collection("events").document(eventId).collection("entries")
        return if (role == SharedRole.ORGANIZER) {
            entries.limit(MAX_ENTRIES).snapshotFlow { it.toSharedEntry() }
        } else {
            combine(
                entries.whereEqualTo("status", "confirmed").limit(MAX_ENTRIES)
                    .snapshotFlow { it.toSharedEntry() },
                entries.whereEqualTo("submittedByUid", accountUid).limit(MAX_ENTRIES)
                    .snapshotFlow { it.toSharedEntry() }
            ) { confirmed, own ->
                (confirmed + own).associateBy(SharedEntry::id).values
                    .sortedByDescending { it.createdAtEpochMillis ?: Long.MIN_VALUE }
            }
        }
    }

    override fun observePresence(
        eventId: String,
        memberUids: List<String>
    ): Flow<Map<String, SharedPresence>> {
        val uniqueUids = memberUids.distinct().take(MAX_MEMBERS.toInt())
        if (uniqueUids.isEmpty()) return flowOf(emptyMap())
        return callbackFlow {
            val current = AtomicReference<Map<String, SharedPresence>>(emptyMap())
            val listeners = uniqueUids.map { uid ->
                val reference = database.getReference("presence/$eventId/$uid")
                val listener = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val value = snapshot.toSharedPresence(uid)
                        val next = current.get().toMutableMap().apply {
                            if (value == null) remove(uid) else put(uid, value)
                        }.toMap()
                        current.set(next)
                        trySend(next)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        val next = current.get().toMutableMap().apply {
                            put(uid, SharedPresence(uid, SharedPresenceState.UNAVAILABLE, null))
                        }.toMap()
                        current.set(next)
                        trySend(next)
                    }
                }
                reference.addValueEventListener(listener)
                reference to listener
            }
            awaitClose { listeners.forEach { (reference, listener) -> reference.removeEventListener(listener) } }
        }
    }

    internal fun close() {
        auth.signOut()
        app.delete()
    }

    private fun Query.snapshotFlow(mapper: (DocumentSnapshot) -> SharedEntry): Flow<List<SharedEntry>> =
        callbackFlow {
            val registration: ListenerRegistration = addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                runCatching {
                    snapshot?.documents.orEmpty().map(mapper)
                        .sortedByDescending { it.createdAtEpochMillis ?: Long.MIN_VALUE }
                }.onSuccess { trySend(it) }.onFailure { close(it) }
            }
            awaitClose(registration::remove)
        }

    companion object {
        private const val PROJECT_ID = "demo-community-ledger"
        private const val APP_NAME = "community-ledger-shared-emulator"
        private const val AUTH_PORT = 9099
        private const val FIRESTORE_PORT = 8080
        private const val DATABASE_PORT = 9000
        private const val MAX_PUBLIC_EVENTS = 100L
        private const val MAX_MEMBERS = 500L
        private const val MAX_ENTRIES = 500L

        fun create(
            context: Context,
            appName: String = APP_NAME
        ): FirebaseEmulatorSharedBackend {
            check(BuildConfig.SHARED_BACKEND_ENABLED) { "Shared backend is disabled for this build." }
            val host = BuildConfig.SHARED_EMULATOR_HOST
            require(host.isNotBlank()) { "Shared emulator host is not configured." }
            require(appName.matches(Regex("[A-Za-z0-9._-]{1,80}"))) {
                "Shared Firebase app name is invalid."
            }
            val appContext = context.applicationContext
            val app = FirebaseApp.getApps(appContext).firstOrNull { it.name == appName }
                ?: FirebaseApp.initializeApp(
                    appContext,
                    FirebaseOptions.Builder()
                        .setApplicationId("1:000000000000:android:community-ledger-emulator")
                        .setApiKey("demo-key")
                        .setProjectId(PROJECT_ID)
                        .setDatabaseUrl("https://$PROJECT_ID-default-rtdb.firebaseio.com")
                        .build(),
                    appName
                )
            val auth = FirebaseAuth.getInstance(app).apply { useEmulator(host, AUTH_PORT) }
            val firestore = FirebaseFirestore.getInstance(app).apply { useEmulator(host, FIRESTORE_PORT) }
            val database = FirebaseDatabase.getInstance(app).apply { useEmulator(host, DATABASE_PORT) }
            return FirebaseEmulatorSharedBackend(
                app,
                auth,
                firestore,
                database,
                EmulatorCallableTransport(auth, host)
            )
        }
    }
}

private class EmulatorCallableTransport(
    private val auth: FirebaseAuth,
    private val host: String
) {
    suspend fun call(functionName: String, data: Map<String, Any?>): JSONObject = withContext(Dispatchers.IO) {
        val user = auth.currentUser
            ?: throw SharedBackendException("unauthenticated", "Shared account is not authenticated.")
        val idToken = user.getIdToken(false).awaitResult().token
            ?: throw SharedBackendException("unauthenticated", "Shared account token is unavailable.")
        val connection = URL(
            "http://$host:5001/demo-community-ledger/us-central1/$functionName"
        ).openConnection() as HttpURLConnection
        try {
            connection.requestMethod = "POST"
            connection.connectTimeout = 10_000
            connection.readTimeout = 120_000
            connection.doOutput = true
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8")
            connection.setRequestProperty("Authorization", "Bearer $idToken")
            connection.setRequestProperty("X-Firebase-AppCheck", emulatorAppCheckToken())
            val requestBytes = JSONObject().put("data", data.toJsonValue()).toString()
                .toByteArray(StandardCharsets.UTF_8)
            require(requestBytes.size <= MAX_REQUEST_BYTES) { "Shared request exceeds the supported size." }
            connection.outputStream.use { it.write(requestBytes) }

            val status = connection.responseCode
            val input = if (status in 200..299) connection.inputStream else connection.errorStream
            val responseText = input?.use { stream ->
                BufferedInputStream(stream).readBoundedUtf8(MAX_RESPONSE_BYTES)
            }.orEmpty()
            val response = runCatching { JSONObject(responseText) }.getOrElse { error ->
                throw SharedBackendException("invalid-response", "Shared server returned malformed data.", error)
            }
            if (status !in 200..299) {
                val serverError = response.optJSONObject("error")
                throw SharedBackendException(
                    serverError?.optString("status")?.lowercase()?.replace('_', '-') ?: "server-error",
                    serverError?.optString("message")?.takeIf(String::isNotBlank)
                        ?: "Shared server rejected the operation."
                )
            }
            response.optJSONObject("result")
                ?: throw SharedBackendException("invalid-response", "Shared server result is missing.")
        } finally {
            connection.disconnect()
        }
    }

    private fun emulatorAppCheckToken(): String {
        val nowSeconds = System.currentTimeMillis() / 1000L
        val header = JSONObject().put("alg", "none").put("typ", "JWT")
        val payload = JSONObject()
            .put("sub", "community-ledger-android-emulator")
            .put("iat", nowSeconds)
            .put("exp", nowSeconds + 3600L)
        return "${header.base64Url()}.${payload.base64Url()}.emulator-signature"
    }

    private fun JSONObject.base64Url(): String = Base64.encodeToString(
        toString().toByteArray(StandardCharsets.UTF_8),
        Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
    )

    private companion object {
        const val MAX_REQUEST_BYTES = 256 * 1024
        const val MAX_RESPONSE_BYTES = 1024 * 1024
    }
}

private suspend fun <T> Task<T>.awaitResult(): T = suspendCancellableCoroutine { continuation ->
    addOnCompleteListener { task ->
        if (task.isSuccessful) {
            continuation.resume(task.result)
        } else {
            continuation.resumeWithException(task.exception ?: IllegalStateException("Firebase task failed."))
        }
    }
}

private fun BufferedInputStream.readBoundedUtf8(maxBytes: Int): String {
    val output = ArrayList<Byte>(minOf(maxBytes, 8192))
    while (true) {
        val value = read()
        if (value < 0) break
        if (output.size >= maxBytes) {
            throw SharedBackendException("response-too-large", "Shared server response exceeds the supported size.")
        }
        output += value.toByte()
    }
    return output.toByteArray().toString(StandardCharsets.UTF_8)
}

private fun Any?.toJsonValue(): Any = when (this) {
    null -> JSONObject.NULL
    is Map<*, *> -> JSONObject().apply {
        this@toJsonValue.forEach { (key, value) -> put(key.toString(), value.toJsonValue()) }
    }
    is Iterable<*> -> JSONArray().apply { this@toJsonValue.forEach { put(it.toJsonValue()) } }
    else -> this
}

private fun JSONObject.toMutationResult(): SharedMutationResult = SharedMutationResult(
    eventId = requireString("eventId"),
    revision = requireLong("revision"),
    entryId = optString("entryId").takeIf(String::isNotBlank),
    status = optString("status").takeIf(String::isNotBlank)?.toSharedEntryStatus(),
    role = optString("role").takeIf(String::isNotBlank)?.toSharedRole(),
    alreadyMember = optBoolean("alreadyMember", false)
)

private fun JSONObject.requireString(name: String): String = optString(name)
    .takeIf(String::isNotBlank)
    ?: throw SharedBackendException("invalid-response", "Shared server field '$name' is missing.")

private fun JSONObject.requireLong(name: String): Long {
    if (!has(name)) throw SharedBackendException("invalid-response", "Shared server field '$name' is missing.")
    return try {
        getLong(name)
    } catch (error: Exception) {
        throw SharedBackendException("invalid-response", "Shared server field '$name' is invalid.", error)
    }
}

private fun JSONObject.requireRole(name: String): SharedRole = requireString(name).toSharedRole()

private fun DocumentSnapshot.toSharedPublicEvent(): SharedPublicEvent = SharedPublicEvent(
    id = id,
    title = requireString("title"),
    duration = getString("duration"),
    activeMemberCount = requireNonNegativeLong("activeMemberCount")
)

private fun DocumentSnapshot.toSharedEvent(): SharedEvent = SharedEvent(
    id = id,
    ownerUid = requireString("ownerUid"),
    title = requireString("title"),
    duration = getString("duration"),
    visibilityPolicy = requireString("visibilityPolicy"),
    customInfo = requireStringMap("customInfo"),
    revision = requirePositiveLong("revision"),
    totalCollectedMinor = requireNonNegativeLong("totalCollectedMinor"),
    totalSpentMinor = requireNonNegativeLong("totalSpentMinor"),
    confirmedReceiptCount = requireNonNegativeLong("confirmedReceiptCount"),
    activeMemberCount = requirePositiveLong("activeMemberCount"),
    organizerCount = requirePositiveLong("organizerCount"),
    contributorCount = requireNonNegativeLong("contributorCount"),
    viewerCount = requireNonNegativeLong("viewerCount"),
    updatedAtEpochMillis = getTimestamp("updatedAtServer")?.toDate()?.time,
    fromCache = metadata.isFromCache
)

private fun DocumentSnapshot.toSharedMember(): SharedMember = SharedMember(
    uid = requireString("uid").also {
        if (it != id) throw SharedBackendException("invalid-data", "Shared member identity does not match its document.")
    },
    role = requireString("role").toSharedRole(),
    displayName = requireString("displayName"),
    confirmedReceiptCount = requireNonNegativeLong("confirmedReceiptCount"),
    confirmedMoneyInMinor = requireNonNegativeLong("confirmedMoneyInMinor"),
    confirmedMoneyOutMinor = requireNonNegativeLong("confirmedMoneyOutMinor")
)

private fun DocumentSnapshot.toSharedEntry(): SharedEntry = SharedEntry(
    id = id,
    status = requireString("status").toSharedEntryStatus(),
    revision = getLong("revision"),
    ledgerType = requireString("ledgerType").also {
        if (it !in setOf("Donated", "Credit", "Debit", "Expense")) {
            throw SharedBackendException("invalid-data", "Shared entry type is not supported.")
        }
    },
    amountMinor = requirePositiveLong("amountMinor"),
    amountEvidenceSource = requireString("amountEvidenceSource"),
    amountEvidenceConfidence = requireBoundedInt("amountEvidenceConfidence", 65, 100),
    ledgerPersonUid = requireString("ledgerPersonUid"),
    ledgerPersonDisplayName = requireString("ledgerPersonDisplayNameSnapshot"),
    submittedByUid = requireString("submittedByUid"),
    submittedByDisplayName = requireString("submittedByDisplayNameSnapshot"),
    createdAtEpochMillis = getTimestamp("createdAtServer")?.toDate()?.time
)

private fun DocumentSnapshot.toSharedPrivateEvidence(
    expectedEventId: String,
    expectedEntryId: String
): SharedPrivateEvidence {
    val eventId = requireString("eventId")
    val entryId = requireString("entryId")
    if (eventId != expectedEventId || entryId != expectedEntryId || id != expectedEntryId) {
        throw SharedBackendException("invalid-data", "Receipt evidence identity does not match its entry.")
    }
    val confidence = requireBoundedInt("confidence", 0, 100)
    val amountEvidenceConfidence = requireBoundedInt("amountEvidenceConfidence", 65, 100)
    val warnings = (get("warnings") as? List<*>)?.map { warning ->
        (warning as? String)?.takeIf { it.length <= 200 }
            ?: throw SharedBackendException("invalid-data", "Receipt evidence warning is invalid.")
    } ?: throw SharedBackendException("invalid-data", "Receipt evidence warnings are missing.")
    if (warnings.size > 20) {
        throw SharedBackendException("invalid-data", "Receipt evidence contains too many warnings.")
    }
    return SharedPrivateEvidence(
        eventId = eventId,
        entryId = entryId,
        submittedByUid = requireString("submittedByUid"),
        paymentApp = getString("paymentApp"),
        paymentDate = getString("paymentDate"),
        counterparty = getString("counterparty"),
        paymentReference = getString("paymentReference"),
        confidence = confidence,
        amountEvidenceSource = requireString("amountEvidenceSource"),
        amountEvidenceConfidence = amountEvidenceConfidence,
        warnings = warnings
    )
}

private fun DataSnapshot.toSharedPresence(uid: String): SharedPresence? {
    if (!exists()) return null
    val state = child("state").getValue(String::class.java)?.toSharedPresenceState()
        ?: throw SharedBackendException("invalid-data", "Shared presence state is missing.")
    val updatedAt = child("updatedAt").getValue(Long::class.java)
        ?: throw SharedBackendException("invalid-data", "Shared presence timestamp is missing.")
    return SharedPresence(uid, state, updatedAt)
}

private fun DocumentSnapshot.requireString(name: String): String = getString(name)
    ?.takeIf(String::isNotBlank)
    ?: throw SharedBackendException("invalid-data", "Shared field '$name' is missing.")

private fun DocumentSnapshot.requirePositiveLong(name: String): Long = requireLong(name).also {
    if (it <= 0L) throw SharedBackendException("invalid-data", "Shared field '$name' must be positive.")
}

private fun DocumentSnapshot.requireNonNegativeLong(name: String): Long = requireLong(name).also {
    if (it < 0L) throw SharedBackendException("invalid-data", "Shared field '$name' must not be negative.")
}

private fun DocumentSnapshot.requireLong(name: String): Long = getLong(name)
    ?: throw SharedBackendException("invalid-data", "Shared field '$name' is missing.")

private fun DocumentSnapshot.requireBoundedInt(name: String, minimum: Int, maximum: Int): Int {
    val value = requireLong(name)
    if (value !in minimum.toLong()..maximum.toLong()) {
        throw SharedBackendException("invalid-data", "Shared field '$name' is outside the supported range.")
    }
    return value.toInt()
}

private fun DocumentSnapshot.requireStringMap(name: String): Map<String, String?> {
    val source = get(name) as? Map<*, *>
        ?: throw SharedBackendException("invalid-data", "Shared field '$name' is invalid.")
    return source.entries.associate { (key, value) ->
        val stringKey = key as? String
            ?: throw SharedBackendException("invalid-data", "Shared custom information key is invalid.")
        if (value != null && value !is String) {
            throw SharedBackendException("invalid-data", "Shared custom information value is invalid.")
        }
        stringKey to value as String?
    }
}

private fun String.toSharedRole(): SharedRole = when (this) {
    "organizer" -> SharedRole.ORGANIZER
    "contributor" -> SharedRole.CONTRIBUTOR
    "viewer" -> SharedRole.VIEWER
    else -> throw SharedBackendException("invalid-data", "Shared role is not supported.")
}

private fun String.toSharedEntryStatus(): SharedEntryStatus = when (this) {
    "pending" -> SharedEntryStatus.PENDING
    "confirmed" -> SharedEntryStatus.CONFIRMED
    "rejected" -> SharedEntryStatus.REJECTED
    else -> throw SharedBackendException("invalid-data", "Shared entry status is not supported.")
}

private fun String.toSharedPresenceState(): SharedPresenceState = when (this) {
    "active" -> SharedPresenceState.ACTIVE
    "recently_active" -> SharedPresenceState.RECENTLY_ACTIVE
    "unavailable" -> SharedPresenceState.UNAVAILABLE
    else -> throw SharedBackendException("invalid-data", "Shared presence state is not supported.")
}

private val SharedRole.wireValue: String
    get() = when (this) {
        SharedRole.ORGANIZER -> "organizer"
        SharedRole.CONTRIBUTOR -> "contributor"
        SharedRole.VIEWER -> "viewer"
    }

private val SharedPresenceState.wireValue: String
    get() = when (this) {
        SharedPresenceState.ACTIVE -> "active"
        SharedPresenceState.RECENTLY_ACTIVE -> "recently_active"
        SharedPresenceState.UNAVAILABLE -> "unavailable"
    }