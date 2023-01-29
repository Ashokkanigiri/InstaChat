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
import com.example.instachat.services.models.dummyjson.User
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val adapter = CommentAdapter()

    fun setAdapter(): CommentAdapter{
        return adapter
    }

    fun loadCommentsForPost(postId: Int) {
        firebaseRepository.getAllCommentsForPost(postId, this)
        loadCurrentPost(postId)
    }

    private fun loadCurrentPost(postId: Int) {
        firebaseRepository.getPost(postId, this)
    }

    private fun loadCurrentPostedUser(userId: Int){
        firebaseRepository.getUser(userId, this)
    }

    override fun getAllCommentsFromFirebase(comments: List<Comment>) {
        adapter.submitList(comments)
    }

    override fun getPost(post: PostModelItem) {
        loadCurrentPostedUser(post.userId)
        currentPost.postValue(post)
    }

    override fun getUser(user: User) {
        postedUser.postValue(user)
    }

    override fun onCleared() {
        super.onCleared()
    }

}