package com.example.instachat.comment

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.instachat.services.firebase.FirebaseCommentsListener
import com.example.instachat.services.firebase.FirebasePostListener
import com.example.instachat.services.firebase.FirebaseRepository
import com.example.instachat.services.firebase.FirebaseUserListener
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.Comment
import com.example.instachat.services.models.dummyjson.CommentUser
import com.example.instachat.services.models.dummyjson.User
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Random
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CommentViewModel
@Inject constructor(val firebaseRepository: FirebaseRepository) :
    ViewModel(),
    FirebaseCommentsListener,
    FirebasePostListener,
    FirebaseUserListener {

    var currentPost = MutableLiveData<PostModelItem>()
    var postedUser = MutableLiveData<User>()
    var currentUser: User? = null
    var comment = MutableLiveData<String>()

    private val adapter = CommentAdapter()

    init {
        firebaseRepository.getUser(29, this, true)
    }

    fun setAdapter(): CommentAdapter {
        return adapter
    }

    fun loadCommentsForPost(postId: Int) {
        firebaseRepository.getAllCommentsForPost(postId, this)
        loadCurrentPost(postId)
    }

    private fun loadCurrentPost(postId: Int) {
        firebaseRepository.getPost(postId, this)
    }

    private fun loadCurrentPostedUser(userId: Int) {
        firebaseRepository.getUser(userId, this, false)
    }

    /**
     * This trigerrs on click of postComment in xml
     */
    fun postComment() {
        var comment = Comment(
            body = comment.value ?: "",
            id = Random().nextInt(),
            postId = currentPost.value?.id ?: 0,
            user = CommentUser(id = currentUser?.id ?: 0, username = currentUser?.username ?: "")
        )
        firebaseRepository.postComment(comment)
    }

    override fun getAllCommentsFromFirebase(comments: List<Comment>) {
        adapter.submitList(comments)
    }

    override fun getPost(post: PostModelItem) {
        loadCurrentPostedUser(post.userId)
        currentPost.postValue(post)
    }

    override fun getUser(user: User, isLoggedInUser: Boolean) {
        if (!isLoggedInUser) {
            postedUser.postValue(user)
        } else {
            currentUser = user
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

}