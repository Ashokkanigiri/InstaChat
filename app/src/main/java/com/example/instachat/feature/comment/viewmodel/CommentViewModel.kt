package com.example.instachat.feature.comment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instachat.feature.comment.CommentAdapter
import com.example.instachat.feature.comment.repository.CommentsRepository
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.Comment
import com.example.instachat.services.models.dummyjson.CommentUser
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.repository.RoomDataSource
import com.example.instachat.services.repository.SyncRepository
import com.example.instachat.utils.DateUtils
import com.example.instachat.utils.Response
import com.example.instachat.utils.SingleLiveEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CommentViewModel
@Inject constructor(
    private val roomDataSource: RoomDataSource,
    private val syncRepository: SyncRepository,
    private val commentsRepository: CommentsRepository
) :
    ViewModel() {

    private val loggedUserId = Firebase.auth.currentUser?.uid ?: ""
    private val _loggedUserEvent = SingleLiveEvent<User?>()
    private val _getCurrentPostEvent = SingleLiveEvent<PostModelItem?>()
    private val _getPostedUserEvent = SingleLiveEvent<User?>()
    private val _onPostedCommentEvent = SingleLiveEvent<Boolean>()

    val loggedUserEvent get() = _loggedUserEvent
    val getCurrentPostEvent get() = _getCurrentPostEvent
    val getPostedUserEvent get() = _getPostedUserEvent
    val onPostedCommentEvent get() = _onPostedCommentEvent
    var comment = MutableLiveData<String>()
    val adapter = CommentAdapter()


    /**
     * Loads the data required for comments fragment to display
     *
     * This will get triggered when user clicks on any post
     */
    fun loadData(postId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            loadLoggedUser()
            loadAllCommentsForPost(postId)
            loadCurrentPost(postId)
        }
    }

    /**
     * Loads logged user details and emits user object
     */
    private fun loadLoggedUser() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val user = commentsRepository.loadCurrentLoggedUser(loggedUserId)) {
                is Response.Failure -> {

                }
                is Response.Loading -> {

                }
                is Response.Success -> {
                    _loggedUserEvent.postValue(user.data)
                }
            }
        }
    }

    /**
     * This method takes postId and loads adapter with list of comments
     */
    private fun loadAllCommentsForPost(postId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            commentsRepository.loadAllCommentsForPost(postId).collect {
                withContext(Dispatchers.Main) {
                    adapter.submitList(it)
                }
            }
        }
    }

    /**
     * This method takes postId and emits post object from room DB
     */
    private fun loadCurrentPost(postId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val post = commentsRepository.loadCurrentPost(postId)) {
                is Response.Failure -> {

                }
                is Response.Loading -> {

                }
                is Response.Success -> {
                    post.data?.let {
                        _getCurrentPostEvent.postValue(it)
                    }
                }
            }
        }
    }

    /**
     * This method takes userId and emits user object from room DB
     */
    fun loadCurrentPostedUser(userId: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val user = commentsRepository.loadPostedUser(userId ?: "")) {
                is Response.Failure -> {

                }
                is Response.Loading -> {

                }
                is Response.Success -> {
                    user.data?.let {
                        _getPostedUserEvent.postValue(it)
                    }
                }
            }
        }
    }

    /**
     * This method triggers when user clicks on post button
     *
     * This method is binds to XML & arguments will be passed from XML
     *
     * This method sync newly created comments with firebase and room DB
     */
    fun postComment(postId: Int?, currentUser: User?) {
        val newComment = Comment(
            body = comment.value ?: "",
            id = Random().nextInt(),
            postId = postId ?: 0,
            user = CommentUser(
                id = currentUser?.id ?: "",
                username = currentUser?.username ?: "",
                userImageUrl = currentUser?.image ?: ""
            ),
            commentCreatedDate = DateUtils.getCurrentTime()
        )
        viewModelScope.launch(Dispatchers.IO) {
            syncRepository.addNewComment(newComment)
        }
        _onPostedCommentEvent.postValue(true)
        comment.value = ""
    }
}