package com.communityledger.app.shared

import android.content.Context
import kotlinx.coroutines.flow.Flow
import java.util.UUID

enum class SharedRole {
    ORGANIZER,
    CONTRIBUTOR,
    VIEWER
}

enum class SharedEntryStatus {
    PENDING,
    CONFIRMED,
    REJECTED
}

enum class SharedPresenceState {
    ACTIVE,
    RECENTLY_ACTIVE,
    UNAVAILABLE
}

data class SharedAccount(
    val uid: String,
    val displayName: String
)

data class SharedEvent(
    val id: String,
    val ownerUid: String,
    val title: String,
    val duration: String?,
    val visibilityPolicy: String,
    val customInfo: Map<String, String?>,
    val revision: Long,
    val totalCollectedMinor: Long,
    val totalSpentMinor: Long,
    val confirmedReceiptCount: Long,
    val activeMemberCount: Long,
    val organizerCount: Long,
    val contributorCount: Long,
    val viewerCount: Long,
    val updatedAtEpochMillis: Long?,
    val fromCache: Boolean
) {
    val availableBalanceMinor: Long
        get() = Math.subtractExact(totalCollectedMinor, totalSpentMinor)
}

data class SharedPublicEvent(
    val id: String,
    val title: String,
    val duration: String?,
    val activeMemberCount: Long
)

data class SharedMember(
    val uid: String,
    val role: SharedRole,
    val displayName: String,
    val confirmedReceiptCount: Long,
    val confirmedMoneyInMinor: Long,
    val confirmedMoneyOutMinor: Long
)

data class SharedEntry(
    val id: String,
    val status: SharedEntryStatus,
    val revision: Long?,
    val ledgerType: String,
    val amountMinor: Long,
    val amountEvidenceSource: String,
    val amountEvidenceConfidence: Int,
    val ledgerPersonUid: String,
    val ledgerPersonDisplayName: String,
    val submittedByUid: String,
    val submittedByDisplayName: String,
    val createdAtEpochMillis: Long?
)

data class SharedPrivateEvidence(
    val eventId: String,
    val entryId: String,
    val submittedByUid: String,
    val paymentApp: String?,
    val paymentDate: String?,
    val counterparty: String?,
    val paymentReference: String?,
    val confidence: Int,
    val amountEvidenceSource: String,
    val amountEvidenceConfidence: Int,
    val warnings: List<String>
)

data class SharedPresence(
    val uid: String,
    val state: SharedPresenceState,
    val updatedAtEpochMillis: Long?
)

data class SharedMutationResult(
    val eventId: String,
    val revision: Long,
    val entryId: String? = null,
    val status: SharedEntryStatus? = null,
    val role: SharedRole? = null,
    val alreadyMember: Boolean = false
)

data class SharedInvite(
    val eventId: String,
    val inviteToken: String,
    val role: SharedRole,
    val expiresAtEpochMillis: Long
)

data class CreateSharedEventRequest(
    val idempotencyKey: String = newSharedOperationKey(),
    val title: String,
    val duration: String?,
    val isPrivate: Boolean,
    val customInfo: Map<String, String?>
)

data class SubmitSharedEntryRequest(
    val idempotencyKey: String = newSharedOperationKey(),
    val eventId: String,
    val ledgerPersonUid: String,
    val ledgerType: String,
    val amountMinor: Long,
    val amountEvidenceSource: String,
    val amountEvidenceConfidence: Int,
    val expectedRevision: Long,
    val paymentReference: String?,
    val paymentDate: String?,
    val paymentApp: String?,
    val counterparty: String?,
    val confidence: Int,
    val warnings: List<String>
)

interface SharedBackend {
    val enabled: Boolean

    suspend fun authenticate(displayName: String): SharedAccount
    suspend fun createEvent(request: CreateSharedEventRequest): SharedMutationResult
    suspend fun createInvite(
        eventId: String,
        role: SharedRole,
        expiresInSeconds: Int,
        idempotencyKey: String = newSharedOperationKey()
    ): SharedInvite
    suspend fun acceptInvite(
        inviteToken: String,
        idempotencyKey: String = newSharedOperationKey()
    ): SharedMutationResult
    suspend fun joinPublicEvent(
        eventId: String,
        idempotencyKey: String = newSharedOperationKey()
    ): SharedMutationResult
    suspend fun submitEntry(request: SubmitSharedEntryRequest): SharedMutationResult
    suspend fun reviewEntry(
        eventId: String,
        entryId: String,
        expectedRevision: Long,
        confirm: Boolean,
        reason: String?,
        idempotencyKey: String = newSharedOperationKey()
    ): SharedMutationResult
    suspend fun publishPresence(eventId: String, state: SharedPresenceState): Long
    suspend fun loadPrivateEvidence(eventId: String, entryId: String): SharedPrivateEvidence

    fun observePublicEvents(): Flow<List<SharedPublicEvent>>
    fun observeEvent(eventId: String): Flow<SharedEvent>
    fun observeMembers(eventId: String): Flow<List<SharedMember>>
    fun observeEntries(eventId: String, role: SharedRole, accountUid: String): Flow<List<SharedEntry>>
    fun observePresence(eventId: String, memberUids: List<String>): Flow<Map<String, SharedPresence>>
}

class SharedBackendUnavailableException(message: String) : IllegalStateException(message)

class SharedBackendException(
    val code: String,
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)

fun newSharedOperationKey(): String = UUID.randomUUID().toString().replace("-", "")

fun createSharedBackend(context: Context): SharedBackend = SharedBackendProvider.create(context)