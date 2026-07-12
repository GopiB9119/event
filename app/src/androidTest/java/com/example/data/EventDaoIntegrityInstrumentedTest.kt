package com.example.data

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EventDaoIntegrityInstrumentedTest {
    private lateinit var database: AppDatabase
    private lateinit var eventDao: EventDao

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
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
    fun conflictingInviteIdCannotReplaceLocalLedger() = runBlocking {
        eventDao.insertEvent(
            EventEntity(
                id = 7,
                title = "Local Event",
                isPrivate = true,
                customFieldsJson = "{}"
            )
        )
        eventDao.insertTransaction(
            TransactionEntity(
                eventId = 7,
                personName = "Test Member",
                personPhone = "",
                personEmail = "",
                amount = 100.0,
                type = "Donated"
            )
        )

        val insertResult = eventDao.insertEventIfAbsent(
            EventEntity(
                id = 7,
                title = "Incoming Event",
                isPrivate = false,
                customFieldsJson = "{}"
            )
        )

        assertEquals(-1L, insertResult)
        val preservedEvent = eventDao.getEventByIdOnce(7)
        assertNotNull(preservedEvent)
        assertEquals("Local Event", preservedEvent?.title)
        assertEquals(true, preservedEvent?.isPrivate)
        assertEquals(1, eventDao.getTransactionsForEvent(7).first().size)
    }

    @Test
    fun duplicateEventKeyCannotReplaceLedgerOrDeleteTransaction() = runBlocking {
        val eventKey = "0123456789abcdef0123456789abcdef"
        val originalId = eventDao.insertEventIfAbsent(
            EventEntity(
                eventKey = eventKey,
                title = "Original Event",
                customFieldsJson = "{}"
            )
        )
        assertNotEquals(-1L, originalId)
        eventDao.insertTransaction(
            TransactionEntity(
                eventId = originalId.toInt(),
                personName = "Test Member",
                personPhone = "",
                personEmail = "",
                amount = 125.0,
                type = "Donated"
            )
        )

        val duplicateResult = eventDao.insertEventIfAbsent(
            EventEntity(
                eventKey = eventKey,
                title = "Conflicting Event",
                customFieldsJson = "{}"
            )
        )

        assertEquals(-1L, duplicateResult)
        val preservedEvent = eventDao.getEventByEventKeyOnce(eventKey)
        assertEquals(originalId.toInt(), preservedEvent?.id)
        assertEquals("Original Event", preservedEvent?.title)
        assertEquals(1, eventDao.getTransactionsForEvent(originalId.toInt()).first().size)
    }

    @Test
    fun regularEventInsertCannotReplaceDuplicateKeyLedger() = runBlocking {
        val eventKey = "fedcba9876543210fedcba9876543210"
        val originalId = eventDao.insertEvent(
            EventEntity(
                eventKey = eventKey,
                title = "Preserved Event",
                customFieldsJson = "{}"
            )
        )
        eventDao.insertTransaction(
            TransactionEntity(
                eventId = originalId.toInt(),
                personName = "Preserved Member",
                personPhone = "",
                personEmail = "",
                amount = 225.0,
                type = "Donated"
            )
        )

        var conflictThrown = false
        try {
            eventDao.insertEvent(
                EventEntity(
                    eventKey = eventKey,
                    title = "Replacement Attempt",
                    customFieldsJson = "{}"
                )
            )
        } catch (error: SQLiteConstraintException) {
            conflictThrown = true
        }

        assertTrue(conflictThrown)
        val preservedEvent = eventDao.getEventByEventKeyOnce(eventKey)
        assertEquals(originalId.toInt(), preservedEvent?.id)
        assertEquals("Preserved Event", preservedEvent?.title)
        assertEquals(1, eventDao.getTransactionsForEvent(originalId.toInt()).first().size)
    }
}