package com.example.instachat.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instachat.services.firebase.FirebaseDataInjector
import com.example.instachat.services.firebase.FirebaseDataListener
import com.example.instachat.services.firebase.FirebaseRepository
import com.example.instachat.services.models.PostModel
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.Comment
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.repository.RestApiRepository
import com.example.instachat.utils.SingleLiveEvent
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val firebaseRepository: FirebaseRepository) : ViewModel(),
    FirebaseDataListener {

    val adapter = HomeDataAdapter(this)
    val usersAdapter = HomeUsersAdapter()
    var currentClickedPostAdapterPosition = 0

    val commentsLayoutClickedEvent = SingleLiveEvent<Int>()

    /**
     * This method injects all the data from API into Firebase
     *
     * Need to be used with care
     */
    fun injectDatabases() {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseRepository.injectDatabasesToFirebase()
        }
    }

    /**
     * Called when swipe to refresh happened,
     *
     * all the home screen data need to be refreshed here
     */
    fun getAllDataFromFirebase() {
        firebaseRepository.getUserPosts(10, this)
//        firebaseRepository.getAllPostsFromFirebase(this)
//        firebaseRepository.getAllCommentsFromFirebase(this)
        firebaseRepository.getAllUsersFromFirebase(this)
    }

    /**
     * This Methos will be trigerred when comments edittext
     * in post gets clicked
     */
    fun onCommentsTextClicked(postId: Int, adapterPosition: Int){
        currentClickedPostAdapterPosition = adapterPosition
        commentsLayoutClickedEvent.postValue(postId)
    }

    fun refreshPost(){
        adapter.notifyItemChanged(currentClickedPostAdapterPosition)
    }

    /**
     * Get All posts in Database
     */
    override fun getAllPosts(posts: List<PostModelItem>) {

    }

    /**
     * Get all comments in Database
     */
    override fun getAllComments(comments: List<Comment>) {
    }

    /**
     * Get List Of All the users in Database
     *
     * TODO: Should be replaced with followed users
     */
    override fun getAllUsers(users: List<User>) {
        usersAdapter.submitList(users)
    }

    /**
     * Posts Displayable in home screen
     */
    override fun getDisplayablePosts(posts: List<PostModelItem>) {
        adapter.submitList(posts)
    }

    /**
     * All the listeners need to be cleared in this method
     *
     */
    override fun onCleared() {
        firebaseRepository.getAllPostsFromFirebase(null)
        firebaseRepository.getAllCommentsFromFirebase(null)
        firebaseRepository.getAllUsersFromFirebase(null)
    }

}