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
class DatabaseRestartInstrumentedTest {
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        context.deleteDatabase(TEST_DATABASE_NAME)
    }

    @After
    fun tearDown() {
        context.deleteDatabase(TEST_DATABASE_NAME)
    }

    @Test
    fun eventAndTransactionSurviveDatabaseReopen() = runBlocking {
        val database = openDatabase()
        try {
            val eventDao = database.eventDao()
            eventDao.insertEvent(
                EventEntity(
                    id = 11,
                    title = "Restart Test Event",
                    isPrivate = true,
                    customFieldsJson = "{}"
                )
            )
            eventDao.insertTransaction(
                TransactionEntity(
                    eventId = 11,
                    personName = "Restart Test Member",
                    personPhone = "",
                    personEmail = "",
                    amount = 250.0,
                    type = "Donated"
                )
            )
        } finally {
            database.close()
        }

        val reopenedDatabase = openDatabase()
        try {
            val eventDao = reopenedDatabase.eventDao()
            val event = eventDao.getEventByIdOnce(11)
            assertNotNull(event)
            assertEquals("Restart Test Event", event?.title)
            assertEquals(true, event?.isPrivate)

            val transactions = eventDao.getTransactionsForEvent(11).first()
            assertEquals(1, transactions.size)
            assertEquals(250.0, transactions.single().amount, 0.0)
        } finally {
            reopenedDatabase.close()
        }
    }

    private fun openDatabase(): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, TEST_DATABASE_NAME)
            .addMigrations(AppDatabase.MIGRATION_2_3, AppDatabase.MIGRATION_3_4)
            .allowMainThreadQueries()
            .build()
    }

    private companion object {
        const val TEST_DATABASE_NAME = "community_ledger_restart_test.db"
    }
}