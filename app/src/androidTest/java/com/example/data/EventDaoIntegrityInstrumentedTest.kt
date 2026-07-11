package com.example.data

import android.content.Context
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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
}