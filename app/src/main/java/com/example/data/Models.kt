package com.example.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val duration: String? = null,
    val createdDate: Long = System.currentTimeMillis(),
    val isPrivate: Boolean = false,
    val customFieldsJson: String = "" // Stores optional extra fields as serialized text
)

@Entity(
    tableName = "members",
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = ["id"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["eventId"]), Index(value = ["eventId", "normalizedName"])]
)
data class MemberEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val eventId: Int,
    val name: String,
    val normalizedName: String,
    val phone: String = "",
    val email: String = "",
    val role: String = "Donor",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = ["id"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MemberEntity::class,
            parentColumns = ["id"],
            childColumns = ["memberId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index(value = ["eventId"]), Index(value = ["memberId"])]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val eventId: Int,
    val memberId: Int? = null,
    val personName: String,
    val personPhone: String,
    val personEmail: String,
    val amount: Double,
    val type: String, // "Donated", "Credit" (Debtor/Member owes/contributes), "Debit" (Creditor/Expense), "Expense"
    val date: Long = System.currentTimeMillis(),
    val transactionId: String = "",
    val isVerified: Boolean = true,
    val notes: String? = null,
    val uploaderEmail: String = ""
)
