package com.example.data

import androidx.room.Entity
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

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val eventId: Int,
    val personName: String,
    val personPhone: String,
    val personEmail: String,
    val amount: Double,
    val type: String, // "Donated", "Credit" (Debtor/Member owes/contributes), "Debit" (Creditor/Expense), "Expense"
    val date: Long = System.currentTimeMillis(),
    val transactionId: String = "",
    val isVerified: Boolean = true,
    val notes: String? = null
)
