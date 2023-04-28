package com.example.instachat.feature.chatDetail.repository

import com.example.instachat.services.models.rest.ChatModel
import com.example.instachat.utils.Response
import kotlinx.coroutines.flow.Flow

interface ChatDetailRepository {
    suspend fun getAllChatsForConversation(
        conversationId: String,
        userId: String
    ): Response<List<ChatModel>>

    suspend fun appendNewMessageToConversation(
        conversationId: String,
        chatModel: ChatModel,
        userId: String
    ): Response<Boolean>

    suspend fun listenToConversation(
        conversationId: String, userId: String,
        callback: ((List<ChatModel>) -> Unit)
    )
}