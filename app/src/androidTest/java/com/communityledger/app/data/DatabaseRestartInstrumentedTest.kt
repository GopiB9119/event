package com.communityledger.app.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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

    @Test
    fun version4LedgerMigratesWithOpaqueEventKeyAndTransaction() = runBlocking {
        createVersion4Database()

        val migratedDatabase = openDatabase()
        try {
            val eventDao = migratedDatabase.eventDao()
            val event = eventDao.getEventByIdOnce(21)
            assertNotNull(event)
            assertEquals("Migration Test Event", event?.title)
            assertFalse(event?.eventKey.isNullOrBlank())
            assertEquals(32, event?.eventKey?.length)
            assertEquals(event, eventDao.getEventByEventKeyOnce(event?.eventKey.orEmpty()))

            val transactions = eventDao.getTransactionsForEvent(21).first()
            assertEquals(1, transactions.size)
            assertEquals(375.0, transactions.single().amount, 0.0)
        } finally {
            migratedDatabase.close()
        }
    }

    @Test
    fun version5LedgerMigratesWithEmptySharedStateAndCanPersistOneMapping() = runBlocking {
        createVersion5Database()

        val migratedDatabase = openDatabase()
        try {
            val sharedSyncDao = migratedDatabase.sharedSyncDao()
            assertEquals(null, sharedSyncDao.getEventLinkByLocalIdOnce(51))
            assertEquals(
                true,
                sharedSyncDao.linkEvent(
                    SharedEventLinkEntity(
                        localEventId = 51,
                        remoteEventId = "AbCdEfGhIjKlMnOpQrSt",
                        role = "organizer",
                        remoteRevision = 1,
                        syncState = SharedSyncState.ACTIVE,
                        lastServerConfirmedAt = 1_720_000_000_100L,
                        lastError = null
                    )
                )
            )
            assertEquals("AbCdEfGhIjKlMnOpQrSt", sharedSyncDao.getEventLinkByLocalIdOnce(51)?.remoteEventId)
        } finally {
            migratedDatabase.close()
        }
    }

    private fun createVersion4Database() {
        val databaseFile = context.getDatabasePath(TEST_DATABASE_NAME)
        databaseFile.parentFile?.mkdirs()
        val database = SQLiteDatabase.openOrCreateDatabase(databaseFile, null)
        try {
            database.execSQL("PRAGMA foreign_keys = ON")
            database.execSQL("CREATE TABLE IF NOT EXISTS `events` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `duration` TEXT, `createdDate` INTEGER NOT NULL, `isPrivate` INTEGER NOT NULL, `customFieldsJson` TEXT NOT NULL)")
            database.execSQL("CREATE TABLE IF NOT EXISTS `members` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `eventId` INTEGER NOT NULL, `name` TEXT NOT NULL, `normalizedName` TEXT NOT NULL, `phone` TEXT NOT NULL, `email` TEXT NOT NULL, `role` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, FOREIGN KEY(`eventId`) REFERENCES `events`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_members_eventId` ON `members` (`eventId`)")
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_members_eventId_normalizedName` ON `members` (`eventId`, `normalizedName`)")
            database.execSQL("CREATE TABLE IF NOT EXISTS `transactions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `eventId` INTEGER NOT NULL, `memberId` INTEGER, `personName` TEXT NOT NULL, `personPhone` TEXT NOT NULL, `personEmail` TEXT NOT NULL, `amount` REAL NOT NULL, `type` TEXT NOT NULL, `date` INTEGER NOT NULL, `transactionId` TEXT NOT NULL, `isVerified` INTEGER NOT NULL, `notes` TEXT, `uploaderEmail` TEXT NOT NULL, FOREIGN KEY(`eventId`) REFERENCES `events`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`memberId`) REFERENCES `members`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL )")
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_eventId` ON `transactions` (`eventId`)")
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_memberId` ON `transactions` (`memberId`)")
            database.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY, identity_hash TEXT)")
            database.execSQL("INSERT OR REPLACE INTO room_master_table (id, identity_hash) VALUES(42, 'f2c89377c5fa2765c9491cb132107abe')")
            database.execSQL(
                "INSERT INTO events (id, title, duration, createdDate, isPrivate, customFieldsJson) VALUES (?, ?, ?, ?, ?, ?)",
                arrayOf<Any?>(21, "Migration Test Event", null, 1_720_000_000_000L, 1, "{}")
            )
            database.execSQL(
                "INSERT INTO transactions (id, eventId, memberId, personName, personPhone, personEmail, amount, type, date, transactionId, isVerified, notes, uploaderEmail) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                arrayOf<Any?>(31, 21, null, "Migration Member", "", "", 375.0, "Donated", 1_720_000_000_100L, "migration-tx", 1, null, "organizer@example.com")
            )
            database.version = 4
        } finally {
            database.close()
        }
    }

    private fun createVersion5Database() {
        val databaseFile = context.getDatabasePath(TEST_DATABASE_NAME)
        databaseFile.parentFile?.mkdirs()
        val database = SQLiteDatabase.openOrCreateDatabase(databaseFile, null)
        try {
            database.execSQL("PRAGMA foreign_keys = ON")
            database.execSQL("CREATE TABLE IF NOT EXISTS `events` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `eventKey` TEXT, `title` TEXT NOT NULL, `duration` TEXT, `createdDate` INTEGER NOT NULL, `isPrivate` INTEGER NOT NULL, `customFieldsJson` TEXT NOT NULL)")
            database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_events_eventKey` ON `events` (`eventKey`)")
            database.execSQL("CREATE TABLE IF NOT EXISTS `members` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `eventId` INTEGER NOT NULL, `name` TEXT NOT NULL, `normalizedName` TEXT NOT NULL, `phone` TEXT NOT NULL, `email` TEXT NOT NULL, `role` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, FOREIGN KEY(`eventId`) REFERENCES `events`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_members_eventId` ON `members` (`eventId`)")
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_members_eventId_normalizedName` ON `members` (`eventId`, `normalizedName`)")
            database.execSQL("CREATE TABLE IF NOT EXISTS `transactions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `eventId` INTEGER NOT NULL, `memberId` INTEGER, `personName` TEXT NOT NULL, `personPhone` TEXT NOT NULL, `personEmail` TEXT NOT NULL, `amount` REAL NOT NULL, `type` TEXT NOT NULL, `date` INTEGER NOT NULL, `transactionId` TEXT NOT NULL, `isVerified` INTEGER NOT NULL, `notes` TEXT, `uploaderEmail` TEXT NOT NULL, FOREIGN KEY(`eventId`) REFERENCES `events`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`memberId`) REFERENCES `members`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL )")
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_eventId` ON `transactions` (`eventId`)")
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_memberId` ON `transactions` (`memberId`)")
            database.execSQL(
                "INSERT INTO events (id, eventKey, title, duration, createdDate, isPrivate, customFieldsJson) VALUES (?, ?, ?, ?, ?, ?, ?)",
                arrayOf<Any?>(51, "55555555555555555555555555555555", "Shared Migration Event", null, 1_720_000_000_000L, 1, "{}")
            )
            database.version = 5
        } finally {
            database.close()
        }
    }

    private fun openDatabase(): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, TEST_DATABASE_NAME)
            .addMigrations(
                AppDatabase.MIGRATION_2_3,
                AppDatabase.MIGRATION_3_4,
                AppDatabase.MIGRATION_4_5,
                AppDatabase.MIGRATION_5_6
            )
            .allowMainThreadQueries()
            .build()
    }

    private companion object {
        const val TEST_DATABASE_NAME = "community_ledger_restart_test.db"
    }
}