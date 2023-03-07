package com.example.instachat.services.room_sync.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("requested_for_interest_sync")
data class RequestedForInterestModelSync(
    @PrimaryKey
    val id: String,
    val requestedUserId: String,
    val interestId: String
)