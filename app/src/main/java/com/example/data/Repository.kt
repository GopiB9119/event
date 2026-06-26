package com.example.data

import kotlinx.coroutines.flow.Flow

class EventRepository(private val eventDao: EventDao) {

    val allEvents: Flow<List<EventEntity>> = eventDao.getAllEvents()

    fun getEventById(id: Int): Flow<EventEntity?> = eventDao.getEventById(id)

    suspend fun insertEvent(event: EventEntity): Long = eventDao.insertEvent(event)

    suspend fun deleteEvent(id: Int) = eventDao.deleteEventById(id)

    fun getTransactionsForEvent(eventId: Int): Flow<List<TransactionEntity>> = 
        eventDao.getTransactionsForEvent(eventId)

    suspend fun insertTransaction(transaction: TransactionEntity): Long = 
        eventDao.insertTransaction(transaction)

    suspend fun deleteTransaction(id: Int) = eventDao.deleteTransactionById(id)
}
