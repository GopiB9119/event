package com.communityledger.app.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "shared_event_links",
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = ["id"],
            childColumns = ["localEventId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["remoteEventId"], unique = true)]
)
data class SharedEventLinkEntity(
    @PrimaryKey val localEventId: Int,
    val remoteEventId: String,
    val role: String,
    val remoteRevision: Long,
    val syncState: String,
    val lastServerConfirmedAt: Long?,
    val lastError: String?
)

@Entity(
    tableName = "shared_pending_operations",
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = ["id"],
            childColumns = ["localEventId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["localEventId"]),
        Index(value = ["remoteEventId"]),
        Index(value = ["state"])
    ]
)
data class SharedPendingOperationEntity(
    @PrimaryKey val idempotencyKey: String,
    val localEventId: Int?,
    val remoteEventId: String?,
    val operationType: String,
    val payloadJson: String,
    val state: String,
    val attemptCount: Int,
    val createdAt: Long,
    val updatedAt: Long,
    val lastError: String?
)

object SharedSyncState {
    const val LINKING = "linking"
    const val ACTIVE = "active"
    const val STALE = "stale"
    const val BLOCKED = "blocked"
}

object SharedOperationState {
    const val PENDING = "pending"
    const val RUNNING = "running"
    const val FAILED = "failed"
    const val BLOCKED = "blocked"
}