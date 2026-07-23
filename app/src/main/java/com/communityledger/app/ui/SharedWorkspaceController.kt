package com.communityledger.app.ui

import com.communityledger.app.data.EventEntity
import com.communityledger.app.data.EventRepository
import com.communityledger.app.data.SharedEventLinkEntity
import com.communityledger.app.data.SharedOperationState
import com.communityledger.app.data.SharedPendingOperationEntity
import com.communityledger.app.data.SharedSyncRepository
import com.communityledger.app.data.SharedSyncState
import com.communityledger.app.shared.CreateSharedEventRequest
import com.communityledger.app.shared.SharedAccount
import com.communityledger.app.shared.SharedBackend
import com.communityledger.app.shared.SharedBackendException
import com.communityledger.app.shared.SharedEntry
import com.communityledger.app.shared.SharedEvent
import com.communityledger.app.shared.SharedInvite
import com.communityledger.app.shared.SharedMember
import com.communityledger.app.shared.SharedPresence
import com.communityledger.app.shared.SharedPresenceState
import com.communityledger.app.shared.SharedPrivateEvidence
import com.communityledger.app.shared.SharedPublicEvent
import com.communityledger.app.shared.SharedRole
import com.communityledger.app.shared.SharedMutationResult
import com.communityledger.app.shared.SubmitSharedEntryRequest
import com.communityledger.app.shared.newSharedOperationKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.json.JSONObject
import org.json.JSONArray
import java.security.MessageDigest

sealed interface SelectedSharedMode {
    data object Loading : SelectedSharedMode
    data object LocalOnly : SelectedSharedMode
    data class Linked(val link: SharedEventLinkEntity) : SelectedSharedMode
}

data class SharedLiveEventState(
    val event: SharedEvent? = null,
    val members: List<SharedMember> = emptyList(),
    val entries: List<SharedEntry> = emptyList(),
    val presence: Map<String, SharedPresence> = emptyMap()
)

class SharedWorkspaceController(
    private val backend: SharedBackend,
    private val eventRepository: EventRepository,
    private val sharedSyncRepository: SharedSyncRepository,
    private val scope: CoroutineScope
) {
    val enabled: Boolean = backend.enabled

    private val _account = MutableStateFlow<SharedAccount?>(null)
    val account: StateFlow<SharedAccount?> = _account.asStateFlow()

    private val _publicEvents = MutableStateFlow<List<SharedPublicEvent>>(emptyList())
    val publicEvents: StateFlow<List<SharedPublicEvent>> = _publicEvents.asStateFlow()

    private val _selectedMode = MutableStateFlow<SelectedSharedMode>(SelectedSharedMode.LocalOnly)
    val selectedMode: StateFlow<SelectedSharedMode> = _selectedMode.asStateFlow()

    private val _liveEvent = MutableStateFlow(SharedLiveEventState())
    val liveEvent: StateFlow<SharedLiveEventState> = _liveEvent.asStateFlow()

    private val _busy = MutableStateFlow(false)
    val busy: StateFlow<Boolean> = _busy.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _lastInvite = MutableStateFlow<SharedInvite?>(null)
    val lastInvite: StateFlow<SharedInvite?> = _lastInvite.asStateFlow()

    private var publicEventsJob: Job? = null
    private var eventJob: Job? = null
    private var membersJob: Job? = null
    private var entriesJob: Job? = null
    private var presenceListenerJob: Job? = null
    private var presenceHeartbeatJob: Job? = null

    fun clearError() {
        _error.value = null
    }

    fun reportError(message: String) {
        _error.value = message.take(240)
    }

    fun clearInvite() {
        _lastInvite.value = null
    }

    suspend fun connect(displayName: String): Boolean = runBusy {
        val connected = backend.authenticate(displayName)
        _account.value = connected
        replayPendingOperations()
        startPublicEvents()
        val linked = _selectedMode.value as? SelectedSharedMode.Linked
        if (linked != null) startLiveEvent(linked.link)
        true
    } ?: false

    suspend fun createSharedEvent(
        title: String,
        duration: String?,
        isPrivate: Boolean,
        customInfo: Map<String, String>
    ): Int? = runBusy {
        requireAccount()
        val idempotencyKey = newSharedOperationKey()
        val now = System.currentTimeMillis()
        val localEvent = EventEntity(
            eventKey = newSharedOperationKey(),
            title = title.trim(),
            duration = duration?.trim()?.takeIf(String::isNotEmpty),
            isPrivate = isPrivate,
            customFieldsJson = JSONObject().apply {
                customInfo.forEach { (key, value) -> put(key, value) }
            }.toString()
        )
        val localEventId = withContext(Dispatchers.IO) {
            eventRepository.insertEvent(localEvent).toInt()
        }
        val payload = JSONObject().apply {
            put("title", localEvent.title)
            put("duration", localEvent.duration ?: JSONObject.NULL)
            put("isPrivate", isPrivate)
            put("customInfo", JSONObject(customInfo))
        }.toString()
        val operation = SharedPendingOperationEntity(
            idempotencyKey = idempotencyKey,
            localEventId = localEventId,
            remoteEventId = null,
            operationType = "createSharedEvent",
            payloadJson = payload,
            state = SharedOperationState.PENDING,
            attemptCount = 0,
            createdAt = now,
            updatedAt = now,
            lastError = null
        )
        withContext(Dispatchers.IO) { sharedSyncRepository.insertPendingOperation(operation) }

        try {
            withContext(Dispatchers.IO) {
                sharedSyncRepository.updatePendingOperationState(
                    idempotencyKey,
                    SharedOperationState.RUNNING,
                    1,
                    null
                )
            }
            val result = backend.createEvent(
                CreateSharedEventRequest(
                    idempotencyKey = idempotencyKey,
                    title = localEvent.title,
                    duration = localEvent.duration,
                    isPrivate = isPrivate,
                    customInfo = customInfo.mapValues { it.value }
                )
            )
            val linked = withContext(Dispatchers.IO) {
                sharedSyncRepository.linkEvent(
                    SharedEventLinkEntity(
                        localEventId = localEventId,
                        remoteEventId = result.eventId,
                        role = SharedRole.ORGANIZER.storageValue,
                        remoteRevision = result.revision,
                        syncState = SharedSyncState.ACTIVE,
                        lastServerConfirmedAt = System.currentTimeMillis(),
                        lastError = null
                    )
                )
            }
            if (!linked) throw SharedBackendException(
                "mapping-conflict",
                "The shared event is already linked to another local ledger."
            )
            withContext(Dispatchers.IO) { sharedSyncRepository.deletePendingOperation(idempotencyKey) }
            localEventId
        } catch (error: Exception) {
            withContext(Dispatchers.IO) {
                sharedSyncRepository.updatePendingOperationState(
                    idempotencyKey,
                    SharedOperationState.FAILED,
                    1,
                    error.safeSharedMessage()
                )
            }
            throw error
        }
    }

    suspend fun acceptInvite(inviteToken: String): Int? = runBusy {
        requireAccount()
        val cleanToken = inviteToken.trim()
        val idempotencyKey = newSharedOperationKey()
        val operation = newPendingOperation(
            idempotencyKey = idempotencyKey,
            localEventId = null,
            remoteEventId = null,
            operationType = "acceptInvite",
            payloadJson = JSONObject().put("inviteToken", cleanToken).toString()
        )
        persistAndRun(operation) {
            val result = backend.acceptInvite(cleanToken, idempotencyKey)
            materializeJoinedEvent(result.eventId, result.role ?: SharedRole.CONTRIBUTOR, result.revision)
        }
    }

    suspend fun joinPublicEvent(remoteEventId: String): Int? = runBusy {
        requireAccount()
        val idempotencyKey = newSharedOperationKey()
        val operation = newPendingOperation(
            idempotencyKey = idempotencyKey,
            localEventId = null,
            remoteEventId = remoteEventId,
            operationType = "joinPublicEvent",
            payloadJson = JSONObject().put("eventId", remoteEventId).toString()
        )
        persistAndRun(operation) {
            val result = backend.joinPublicEvent(remoteEventId, idempotencyKey)
            materializeJoinedEvent(result.eventId, result.role ?: SharedRole.VIEWER, result.revision)
        }
    }

    suspend fun createInvite(
        remoteEventId: String,
        role: SharedRole,
        expiresInSeconds: Int
    ): SharedInvite? = runBusy {
        requireAccount()
        val link = (_selectedMode.value as? SelectedSharedMode.Linked)?.link
            ?: throw SharedBackendException("not-linked", "Select a linked shared event first.")
        val idempotencyKey = newSharedOperationKey()
        val operation = newPendingOperation(
            idempotencyKey = idempotencyKey,
            localEventId = link.localEventId,
            remoteEventId = remoteEventId,
            operationType = "createInvite",
            payloadJson = JSONObject()
                .put("eventId", remoteEventId)
                .put("role", role.storageValue)
                .put("expiresInSeconds", expiresInSeconds)
                .toString()
        )
        persistAndRun(operation) {
            backend.createInvite(remoteEventId, role, expiresInSeconds, idempotencyKey)
                .also { _lastInvite.value = it }
        }
    }

    suspend fun submitEntry(request: SubmitSharedEntryRequest): SharedMutationResult? = runBusy {
        val link = (_selectedMode.value as? SelectedSharedMode.Linked)?.link
            ?: throw SharedBackendException("not-linked", "Select a linked shared event first.")
        requireAccount()
        if (request.eventId != link.remoteEventId) {
            throw SharedBackendException("event-mismatch", "Receipt does not belong to the selected shared event.")
        }
        val now = System.currentTimeMillis()
        val operation = SharedPendingOperationEntity(
            idempotencyKey = request.idempotencyKey,
            localEventId = link.localEventId,
            remoteEventId = link.remoteEventId,
            operationType = "submitReviewedEntry",
            payloadJson = request.toPendingJson(),
            state = SharedOperationState.PENDING,
            attemptCount = 0,
            createdAt = now,
            updatedAt = now,
            lastError = null
        )
        withContext(Dispatchers.IO) { sharedSyncRepository.insertPendingOperation(operation) }
        try {
            withContext(Dispatchers.IO) {
                sharedSyncRepository.updatePendingOperationState(
                    request.idempotencyKey,
                    SharedOperationState.RUNNING,
                    1,
                    null
                )
            }
            backend.submitEntry(request).also {
                withContext(Dispatchers.IO) {
                    sharedSyncRepository.deletePendingOperation(request.idempotencyKey)
                }
            }
        } catch (error: Exception) {
            withContext(Dispatchers.IO) {
                sharedSyncRepository.updatePendingOperationState(
                    request.idempotencyKey,
                    SharedOperationState.FAILED,
                    1,
                    error.safeSharedMessage()
                )
            }
            throw error
        }
    }

    suspend fun reviewEntry(
        entryId: String,
        confirm: Boolean,
        reason: String?
    ): SharedMutationResult? = runBusy {
        val link = (_selectedMode.value as? SelectedSharedMode.Linked)?.link
            ?: throw SharedBackendException("not-linked", "Select a linked shared event first.")
        val event = _liveEvent.value.event
            ?: throw SharedBackendException("not-ready", "Shared event state is not ready.")
        requireAccount()
        val idempotencyKey = newSharedOperationKey()
        val now = System.currentTimeMillis()
        val payload = JSONObject().apply {
            put("eventId", link.remoteEventId)
            put("entryId", entryId)
            put("expectedRevision", event.revision)
            put("decision", if (confirm) "confirm" else "reject")
            put("reason", reason ?: JSONObject.NULL)
        }.toString()
        withContext(Dispatchers.IO) {
            sharedSyncRepository.insertPendingOperation(
                SharedPendingOperationEntity(
                    idempotencyKey = idempotencyKey,
                    localEventId = link.localEventId,
                    remoteEventId = link.remoteEventId,
                    operationType = "reviewPendingEntry",
                    payloadJson = payload,
                    state = SharedOperationState.PENDING,
                    attemptCount = 0,
                    createdAt = now,
                    updatedAt = now,
                    lastError = null
                )
            )
            sharedSyncRepository.updatePendingOperationState(
                idempotencyKey,
                SharedOperationState.RUNNING,
                1,
                null
            )
        }
        try {
            backend.reviewEntry(
                eventId = link.remoteEventId,
                entryId = entryId,
                expectedRevision = event.revision,
                confirm = confirm,
                reason = reason,
                idempotencyKey = idempotencyKey
            ).also {
                withContext(Dispatchers.IO) { sharedSyncRepository.deletePendingOperation(idempotencyKey) }
            }
        } catch (error: Exception) {
            withContext(Dispatchers.IO) {
                sharedSyncRepository.updatePendingOperationState(
                    idempotencyKey,
                    SharedOperationState.FAILED,
                    1,
                    error.safeSharedMessage()
                )
            }
            throw error
        }
    }

    suspend fun loadPrivateEvidence(entryId: String): SharedPrivateEvidence? = runBusy {
        val link = (_selectedMode.value as? SelectedSharedMode.Linked)?.link
            ?: throw SharedBackendException("not-linked", "Select a linked shared event first.")
        requireAccount()
        backend.loadPrivateEvidence(link.remoteEventId, entryId)
    }

    fun selectLocalEvent(localEventId: Int) {
        _selectedMode.value = SelectedSharedMode.Loading
        stopLiveEvent()
        scope.launch(Dispatchers.IO) {
            val link = sharedSyncRepository.getEventLink(localEventId)
            withContext(Dispatchers.Main) {
                if (link == null) {
                    _selectedMode.value = SelectedSharedMode.LocalOnly
                } else {
                    _selectedMode.value = SelectedSharedMode.Linked(link)
                    startLiveEvent(link)
                }
            }
        }
    }

    fun enterSelectedSharedEvent() {
        val link = (_selectedMode.value as? SelectedSharedMode.Linked)?.link ?: return
        if (_account.value == null || presenceHeartbeatJob?.isActive == true) return
        presenceHeartbeatJob = scope.launch {
            while (true) {
                runCatching { backend.publishPresence(link.remoteEventId, SharedPresenceState.ACTIVE) }
                    .onSuccess {
                        val memberUids = _liveEvent.value.members.map(SharedMember::uid)
                        if (memberUids.isNotEmpty()) {
                            startPresenceListener(link.remoteEventId, memberUids)
                        }
                    }
                    .onFailure { setError(it) }
                delay(PRESENCE_REFRESH_MILLIS)
            }
        }
    }

    fun leaveSelectedSharedEvent() {
        val link = (_selectedMode.value as? SelectedSharedMode.Linked)?.link
        presenceHeartbeatJob?.cancel()
        presenceHeartbeatJob = null
        if (link != null && _account.value != null) {
            scope.launch {
                runCatching {
                    backend.publishPresence(link.remoteEventId, SharedPresenceState.RECENTLY_ACTIVE)
                }
            }
        }
    }

    private fun startPublicEvents() {
        publicEventsJob?.cancel()
        publicEventsJob = scope.launch {
            backend.observePublicEvents()
                .catch { setError(it) }
                .collect { _publicEvents.value = it }
        }
    }

    private fun startLiveEvent(link: SharedEventLinkEntity) {
        stopLiveEvent()
        val account = _account.value
        if (account == null) {
            _liveEvent.value = SharedLiveEventState()
            _error.value = "Connect a shared profile to load this event."
            return
        }
        val role = link.role.toSharedRoleOrNull() ?: run {
            _error.value = "Stored shared role is invalid."
            return
        }
        eventJob = launchProtectedListener(link, backend.observeEvent(link.remoteEventId)) { event ->
            if (!isBlocked(link.localEventId)) {
                    _liveEvent.value = _liveEvent.value.copy(event = event)
                    withContext(Dispatchers.IO) {
                        val currentLink = sharedSyncRepository.getEventLink(link.localEventId) ?: link
                        sharedSyncRepository.updateEventLinkState(
                            localEventId = link.localEventId,
                            role = currentLink.role,
                            remoteRevision = event.revision,
                            syncState = if (event.fromCache) SharedSyncState.STALE else SharedSyncState.ACTIVE,
                            lastServerConfirmedAt = if (event.fromCache) currentLink.lastServerConfirmedAt else System.currentTimeMillis(),
                            lastError = null
                        )
                    }
                }
        }
        membersJob = launchProtectedListener(link, backend.observeMembers(link.remoteEventId)) { members ->
            if (!isBlocked(link.localEventId)) {
                    val ownMembership = members.firstOrNull { it.uid == account.uid }
                    if (ownMembership == null) {
                        blockLinkedEvent(
                            link,
                            SharedBackendException("permission-denied", "Active shared membership is no longer available.")
                        )
                        return@launchProtectedListener
                    }
                    val currentLink = (_selectedMode.value as? SelectedSharedMode.Linked)?.link ?: link
                    val currentRole = ownMembership.role.storageValue
                    if (currentLink.role != currentRole) {
                        val updatedLink = currentLink.copy(role = currentRole, lastError = null)
                        withContext(Dispatchers.IO) {
                            sharedSyncRepository.updateEventLinkState(
                                localEventId = updatedLink.localEventId,
                                role = updatedLink.role,
                                remoteRevision = updatedLink.remoteRevision,
                                syncState = updatedLink.syncState,
                                lastServerConfirmedAt = updatedLink.lastServerConfirmedAt,
                                lastError = null
                            )
                        }
                        _selectedMode.value = SelectedSharedMode.Linked(updatedLink)
                        startEntriesListener(updatedLink, account)
                    }
                    _liveEvent.value = _liveEvent.value.copy(members = members)
                    startPresenceListener(link.remoteEventId, members.map(SharedMember::uid))
                }
        }
        startEntriesListener(link.copy(role = role.storageValue), account)
    }

    private fun startEntriesListener(link: SharedEventLinkEntity, account: SharedAccount) {
        entriesJob?.cancel()
        val role = link.role.toSharedRoleOrNull() ?: return
        entriesJob = launchProtectedListener(
            link,
            backend.observeEntries(link.remoteEventId, role, account.uid)
        ) { entries ->
            if (!isBlocked(link.localEventId)) {
                _liveEvent.value = _liveEvent.value.copy(entries = entries)
            }
        }
    }

    private fun startPresenceListener(remoteEventId: String, memberUids: List<String>) {
        presenceListenerJob?.cancel()
        presenceListenerJob = scope.launch {
            backend.observePresence(remoteEventId, memberUids)
                .catch { setError(it) }
                .collect { presence -> _liveEvent.value = _liveEvent.value.copy(presence = presence) }
        }
    }

    private fun <T> launchProtectedListener(
        link: SharedEventLinkEntity,
        source: Flow<T>,
        onValue: suspend (T) -> Unit
    ): Job = scope.launch {
        source
            .retryWhen { error, attempt ->
                if (error.isPermissionFailure()) {
                    blockLinkedEvent(link, error)
                    false
                } else if (attempt < MAX_LISTENER_RETRIES) {
                    markLinkedEventStale(link, error)
                    delay((LISTENER_RETRY_BASE_MILLIS * (attempt + 1)).coerceAtMost(LISTENER_RETRY_MAX_MILLIS))
                    true
                } else {
                    false
                }
            }
            .catch { error ->
                if (error.isPermissionFailure()) blockLinkedEvent(link, error)
                else markLinkedEventStale(link, error)
            }
            .collect(onValue)
    }

    private suspend fun blockLinkedEvent(link: SharedEventLinkEntity, error: Throwable) {
        val message = error.safeSharedMessage()
        withContext(Dispatchers.IO) {
            sharedSyncRepository.updateEventLinkState(
                localEventId = link.localEventId,
                role = link.role,
                remoteRevision = link.remoteRevision,
                syncState = SharedSyncState.BLOCKED,
                lastServerConfirmedAt = link.lastServerConfirmedAt,
                lastError = message
            )
        }
        _selectedMode.value = SelectedSharedMode.Linked(
            link.copy(syncState = SharedSyncState.BLOCKED, lastError = message)
        )
        _liveEvent.value = SharedLiveEventState()
        presenceHeartbeatJob?.cancel()
        presenceHeartbeatJob = null
        setError(error)
    }

    private suspend fun markLinkedEventStale(link: SharedEventLinkEntity, error: Throwable) {
        if (isBlocked(link.localEventId)) return
        val message = error.safeSharedMessage()
        withContext(Dispatchers.IO) {
            sharedSyncRepository.updateEventLinkState(
                localEventId = link.localEventId,
                role = link.role,
                remoteRevision = _liveEvent.value.event?.revision ?: link.remoteRevision,
                syncState = SharedSyncState.STALE,
                lastServerConfirmedAt = link.lastServerConfirmedAt,
                lastError = message
            )
        }
        _selectedMode.value = SelectedSharedMode.Linked(
            link.copy(syncState = SharedSyncState.STALE, lastError = message)
        )
        setError(error)
    }

    private fun isBlocked(localEventId: Int): Boolean =
        ((_selectedMode.value as? SelectedSharedMode.Linked)?.link?.takeIf {
            it.localEventId == localEventId
        }?.syncState == SharedSyncState.BLOCKED)

    private fun stopLiveEvent() {
        eventJob?.cancel()
        membersJob?.cancel()
        entriesJob?.cancel()
        presenceListenerJob?.cancel()
        presenceHeartbeatJob?.cancel()
        eventJob = null
        membersJob = null
        entriesJob = null
        presenceListenerJob = null
        presenceHeartbeatJob = null
        _liveEvent.value = SharedLiveEventState()
    }

    private suspend fun materializeJoinedEvent(
        remoteEventId: String,
        role: SharedRole,
        revision: Long
    ): Int {
        val existingLink = withContext(Dispatchers.IO) {
            sharedSyncRepository.getEventLink(remoteEventId)
        }
        if (existingLink != null) return existingLink.localEventId

        val remoteEvent = withTimeout(REMOTE_EVENT_LOAD_TIMEOUT_MILLIS) {
            backend.observeEvent(remoteEventId).first()
        }
        val eventKey = sha256Hex("shared:$remoteEventId").take(32)
        val existingEvent = withContext(Dispatchers.IO) {
            eventRepository.getEventByEventKeyOnce(eventKey)
        }
        val localEventId = if (existingEvent != null) {
            existingEvent.id
        } else {
            val customFields = JSONObject().apply {
                put("sharedRemoteEventId", remoteEventId)
                remoteEvent.customInfo.forEach { (key, value) -> put(key, value ?: JSONObject.NULL) }
            }
            val inserted = withContext(Dispatchers.IO) {
                eventRepository.insertEventIfAbsent(
                    EventEntity(
                        eventKey = eventKey,
                        title = remoteEvent.title,
                        duration = remoteEvent.duration,
                        isPrivate = remoteEvent.visibilityPolicy == "private",
                        customFieldsJson = customFields.toString()
                    )
                )
            }
            if (inserted > 0) inserted.toInt() else withContext(Dispatchers.IO) {
                eventRepository.getEventByEventKeyOnce(eventKey)?.id
            } ?: throw SharedBackendException("mapping-conflict", "Could not create a local shared-event shell.")
        }
        val linked = withContext(Dispatchers.IO) {
            sharedSyncRepository.linkEvent(
                SharedEventLinkEntity(
                    localEventId = localEventId,
                    remoteEventId = remoteEventId,
                    role = role.storageValue,
                    remoteRevision = revision,
                    syncState = if (remoteEvent.fromCache) SharedSyncState.STALE else SharedSyncState.ACTIVE,
                    lastServerConfirmedAt = if (remoteEvent.fromCache) null else System.currentTimeMillis(),
                    lastError = null
                )
            )
        }
        if (!linked) throw SharedBackendException(
            "mapping-conflict",
            "This shared event is already linked to another local ledger."
        )
        return localEventId
    }

    private suspend fun replayPendingOperations() {
        val operations = withContext(Dispatchers.IO) {
            sharedSyncRepository.getRetryableOperations(MAX_REPLAY_OPERATIONS)
        }
        operations.forEach { operation ->
            if (operation.attemptCount >= MAX_OPERATION_ATTEMPTS) {
                withContext(Dispatchers.IO) {
                    sharedSyncRepository.updatePendingOperationState(
                        operation.idempotencyKey,
                        SharedOperationState.BLOCKED,
                        operation.attemptCount,
                        "Retry limit reached. Review this operation before trying again."
                    )
                }
                return@forEach
            }
            val nextAttempt = operation.attemptCount + 1
            try {
                withContext(Dispatchers.IO) {
                    sharedSyncRepository.updatePendingOperationState(
                        operation.idempotencyKey,
                        SharedOperationState.RUNNING,
                        nextAttempt,
                        null
                    )
                }
                replayOperation(operation)
                withContext(Dispatchers.IO) {
                    sharedSyncRepository.deletePendingOperation(operation.idempotencyKey)
                }
            } catch (error: Exception) {
                val blocked = error.isPermanentSharedFailure()
                withContext(Dispatchers.IO) {
                    sharedSyncRepository.updatePendingOperationState(
                        operation.idempotencyKey,
                        if (blocked) SharedOperationState.BLOCKED else SharedOperationState.FAILED,
                        nextAttempt,
                        error.safeSharedMessage()
                    )
                }
                setError(error)
            }
        }
    }

    private suspend fun replayOperation(operation: SharedPendingOperationEntity) {
        val payload = try {
            JSONObject(operation.payloadJson)
        } catch (error: Exception) {
            throw SharedBackendException("invalid-local-operation", "Saved shared operation is damaged.", error)
        }
        when (operation.operationType) {
            "createSharedEvent" -> {
                val localEventId = operation.localEventId
                    ?: throw SharedBackendException("invalid-local-operation", "Saved event creation has no local event.")
                val customInfo = payload.requireStringMap("customInfo")
                val result = backend.createEvent(
                    CreateSharedEventRequest(
                        idempotencyKey = operation.idempotencyKey,
                        title = payload.requireText("title"),
                        duration = payload.optionalText("duration"),
                        isPrivate = payload.getBoolean("isPrivate"),
                        customInfo = customInfo
                    )
                )
                val linked = withContext(Dispatchers.IO) {
                    sharedSyncRepository.linkEvent(
                        SharedEventLinkEntity(
                            localEventId = localEventId,
                            remoteEventId = result.eventId,
                            role = SharedRole.ORGANIZER.storageValue,
                            remoteRevision = result.revision,
                            syncState = SharedSyncState.ACTIVE,
                            lastServerConfirmedAt = System.currentTimeMillis(),
                            lastError = null
                        )
                    )
                }
                if (!linked) throw SharedBackendException("mapping-conflict", "Recovered event mapping conflicts with another ledger.")
            }
            "acceptInvite" -> {
                val result = backend.acceptInvite(
                    payload.requireText("inviteToken"),
                    operation.idempotencyKey
                )
                materializeJoinedEvent(result.eventId, result.role ?: SharedRole.CONTRIBUTOR, result.revision)
            }
            "joinPublicEvent" -> {
                val eventId = payload.requireText("eventId")
                val result = backend.joinPublicEvent(eventId, operation.idempotencyKey)
                materializeJoinedEvent(result.eventId, result.role ?: SharedRole.VIEWER, result.revision)
            }
            "createInvite" -> {
                val invite = backend.createInvite(
                    eventId = payload.requireText("eventId"),
                    role = payload.requireText("role").toSharedRoleOrNull()
                        ?: throw SharedBackendException("invalid-local-operation", "Saved invite role is invalid."),
                    expiresInSeconds = payload.getInt("expiresInSeconds"),
                    idempotencyKey = operation.idempotencyKey
                )
                _lastInvite.value = invite
            }
            "submitReviewedEntry" -> backend.submitEntry(payload.toSubmitRequest(operation.idempotencyKey))
            "reviewPendingEntry" -> backend.reviewEntry(
                eventId = payload.requireText("eventId"),
                entryId = payload.requireText("entryId"),
                expectedRevision = payload.getLong("expectedRevision"),
                confirm = payload.requireText("decision") == "confirm",
                reason = payload.optionalText("reason"),
                idempotencyKey = operation.idempotencyKey
            )
            else -> throw SharedBackendException("invalid-local-operation", "Saved shared operation type is unsupported.")
        }
    }

    private suspend fun <T> persistAndRun(
        operation: SharedPendingOperationEntity,
        action: suspend () -> T
    ): T {
        withContext(Dispatchers.IO) {
            sharedSyncRepository.insertPendingOperation(operation)
            sharedSyncRepository.updatePendingOperationState(
                operation.idempotencyKey,
                SharedOperationState.RUNNING,
                1,
                null
            )
        }
        return try {
            action().also {
                withContext(Dispatchers.IO) {
                    sharedSyncRepository.deletePendingOperation(operation.idempotencyKey)
                }
            }
        } catch (error: Exception) {
            withContext(Dispatchers.IO) {
                sharedSyncRepository.updatePendingOperationState(
                    operation.idempotencyKey,
                    if (error.isPermanentSharedFailure()) SharedOperationState.BLOCKED else SharedOperationState.FAILED,
                    1,
                    error.safeSharedMessage()
                )
            }
            throw error
        }
    }

    private fun newPendingOperation(
        idempotencyKey: String,
        localEventId: Int?,
        remoteEventId: String?,
        operationType: String,
        payloadJson: String
    ): SharedPendingOperationEntity {
        val now = System.currentTimeMillis()
        return SharedPendingOperationEntity(
            idempotencyKey = idempotencyKey,
            localEventId = localEventId,
            remoteEventId = remoteEventId,
            operationType = operationType,
            payloadJson = payloadJson,
            state = SharedOperationState.PENDING,
            attemptCount = 0,
            createdAt = now,
            updatedAt = now,
            lastError = null
        )
    }

    private fun requireAccount(): SharedAccount = _account.value
        ?: throw SharedBackendException("unauthenticated", "Connect a shared profile first.")

    private suspend fun <T> runBusy(block: suspend () -> T): T? {
        if (_busy.value) return null
        _busy.value = true
        _error.value = null
        return try {
            block()
        } catch (error: Exception) {
            setError(error)
            null
        } finally {
            _busy.value = false
        }
    }

    private fun setError(error: Throwable) {
        _error.value = error.safeSharedMessage()
    }

    private fun Throwable.safeSharedMessage(): String = when (this) {
        is SharedBackendException -> message.orEmpty().ifBlank { "Shared operation failed." }
        is IllegalArgumentException -> message.orEmpty().ifBlank { "Shared input is invalid." }
        else -> "Shared service is unavailable. Check the local emulator and try again."
    }.take(240)

    private fun Throwable.isPermanentSharedFailure(): Boolean = this is SharedBackendException && code in setOf(
        "invalid-argument",
        "failed-precondition",
        "permission-denied",
        "not-found",
        "already-exists",
        "aborted",
        "idempotency-conflict",
        "invalid-local-operation",
        "mapping-conflict"
    )

    private fun Throwable.isPermissionFailure(): Boolean =
        (this is SharedBackendException && code == "permission-denied") ||
            message.orEmpty().contains("PERMISSION_DENIED", ignoreCase = true)

    private fun sha256Hex(value: String): String = MessageDigest.getInstance("SHA-256")
        .digest(value.toByteArray())
        .joinToString("") { "%02x".format(it) }

    private fun String.toSharedRoleOrNull(): SharedRole? = when (this) {
        "organizer" -> SharedRole.ORGANIZER
        "contributor" -> SharedRole.CONTRIBUTOR
        "viewer" -> SharedRole.VIEWER
        else -> null
    }

    private val SharedRole.storageValue: String
        get() = name.lowercase()

    private fun SubmitSharedEntryRequest.toPendingJson(): String = JSONObject().apply {
        put("eventId", eventId)
        put("ledgerPersonUid", ledgerPersonUid)
        put("ledgerType", ledgerType)
        put("amountMinor", amountMinor)
        put("amountEvidenceSource", amountEvidenceSource)
        put("amountEvidenceConfidence", amountEvidenceConfidence)
        put("expectedRevision", expectedRevision)
        put("paymentReference", paymentReference ?: JSONObject.NULL)
        put("paymentDate", paymentDate ?: JSONObject.NULL)
        put("paymentApp", paymentApp ?: JSONObject.NULL)
        put("counterparty", counterparty ?: JSONObject.NULL)
        put("confidence", confidence)
        put("warnings", org.json.JSONArray(warnings))
    }.toString()

    private fun JSONObject.toSubmitRequest(idempotencyKey: String): SubmitSharedEntryRequest =
        SubmitSharedEntryRequest(
            idempotencyKey = idempotencyKey,
            eventId = requireText("eventId"),
            ledgerPersonUid = requireText("ledgerPersonUid"),
            ledgerType = requireText("ledgerType"),
            amountMinor = getLong("amountMinor"),
            amountEvidenceSource = requireText("amountEvidenceSource"),
            amountEvidenceConfidence = getInt("amountEvidenceConfidence"),
            expectedRevision = getLong("expectedRevision"),
            paymentReference = optionalText("paymentReference"),
            paymentDate = optionalText("paymentDate"),
            paymentApp = optionalText("paymentApp"),
            counterparty = optionalText("counterparty"),
            confidence = getInt("confidence"),
            warnings = getJSONArray("warnings").toStringList()
        )

    private fun JSONObject.requireText(name: String): String = getString(name)
        .trim()
        .takeIf(String::isNotEmpty)
        ?: throw SharedBackendException("invalid-local-operation", "Saved shared field '$name' is missing.")

    private fun JSONObject.optionalText(name: String): String? = when {
        !has(name) || isNull(name) -> null
        else -> getString(name).trim().takeIf(String::isNotEmpty)
    }

    private fun JSONObject.requireStringMap(name: String): Map<String, String?> {
        val source = getJSONObject(name)
        return source.keys().asSequence().associateWith { key ->
            if (source.isNull(key)) null else source.getString(key)
        }
    }

    private fun JSONArray.toStringList(): List<String> = (0 until length()).map { index -> getString(index) }

    private companion object {
        const val PRESENCE_REFRESH_MILLIS = 90_000L
        const val REMOTE_EVENT_LOAD_TIMEOUT_MILLIS = 15_000L
        const val MAX_REPLAY_OPERATIONS = 50
        const val MAX_OPERATION_ATTEMPTS = 5
        const val MAX_LISTENER_RETRIES = 5L
        const val LISTENER_RETRY_BASE_MILLIS = 500L
        const val LISTENER_RETRY_MAX_MILLIS = 5_000L
    }
}