package com.example.data

import kotlinx.coroutines.flow.Flow

class EventRepository(private val eventDao: EventDao) {

    val allEvents: Flow<List<EventEntity>> = eventDao.getAllEvents()

    fun getEventById(id: Int): Flow<EventEntity?> = eventDao.getEventById(id)

    suspend fun getEventByIdOnce(id: Int): EventEntity? = eventDao.getEventByIdOnce(id)

    suspend fun insertEvent(event: EventEntity): Long = eventDao.insertEvent(event)

    suspend fun insertEventIfAbsent(event: EventEntity): Long = eventDao.insertEventIfAbsent(event)

    suspend fun deleteEvent(id: Int) = eventDao.deleteEventById(id)

    fun getTransactionsForEvent(eventId: Int): Flow<List<TransactionEntity>> = 
        eventDao.getTransactionsForEvent(eventId)

    suspend fun getUnlinkedTransactionsForEvent(eventId: Int): List<TransactionEntity> =
        eventDao.getUnlinkedTransactionsForEvent(eventId)

    suspend fun updateTransactionMemberId(transactionId: Int, memberId: Int) =
        eventDao.updateTransactionMemberId(transactionId, memberId)

    fun getMembersForEvent(eventId: Int): Flow<List<MemberEntity>> =
        eventDao.getMembersForEvent(eventId)

    suspend fun findMatchingMember(
        eventId: Int,
        normalizedName: String,
        phone: String,
        email: String
    ): MemberEntity? = eventDao.findMatchingMember(eventId, normalizedName, phone, email)

    suspend fun insertMember(member: MemberEntity): Long = eventDao.insertMember(member)

    suspend fun insertTransaction(transaction: TransactionEntity): Long {
        if (!isValidLedgerTransaction(transaction.eventId, transaction.amount, transaction.type)) {
            return -1L
        }
        return eventDao.insertTransaction(transaction)
    }

    suspend fun deleteTransaction(id: Int) = eventDao.deleteTransactionById(id)
}
