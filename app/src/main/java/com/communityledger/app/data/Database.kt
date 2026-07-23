package com.communityledger.app.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.flow.Flow

const val APP_DATABASE_SCHEMA_VERSION = 6

@Dao
interface EventDao {
    @Query("SELECT * FROM events ORDER BY createdDate DESC, id DESC")
    fun getAllEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events ORDER BY id ASC")
    suspend fun getAllEventsOnce(): List<EventEntity>

    @Query("""
        SELECT * FROM events
        WHERE id NOT IN (SELECT localEventId FROM shared_event_links)
        ORDER BY id ASC
    """)
    suspend fun getBackupEventsOnce(): List<EventEntity>

    @Query("SELECT COUNT(*) FROM events")
    suspend fun countAllEvents(): Long

    @Query("SELECT COUNT(*) FROM events WHERE id NOT IN (SELECT localEventId FROM shared_event_links)")
    suspend fun countBackupEvents(): Long

    @Query("SELECT * FROM events WHERE id = :id")
    fun getEventById(id: Int): Flow<EventEntity?>

    @Query("SELECT * FROM events WHERE id = :id LIMIT 1")
    suspend fun getEventByIdOnce(id: Int): EventEntity?

    @Query("SELECT * FROM events WHERE eventKey = :eventKey LIMIT 1")
    suspend fun getEventByEventKeyOnce(eventKey: String): EventEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertEvent(event: EventEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEventIfAbsent(event: EventEntity): Long

    @Query("DELETE FROM events WHERE id = :id")
    suspend fun deleteEventById(id: Int)

    @Transaction
    suspend fun deleteEventWithReceiptReferences(id: Int): List<ReceiptEvidenceReference> {
        val references = getTransactionsForEventOnce(id).map { transaction ->
            ReceiptEvidenceReference(transaction.eventId, transaction.notes)
        }
        deleteEventById(id)
        return references
    }

    @Query("SELECT * FROM transactions WHERE eventId = :eventId ORDER BY date DESC, id DESC")
    fun getTransactionsForEvent(eventId: Int): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE eventId = :eventId ORDER BY id ASC")
    suspend fun getTransactionsForEventOnce(eventId: Int): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE id = :id LIMIT 1")
    suspend fun getTransactionByIdOnce(id: Int): TransactionEntity?

    @Query("SELECT * FROM transactions ORDER BY id ASC")
    suspend fun getAllTransactionsOnce(): List<TransactionEntity>

    @Query("""
        SELECT * FROM transactions
        WHERE eventId NOT IN (SELECT localEventId FROM shared_event_links)
        ORDER BY id ASC
    """)
    suspend fun getBackupTransactionsOnce(): List<TransactionEntity>

    @Query("SELECT eventId, notes FROM transactions WHERE notes IS NOT NULL ORDER BY id ASC")
    suspend fun getAllReceiptEvidenceReferencesOnce(): List<ReceiptEvidenceReference>

    @Query("SELECT COUNT(*) FROM transactions")
    suspend fun countAllTransactions(): Long

    @Query("""
        SELECT COUNT(*) FROM transactions
        WHERE eventId NOT IN (SELECT localEventId FROM shared_event_links)
    """)
    suspend fun countBackupTransactions(): Long

    @Query("SELECT * FROM transactions WHERE eventId = :eventId AND memberId IS NULL ORDER BY id ASC")
    suspend fun getUnlinkedTransactionsForEvent(eventId: Int): List<TransactionEntity>

    @Query("UPDATE transactions SET memberId = :memberId WHERE id = :transactionId")
    suspend fun updateTransactionMemberId(transactionId: Int, memberId: Int)

    @Query("SELECT * FROM members WHERE eventId = :eventId ORDER BY name COLLATE NOCASE ASC, id ASC")
    fun getMembersForEvent(eventId: Int): Flow<List<MemberEntity>>

    @Query("SELECT * FROM members ORDER BY id ASC")
    suspend fun getAllMembersOnce(): List<MemberEntity>

    @Query("""
        SELECT * FROM members
        WHERE eventId NOT IN (SELECT localEventId FROM shared_event_links)
        ORDER BY id ASC
    """)
    suspend fun getBackupMembersOnce(): List<MemberEntity>

    @Query("SELECT COUNT(*) FROM members")
    suspend fun countAllMembers(): Long

    @Query("""
        SELECT COUNT(*) FROM members
        WHERE eventId NOT IN (SELECT localEventId FROM shared_event_links)
    """)
    suspend fun countBackupMembers(): Long

    @Query("""
        SELECT * FROM members
        WHERE eventId = :eventId AND (
            (:phone != '' AND phone = :phone) OR
            (:email != '' AND email = :email) OR
            (:normalizedName != '' AND normalizedName = :normalizedName)
        )
        ORDER BY id ASC
        LIMIT 1
    """)
    suspend fun findMatchingMember(
        eventId: Int,
        normalizedName: String,
        phone: String,
        email: String
    ): MemberEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: MemberEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactionUnchecked(transaction: TransactionEntity): Long

    @Query("""
        SELECT
            COALESCE(SUM(CASE WHEN type IN ('Donated', 'Credit') THEN amount ELSE 0.0 END), 0.0) AS totalCollected,
            COALESCE(SUM(CASE WHEN type IN ('Debit', 'Expense') THEN amount ELSE 0.0 END), 0.0) AS totalSpent,
            COALESCE(SUM(CASE WHEN amount <= 0.0 OR type NOT IN ('Donated', 'Credit', 'Debit', 'Expense') THEN 1 ELSE 0 END), 0) AS invalidRowCount
        FROM transactions
        WHERE eventId = :eventId AND (:excludedTransactionId <= 0 OR id != :excludedTransactionId)
    """)
    suspend fun getLedgerAggregateProjection(
        eventId: Int,
        excludedTransactionId: Int
    ): LedgerAggregateProjection

    @Transaction
    suspend fun insertTransaction(transaction: TransactionEntity): Long {
        if (
            transaction.id > 0 &&
            getTransactionByIdOnce(transaction.id)?.eventId?.let { it != transaction.eventId } == true
        ) {
            return -1L
        }
        val projection = getLedgerAggregateProjection(transaction.eventId, transaction.id)
        if (!wouldProjectedLedgerTotalsRemainFinite(projection, transaction)) return -1L
        return insertTransactionUnchecked(transaction)
    }

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransactionById(id: Int)

    @Transaction
    suspend fun deleteTransactionWithReceiptReference(id: Int): ReceiptEvidenceReference? {
        val transaction = getTransactionByIdOnce(id)
        deleteTransactionById(id)
        return transaction?.let { ReceiptEvidenceReference(it.eventId, it.notes) }
    }

    @Transaction
    suspend fun captureLedgerSourceSnapshot(
        maxEvents: Int = MAX_LEDGER_SOURCE_EVENTS,
        maxMembers: Int = MAX_LEDGER_SOURCE_MEMBERS,
        maxTransactions: Int = MAX_LEDGER_SOURCE_TRANSACTIONS
    ): LedgerSourceSnapshot {
        if (countBackupEvents() > maxEvents) {
            throw LedgerSourceSnapshotLimitException("Ledger contains too many events for one backup snapshot.")
        }
        if (countBackupMembers() > maxMembers) {
            throw LedgerSourceSnapshotLimitException("Ledger contains too many members for one backup snapshot.")
        }
        if (countBackupTransactions() > maxTransactions) {
            throw LedgerSourceSnapshotLimitException("Ledger contains too many transactions for one backup snapshot.")
        }
        return LedgerSourceSnapshot(
            events = getBackupEventsOnce(),
            members = getBackupMembersOnce(),
            transactions = getBackupTransactionsOnce()
        )
    }

    @Transaction
    suspend fun captureReceiptEvidenceReferencesForReconciliation(
        maxTransactions: Int = MAX_LEDGER_SOURCE_TRANSACTIONS
    ): List<ReceiptEvidenceReference>? {
        if (countAllTransactions() > maxTransactions) return null
        return getAllReceiptEvidenceReferencesOnce()
    }
}

@Dao
interface SharedSyncDao {
    @Query("SELECT * FROM shared_event_links WHERE localEventId = :localEventId LIMIT 1")
    fun observeEventLink(localEventId: Int): Flow<SharedEventLinkEntity?>

    @Query("SELECT * FROM shared_event_links WHERE localEventId = :localEventId LIMIT 1")
    suspend fun getEventLinkByLocalIdOnce(localEventId: Int): SharedEventLinkEntity?

    @Query("SELECT * FROM shared_event_links WHERE remoteEventId = :remoteEventId LIMIT 1")
    suspend fun getEventLinkByRemoteIdOnce(remoteEventId: String): SharedEventLinkEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertEventLink(link: SharedEventLinkEntity)

    @Query("""
        UPDATE shared_event_links
        SET role = :role,
            remoteRevision = :remoteRevision,
            syncState = :syncState,
            lastServerConfirmedAt = :lastServerConfirmedAt,
            lastError = :lastError
        WHERE localEventId = :localEventId
    """)
    suspend fun updateEventLinkState(
        localEventId: Int,
        role: String,
        remoteRevision: Long,
        syncState: String,
        lastServerConfirmedAt: Long?,
        lastError: String?
    ): Int

    @Transaction
    suspend fun linkEvent(link: SharedEventLinkEntity): Boolean {
        val localLink = getEventLinkByLocalIdOnce(link.localEventId)
        if (localLink != null) return localLink.remoteEventId == link.remoteEventId
        if (getEventLinkByRemoteIdOnce(link.remoteEventId) != null) return false
        insertEventLink(link)
        return true
    }

    @Query("""
        SELECT * FROM shared_pending_operations
        WHERE localEventId = :localEventId
        ORDER BY createdAt ASC, idempotencyKey ASC
    """)
    fun observePendingOperations(localEventId: Int): Flow<List<SharedPendingOperationEntity>>

    @Query("""
        SELECT * FROM shared_pending_operations
        WHERE state IN ('pending', 'running', 'failed')
        ORDER BY createdAt ASC, idempotencyKey ASC
        LIMIT :limit
    """)
    suspend fun getRetryableOperations(limit: Int): List<SharedPendingOperationEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPendingOperation(operation: SharedPendingOperationEntity)

    @Query("""
        UPDATE shared_pending_operations
        SET state = :state,
            attemptCount = :attemptCount,
            updatedAt = :updatedAt,
            lastError = :lastError
        WHERE idempotencyKey = :idempotencyKey
    """)
    suspend fun updatePendingOperationState(
        idempotencyKey: String,
        state: String,
        attemptCount: Int,
        updatedAt: Long,
        lastError: String?
    ): Int

    @Query("DELETE FROM shared_pending_operations WHERE idempotencyKey = :idempotencyKey")
    suspend fun deletePendingOperation(idempotencyKey: String): Int
}

@Database(
    entities = [
        EventEntity::class,
        MemberEntity::class,
        TransactionEntity::class,
        SharedEventLinkEntity::class,
        SharedPendingOperationEntity::class
    ],
    version = APP_DATABASE_SCHEMA_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun sharedSyncDao(): SharedSyncDao

    companion object {
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `transactions_new` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `eventId` INTEGER NOT NULL,
                        `personName` TEXT NOT NULL,
                        `personPhone` TEXT NOT NULL,
                        `personEmail` TEXT NOT NULL,
                        `amount` REAL NOT NULL,
                        `type` TEXT NOT NULL,
                        `date` INTEGER NOT NULL,
                        `transactionId` TEXT NOT NULL,
                        `isVerified` INTEGER NOT NULL,
                        `notes` TEXT,
                        `uploaderEmail` TEXT NOT NULL,
                        FOREIGN KEY(`eventId`) REFERENCES `events`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                    )
                """.trimIndent())
                db.execSQL("""
                    INSERT INTO `transactions_new` (
                        `id`, `eventId`, `personName`, `personPhone`, `personEmail`, `amount`,
                        `type`, `date`, `transactionId`, `isVerified`, `notes`, `uploaderEmail`
                    )
                    SELECT
                        `id`, `eventId`, `personName`, `personPhone`, `personEmail`, `amount`,
                        `type`, `date`, `transactionId`, `isVerified`, `notes`, `uploaderEmail`
                    FROM `transactions`
                    WHERE `eventId` IN (SELECT `id` FROM `events`)
                """.trimIndent())
                db.execSQL("DROP TABLE `transactions`")
                db.execSQL("ALTER TABLE `transactions_new` RENAME TO `transactions`")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_eventId` ON `transactions` (`eventId`)")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `members` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `eventId` INTEGER NOT NULL,
                        `name` TEXT NOT NULL,
                        `normalizedName` TEXT NOT NULL,
                        `phone` TEXT NOT NULL,
                        `email` TEXT NOT NULL,
                        `role` TEXT NOT NULL,
                        `createdAt` INTEGER NOT NULL,
                        FOREIGN KEY(`eventId`) REFERENCES `events`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                    )
                """.trimIndent())
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_members_eventId` ON `members` (`eventId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_members_eventId_normalizedName` ON `members` (`eventId`, `normalizedName`)")

                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `transactions_new` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `eventId` INTEGER NOT NULL,
                        `memberId` INTEGER,
                        `personName` TEXT NOT NULL,
                        `personPhone` TEXT NOT NULL,
                        `personEmail` TEXT NOT NULL,
                        `amount` REAL NOT NULL,
                        `type` TEXT NOT NULL,
                        `date` INTEGER NOT NULL,
                        `transactionId` TEXT NOT NULL,
                        `isVerified` INTEGER NOT NULL,
                        `notes` TEXT,
                        `uploaderEmail` TEXT NOT NULL,
                        FOREIGN KEY(`eventId`) REFERENCES `events`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE,
                        FOREIGN KEY(`memberId`) REFERENCES `members`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL
                    )
                """.trimIndent())
                db.execSQL("""
                    INSERT INTO `transactions_new` (
                        `id`, `eventId`, `memberId`, `personName`, `personPhone`, `personEmail`, `amount`,
                        `type`, `date`, `transactionId`, `isVerified`, `notes`, `uploaderEmail`
                    )
                    SELECT
                        `id`, `eventId`, NULL, `personName`, `personPhone`, `personEmail`, `amount`,
                        `type`, `date`, `transactionId`, `isVerified`, `notes`, `uploaderEmail`
                    FROM `transactions`
                    WHERE `eventId` IN (SELECT `id` FROM `events`)
                """.trimIndent())
                db.execSQL("DROP TABLE `transactions`")
                db.execSQL("ALTER TABLE `transactions_new` RENAME TO `transactions`")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_eventId` ON `transactions` (`eventId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_memberId` ON `transactions` (`memberId`)")
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE `events` ADD COLUMN `eventKey` TEXT")
                db.execSQL("UPDATE `events` SET `eventKey` = lower(hex(randomblob(16))) WHERE `eventKey` IS NULL")
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_events_eventKey` ON `events` (`eventKey`)")
            }
        }

        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `shared_event_links` (
                        `localEventId` INTEGER NOT NULL,
                        `remoteEventId` TEXT NOT NULL,
                        `role` TEXT NOT NULL,
                        `remoteRevision` INTEGER NOT NULL,
                        `syncState` TEXT NOT NULL,
                        `lastServerConfirmedAt` INTEGER,
                        `lastError` TEXT,
                        PRIMARY KEY(`localEventId`),
                        FOREIGN KEY(`localEventId`) REFERENCES `events`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                    )
                """.trimIndent())
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_shared_event_links_remoteEventId` ON `shared_event_links` (`remoteEventId`)")
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `shared_pending_operations` (
                        `idempotencyKey` TEXT NOT NULL,
                        `localEventId` INTEGER,
                        `remoteEventId` TEXT,
                        `operationType` TEXT NOT NULL,
                        `payloadJson` TEXT NOT NULL,
                        `state` TEXT NOT NULL,
                        `attemptCount` INTEGER NOT NULL,
                        `createdAt` INTEGER NOT NULL,
                        `updatedAt` INTEGER NOT NULL,
                        `lastError` TEXT,
                        PRIMARY KEY(`idempotencyKey`),
                        FOREIGN KEY(`localEventId`) REFERENCES `events`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                    )
                """.trimIndent())
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_shared_pending_operations_localEventId` ON `shared_pending_operations` (`localEventId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_shared_pending_operations_remoteEventId` ON `shared_pending_operations` (`remoteEventId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_shared_pending_operations_state` ON `shared_pending_operations` (`state`)")
            }
        }
    }
}
