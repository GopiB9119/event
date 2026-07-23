package com.communityledger.app.shared

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object SharedBackendProvider {
    fun create(context: Context): SharedBackend = DisabledSharedBackend
}

private object DisabledSharedBackend : SharedBackend {
    override val enabled: Boolean = false

    private fun unavailable(): Nothing = throw SharedBackendUnavailableException(
        "Shared events are not configured for this build."
    )

    private fun <T> unavailableFlow(): Flow<T> = flow { unavailable() }

    override suspend fun authenticate(displayName: String): SharedAccount = unavailable()
    override suspend fun createEvent(request: CreateSharedEventRequest): SharedMutationResult = unavailable()
    override suspend fun createInvite(eventId: String, role: SharedRole, expiresInSeconds: Int, idempotencyKey: String): SharedInvite = unavailable()
    override suspend fun acceptInvite(inviteToken: String, idempotencyKey: String): SharedMutationResult = unavailable()
    override suspend fun joinPublicEvent(eventId: String, idempotencyKey: String): SharedMutationResult = unavailable()
    override suspend fun submitEntry(request: SubmitSharedEntryRequest): SharedMutationResult = unavailable()
    override suspend fun reviewEntry(eventId: String, entryId: String, expectedRevision: Long, confirm: Boolean, reason: String?, idempotencyKey: String): SharedMutationResult = unavailable()
    override suspend fun publishPresence(eventId: String, state: SharedPresenceState): Long = unavailable()
    override suspend fun loadPrivateEvidence(eventId: String, entryId: String): SharedPrivateEvidence = unavailable()
    override fun observePublicEvents(): Flow<List<SharedPublicEvent>> = unavailableFlow()
    override fun observeEvent(eventId: String): Flow<SharedEvent> = unavailableFlow()
    override fun observeMembers(eventId: String): Flow<List<SharedMember>> = unavailableFlow()
    override fun observeEntries(eventId: String, role: SharedRole, accountUid: String): Flow<List<SharedEntry>> = unavailableFlow()
    override fun observePresence(eventId: String, memberUids: List<String>): Flow<Map<String, SharedPresence>> = unavailableFlow()
}