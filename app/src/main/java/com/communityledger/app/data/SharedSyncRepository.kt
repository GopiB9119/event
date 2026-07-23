package com.communityledger.app.data

import kotlinx.coroutines.flow.Flow

class SharedSyncRepository(private val sharedSyncDao: SharedSyncDao) {
    fun observeEventLink(localEventId: Int): Flow<SharedEventLinkEntity?> =
        sharedSyncDao.observeEventLink(localEventId)

    fun observePendingOperations(localEventId: Int): Flow<List<SharedPendingOperationEntity>> =
        sharedSyncDao.observePendingOperations(localEventId)

    suspend fun getEventLink(localEventId: Int): SharedEventLinkEntity? =
        sharedSyncDao.getEventLinkByLocalIdOnce(localEventId)

    suspend fun getEventLink(remoteEventId: String): SharedEventLinkEntity? =
        sharedSyncDao.getEventLinkByRemoteIdOnce(remoteEventId)

    suspend fun linkEvent(link: SharedEventLinkEntity): Boolean = sharedSyncDao.linkEvent(link)

    suspend fun updateEventLinkState(
        localEventId: Int,
        role: String,
        remoteRevision: Long,
        syncState: String,
        lastServerConfirmedAt: Long?,
        lastError: String?
    ): Boolean = sharedSyncDao.updateEventLinkState(
        localEventId,
        role,
        remoteRevision,
        syncState,
        lastServerConfirmedAt,
        lastError
    ) == 1

    suspend fun insertPendingOperation(operation: SharedPendingOperationEntity) =
        sharedSyncDao.insertPendingOperation(operation)

    suspend fun getRetryableOperations(limit: Int = 50): List<SharedPendingOperationEntity> =
        sharedSyncDao.getRetryableOperations(limit.coerceIn(1, 50))

    suspend fun updatePendingOperationState(
        idempotencyKey: String,
        state: String,
        attemptCount: Int,
        lastError: String?
    ): Boolean = sharedSyncDao.updatePendingOperationState(
        idempotencyKey,
        state,
        attemptCount,
        System.currentTimeMillis(),
        lastError
    ) == 1

    suspend fun deletePendingOperation(idempotencyKey: String): Boolean =
        sharedSyncDao.deletePendingOperation(idempotencyKey) == 1
}