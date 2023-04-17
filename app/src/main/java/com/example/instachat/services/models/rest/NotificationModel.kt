package com.example.instachat.services.models.rest

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationModel(
    @PrimaryKey
    val id: String,
    val targetUserId: String,
    val triggeredUserId: String,
    val title: String,
    val body: String,
    val timeStamp: String
)
