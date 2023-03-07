package com.example.instachat.services.models.dummyjson

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("requested-for-interest")
data class RequestedForInterestModel(
    @PrimaryKey
    val id: String,
    val requestedUserId: String,
    val interestId: String
)