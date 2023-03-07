package com.example.instachat.services.models.dummyjson

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("interested-users")
data class InterestedUsersModel(
    @PrimaryKey
    val id: String,
    val userId: String,
    val isFollowRequested: Boolean,
    val isFollowAccepted: Boolean,
    val isFollowRejected: Boolean,
    val interestedUserId: String,
    val timeStamp: String
)

