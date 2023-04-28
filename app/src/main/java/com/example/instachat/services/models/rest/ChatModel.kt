package com.example.instachat.services.models.rest

data class ChatModel(
    val id: Long = 0,
    val conversationId: String = "",
    val sentUserId: String = "",
    val receivedUserId: String = "",
    val body: String = "",
    val timeStamp: String = "",
    val sentUserImageUrl: String = "",
    val receivedUserImageUrl: String = ""
)