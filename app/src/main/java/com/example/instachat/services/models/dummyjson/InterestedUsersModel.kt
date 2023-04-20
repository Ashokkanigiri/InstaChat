package com.example.instachat.services.models.dummyjson

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("interested_users")
data class InterestedUsersModel(
    @PrimaryKey
    @ColumnInfo("id")
    val id: String,
    val userId: String,
    val isFollowRequested: Boolean,
    val isFollowAccepted: Boolean,
    val isFollowRejected: Boolean,
    val interestedUserId: String,
    val timeStamp: String
)

