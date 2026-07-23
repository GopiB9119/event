package com.communityledger.app.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class SharedSyncPersistenceTest {
    private lateinit var database: AppDatabase
    private lateinit var eventDao: EventDao
    private lateinit var sharedSyncDao: SharedSyncDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        eventDao = database.eventDao()
        sharedSyncDao = database.sharedSyncDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun oneRemoteEventCannotAttachToTwoLocalLedgers() = runBlocking {
        eventDao.insertEvent(event(1))
        eventDao.insertEvent(event(2))
        val first = link(localEventId = 1, remoteEventId = "AbCdEfGhIjKlMnOpQrSt")

        assertTrue(sharedSyncDao.linkEvent(first))
        assertTrue(sharedSyncDao.linkEvent(first))
        assertFalse(sharedSyncDao.linkEvent(link(2, "AbCdEfGhIjKlMnOpQrSt")))
        assertNull(sharedSyncDao.getEventLinkByLocalIdOnce(2))
    }

    @Test
    fun eventDeletionCascadesSharedMappingAndPendingWork() = runBlocking {
        eventDao.insertEvent(event(3))
        assertTrue(sharedSyncDao.linkEvent(link(3, "ZyXwVuTsRqPoNmLkJiHg")))
        val operation = SharedPendingOperationEntity(
            idempotencyKey = "11111111111111111111111111111111",
            localEventId = 3,
            remoteEventId = "ZyXwVuTsRqPoNmLkJiHg",
            operationType = "submitReviewedEntry",
            payloadJson = "{}",
            state = SharedOperationState.PENDING,
            attemptCount = 0,
            createdAt = 1_720_000_000_000L,
            updatedAt = 1_720_000_000_000L,
            lastError = null
        )
        sharedSyncDao.insertPendingOperation(operation)

        eventDao.deleteEventById(3)

        assertNull(sharedSyncDao.getEventLinkByLocalIdOnce(3))
        assertEquals(emptyList<SharedPendingOperationEntity>(), sharedSyncDao.getRetryableOperations(50))
    }

    @Test
    fun staleRunningOperationIsReturnedForRecovery() = runBlocking {
        eventDao.insertEvent(event(4))
        sharedSyncDao.insertPendingOperation(
            SharedPendingOperationEntity(
                idempotencyKey = "22222222222222222222222222222222",
                localEventId = 4,
                remoteEventId = null,
                operationType = "createSharedEvent",
                payloadJson = "{}",
                state = SharedOperationState.RUNNING,
                attemptCount = 1,
                createdAt = 1_720_000_000_000L,
                updatedAt = 1_720_000_000_100L,
                lastError = null
            )
        )

        assertEquals(
            listOf("22222222222222222222222222222222"),
            sharedSyncDao.getRetryableOperations(50).map { it.idempotencyKey }
        )
    }

    private fun event(id: Int) = EventEntity(
        id = id,
        eventKey = id.toString().padStart(32, '0'),
        title = "Event $id",
        customFieldsJson = "{}"
    )

    private fun link(localEventId: Int, remoteEventId: String) = SharedEventLinkEntity(
        localEventId = localEventId,
        remoteEventId = remoteEventId,
        role = "organizer",
        remoteRevision = 1,
        syncState = SharedSyncState.ACTIVE,
        lastServerConfirmedAt = 1_720_000_000_000L,
        lastError = null
    )
}