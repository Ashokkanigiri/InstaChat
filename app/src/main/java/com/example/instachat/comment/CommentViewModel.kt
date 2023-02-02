package com.example.instachat.comment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instachat.services.firebase.FirebaseRepository
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.Comment
import com.example.instachat.services.models.dummyjson.CommentUser
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Random
import javax.inject.Inject

@HiltViewModel
class CommentViewModel
@Inject constructor(val roomRepository: RoomRepository) :
    ViewModel() {

    var currentPost = MutableLiveData<PostModelItem>()
    var postedUser = MutableLiveData<User>()
    var currentUser: User? = null
    var comment = MutableLiveData<String>()
    var isCommentUpdated = false

    private val adapter = CommentAdapter()

    init {
        viewModelScope.launch {
            roomRepository.usersDao.getUser("29").apply {
                currentUser = this
            }
        }
    }

    fun setAdapter(): CommentAdapter {
        return adapter
    }

    fun loadCommentsForPost(postId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            roomRepository.commentsDao.getAllCommentsForPost(postId).apply {
                withContext(Dispatchers.Main){
                    adapter.submitList(this@apply)
                }
            }
        }
        loadCurrentPost(postId)
    }

    private fun loadCurrentPost(postId: Int) {
        viewModelScope.launch {
            roomRepository.postsDao.getPost(postId).apply {
                loadCurrentPostedUser(this.userId)
                currentPost.postValue(this)
            }
        }
    }

    private fun loadCurrentPostedUser(userId: String) {
        viewModelScope.launch {
            roomRepository.usersDao.getUser(userId).apply {
                postedUser.postValue(this)
            }
        }
    }

    /**
     * This trigerrs on click of postComment in xml
     */
    fun postComment() {
        var newComment = Comment(
            body = comment.value ?: "",
            id = Random().nextInt(),
            postId = currentPost.value?.id ?: 0,
            user = CommentUser(id = currentUser?.id ?: "", username = currentUser?.username ?: "")
        )
        viewModelScope.launch {
            roomRepository.commentsDao.insert(newComment)
        }
        comment.value = ""
        isCommentUpdated = true
    }


    override fun onCleared() {
        super.onCleared()
    }

}