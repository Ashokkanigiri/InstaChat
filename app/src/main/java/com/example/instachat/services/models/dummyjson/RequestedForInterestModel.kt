package com.example.instachat.services.models.dummyjson

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("requested_for_interest")
data class RequestedForInterestModel(
    @PrimaryKey
    val id: String,
    val requestedUserId: String,
    val interestId: String,
    val userId: String
)