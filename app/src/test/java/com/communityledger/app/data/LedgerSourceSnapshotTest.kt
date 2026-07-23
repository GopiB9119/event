package com.communityledger.app.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class LedgerSourceSnapshotTest {
    private lateinit var database: AppDatabase
    private lateinit var eventDao: EventDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        eventDao = database.eventDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun capturesAllSourceRowsInStablePrimaryKeyOrder() = runBlocking {
        eventDao.insertEvent(event(id = 20, key = "22222222222222222222222222222222"))
        eventDao.insertEvent(event(id = 10, key = "11111111111111111111111111111111"))
        eventDao.insertMember(member(id = 200, eventId = 20))
        eventDao.insertMember(member(id = 100, eventId = 10))
        eventDao.insertTransaction(transaction(id = 2000, eventId = 20, memberId = 200))
        eventDao.insertTransaction(transaction(id = 1000, eventId = 10, memberId = 100))

        val snapshot = eventDao.captureLedgerSourceSnapshot()

        assertEquals(listOf(10, 20), snapshot.events.map { it.id })
        assertEquals(listOf(100, 200), snapshot.members.map { it.id })
        assertEquals(listOf(1000, 2000), snapshot.transactions.map { it.id })
    }

    @Test
    fun aggregateProjectionExcludesReplacementRowWithoutLoadingNotes() = runBlocking {
        eventDao.insertEvent(event(id = 30, key = "33333333333333333333333333333333"))
        eventDao.insertTransaction(transaction(id = 3000, eventId = 30, memberId = 0).copy(memberId = null, amount = 500.0))
        eventDao.insertTransaction(
            transaction(id = 3001, eventId = 30, memberId = 0).copy(
                memberId = null,
                amount = 125.0,
                type = "Expense"
            )
        )

        val projection = eventDao.getLedgerAggregateProjection(eventId = 30, excludedTransactionId = 3000)

        assertEquals(0.0, projection.totalCollected, 0.0)
        assertEquals(125.0, projection.totalSpent, 0.0)
        assertEquals(0L, projection.invalidRowCount)
    }

    @Test
    fun portableSnapshotExcludesLinkedSharedEventShellsAndChildren() = runBlocking {
        eventDao.insertEvent(event(id = 60, key = "66666666666666666666666666666666"))
        eventDao.insertEvent(event(id = 61, key = "77777777777777777777777777777777"))
        eventDao.insertMember(member(id = 600, eventId = 60))
        eventDao.insertMember(member(id = 610, eventId = 61))
        eventDao.insertTransaction(transaction(id = 6000, eventId = 60, memberId = 600))
        eventDao.insertTransaction(transaction(id = 6100, eventId = 61, memberId = 610))
        database.sharedSyncDao().linkEvent(
            SharedEventLinkEntity(
                localEventId = 61,
                remoteEventId = "AbCdEfGhIjKlMnOpQrSt",
                role = "viewer",
                remoteRevision = 2,
                syncState = SharedSyncState.ACTIVE,
                lastServerConfirmedAt = 1_720_000_000_000L,
                lastError = null
            )
        )

        val snapshot = eventDao.captureLedgerSourceSnapshot()

        assertEquals(listOf(60), snapshot.events.map { it.id })
        assertEquals(listOf(600), snapshot.members.map { it.id })
        assertEquals(listOf(6000), snapshot.transactions.map { it.id })
    }

    @Test
    fun rejectsCrossEventReplacementAndPreservesOriginalReceiptReference() = runBlocking {
        eventDao.insertEvent(event(id = 40, key = "44444444444444444444444444444444"))
        eventDao.insertEvent(event(id = 41, key = "55555555555555555555555555555555"))
        val original = transaction(id = 4000, eventId = 40, memberId = 0).copy(
            memberId = null,
            notes = """{"receiptFilePath":"/private/event_40/receipt.json"}"""
        )
        assertEquals(4000L, eventDao.insertTransaction(original))

        val result = eventDao.insertTransaction(
            original.copy(eventId = 41, notes = null, amount = 99.0)
        )

        assertEquals(-1L, result)
        assertEquals(original, eventDao.getTransactionByIdOnce(original.id))
        assertTrue(eventDao.getTransactionsForEventOnce(41).isEmpty())
    }

    @Test
    fun rejectsOversizedSourceBeforeMaterializingSnapshotRows() = runBlocking {
        eventDao.insertEvent(event(id = 20, key = "22222222222222222222222222222222"))
        eventDao.insertEvent(event(id = 10, key = "11111111111111111111111111111111"))

        var rejected = false
        try {
            eventDao.captureLedgerSourceSnapshot(
                maxEvents = 1,
                maxMembers = MAX_LEDGER_SOURCE_MEMBERS,
                maxTransactions = MAX_LEDGER_SOURCE_TRANSACTIONS
            )
        } catch (error: LedgerSourceSnapshotLimitException) {
            rejected = true
        }

        assertTrue(rejected)
    }

    private fun event(id: Int, key: String) = EventEntity(
        id = id,
        eventKey = key,
        title = "Event $id",
        customFieldsJson = "{}"
    )

    private fun member(id: Int, eventId: Int) = MemberEntity(
        id = id,
        eventId = eventId,
        name = "Member $id",
        normalizedName = "member $id"
    )

    private fun transaction(id: Int, eventId: Int, memberId: Int) = TransactionEntity(
        id = id,
        eventId = eventId,
        memberId = memberId,
        personName = "Member $memberId",
        personPhone = "",
        personEmail = "",
        amount = 10.0,
        type = "Donated"
    )
}