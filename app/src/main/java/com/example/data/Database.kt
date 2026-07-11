package com.example.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("SELECT * FROM events ORDER BY createdDate DESC")
    fun getAllEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE id = :id")
    fun getEventById(id: Int): Flow<EventEntity?>

    @Query("SELECT * FROM events WHERE id = :id LIMIT 1")
    suspend fun getEventByIdOnce(id: Int): EventEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEventIfAbsent(event: EventEntity): Long

    @Query("DELETE FROM events WHERE id = :id")
    suspend fun deleteEventById(id: Int)

    @Query("SELECT * FROM transactions WHERE eventId = :eventId ORDER BY date DESC")
    fun getTransactionsForEvent(eventId: Int): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE eventId = :eventId AND memberId IS NULL")
    suspend fun getUnlinkedTransactionsForEvent(eventId: Int): List<TransactionEntity>

    @Query("UPDATE transactions SET memberId = :memberId WHERE id = :transactionId")
    suspend fun updateTransactionMemberId(transactionId: Int, memberId: Int)

    @Query("SELECT * FROM members WHERE eventId = :eventId ORDER BY name COLLATE NOCASE ASC")
    fun getMembersForEvent(eventId: Int): Flow<List<MemberEntity>>

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
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransactionById(id: Int)
}

@Database(entities = [EventEntity::class, MemberEntity::class, TransactionEntity::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao

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
    }
}
