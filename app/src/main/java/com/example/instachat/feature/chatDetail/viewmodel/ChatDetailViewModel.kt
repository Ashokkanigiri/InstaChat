package com.example.instachat.feature.chatDetail.viewmodel

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instachat.feature.chatDetail.repository.ChatDetailRepository
import com.example.instachat.feature.chatDetail.view.ChatDetailAdapter
import com.example.instachat.services.models.rest.ChatModel
import com.example.instachat.utils.DateUtils
import com.example.instachat.utils.Response
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChatDetailViewModel @Inject constructor(private val chatDetailRepository: ChatDetailRepository) :
    ViewModel() {

    private val loggedUserId = Firebase.auth.currentUser?.uid ?: ""

    val chatAdapter = ChatDetailAdapter(this, loggedUserId)

    var userId: String? = null

    val messageTextListener = ObservableField<String>()

    fun getData() {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            chatDetailRepository.listenToConversation(getConversationId(), loggedUserId ?: "") {
                chatAdapter.submitList(it)
            }
        }
    }

    fun sendMessageToConversation() {

        val chatModel = ChatModel(
            id = Random().nextLong(),
            conversationId = getConversationId(),
            sentUserId = loggedUserId,
            receivedUserId = userId ?: "",
            body = messageTextListener.get()?.trim() ?: "",
            timeStamp = DateUtils.getCurrentTimeInMillis()?:""
        )

        viewModelScope.launch(Dispatchers.IO) {
            when (val response = chatDetailRepository.appendNewMessageToConversation(
                conversationId = getConversationId(),
                chatModel = chatModel,
                userId = loggedUserId ?: ""
            )) {
                is Response.Failure -> {

                }
                is Response.HandleNetworkError -> {

                }
                is Response.Loading -> {

                }
                is Response.Success -> {
                    messageTextListener.set("")
                }
            }
        }
    }

    private fun getConversationId() : String{
        val skeletonId = "$loggedUserId|$userId"
        return skeletonId.toCharArray().sortedArray().concatToString()
    }

}