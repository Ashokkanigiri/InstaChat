package com.example.instachat.services.models.rest

data class ConversationModel(
    val id: String,
    val fromUser: String,
    val toUser: String,
    val timestamp: String
)