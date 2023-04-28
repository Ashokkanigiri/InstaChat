package com.example.instachat.feature.chatDetail.repository

import android.util.Log
import com.example.instachat.services.models.rest.ChatModel
import com.example.instachat.utils.ConnectivityService
import com.example.instachat.utils.Response
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatDetailRepositoryImpl @Inject constructor(private val connectivityService: ConnectivityService) :
    ChatDetailRepository {

    override suspend fun getAllChatsForConversation(
        conversationId: String,
        userId: String
    ): Response<List<ChatModel>> {
        return try {
            "".toCharArray().sort()
            if (connectivityService.hasActiveNetwork()) {
                val db = Firebase.firestore
                val data =
                    db.collection("conversations").document(conversationId).collection("chat")
                        .orderBy("timeStamp").get().await()
                val location = data.toObjects(ChatModel::class.java)
                Response.Success(location)
            } else {
                Response.HandleNetworkError
            }
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun appendNewMessageToConversation(
        conversationId: String,
        chatModel: ChatModel,
        userId: String
    ): Response<Boolean> {
        return try {
            if (connectivityService.hasActiveNetwork()) {
                val db = Firebase.firestore
                db.collection("conversations").document(conversationId).collection("chat")
                    .document("${chatModel.id}").set(chatModel).await()
                Response.Success(true)
            } else {
                Response.HandleNetworkError
            }
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun listenToConversation(
        conversationId: String,
        userId: String,
        callback: ((List<ChatModel>) -> Unit)
    ) {
        val db = Firebase.firestore
        db.collection("conversations").document(conversationId).collection("chat")
            .addSnapshotListener { value, error ->
                val data = value?.toObjects(ChatModel::class.java)
                data?.let {
                    it.sortBy { it.timeStamp }
                    Log.d("wfmlwf", "DATA sent: ${Gson().toJson(it)}")

                    callback.invoke(it)
                }
            }
    }

}