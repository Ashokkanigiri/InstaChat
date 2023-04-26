package com.example.instachat.services.room_sync.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("interested_users_sync")
data class InterestedUsersModelSync(
    @PrimaryKey
    val id: String = "",
    val userId: String = "",
    var isFollowRequested: Boolean = false,
    var isFollowAccepted: Boolean = false,
    var isFollowRejected: Boolean = false,
    val interestedUserId: String = "",
    val timeStamp: String = ""
)

