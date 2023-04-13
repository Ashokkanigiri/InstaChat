package com.example.instachat.services.room_sync.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("interested_users_sync")
data class InterestedUsersModelSync(
    @PrimaryKey
    val id: String,
    val userId: String,
    val isFollowRequested: Boolean,
    val isFollowAccepted: Boolean,
    val isFollowRejected: Boolean,
    val interestedUserId: String,
    val timeStamp: String
)

