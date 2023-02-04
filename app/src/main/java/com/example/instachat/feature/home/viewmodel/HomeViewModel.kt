package com.example.instachat.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instachat.feature.home.HomeDataAdapter
import com.example.instachat.feature.home.models.HomeDataCommentsModel
import com.example.instachat.feature.home.models.HomeDataModel
import com.example.instachat.feature.home.HomeUsersAdapter
import com.example.instachat.services.repository.FirebaseRepository
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.LikedPosts
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.repository.RoomRepository
import com.example.instachat.services.repository.RoomSyncRepository
import com.example.instachat.services.repository.SyncRepository
import com.example.instachat.utils.SingleLiveEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val firebaseRepository: FirebaseRepository,
    val roomRepository: RoomRepository,
    val roomSyncRepository: RoomSyncRepository,
    val syncRepository: SyncRepository
) : ViewModel() {

    val adapter = HomeDataAdapter(this)
    val usersAdapter = HomeUsersAdapter()
    val commentsLayoutClickedEvent = SingleLiveEvent<Int>()
    val auth = Firebase.auth

    /**
     * This method injects all the data from API into Firebase
     *
     * Need to be used with care
     */
    fun injectDataFromFirebase() {

        viewModelScope.launch(Dispatchers.IO) {
            firebaseRepository.getAllPostsFromFirebase()
            firebaseRepository.getAllUsersFromFirebase()
            firebaseRepository.getAllCommentsFromFirebase()
        }
    }

    /**
     * This Method will be trigerred when comments edittext
     * in post gets clicked
     */
    fun onCommentsTextClicked(postId: Int) {
        commentsLayoutClickedEvent.postValue(postId)
    }

    fun onLikeButtonClicked(homeDataModel: HomeDataModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentUser = roomRepository.usersDao.getUser(auth.currentUser?.uid ?: "0")
            val postModelItem = roomRepository.postsDao.getPost(homeDataModel.postId)

            val isUserAlreadyLikedPost =
                currentUser.likedPosts?.map { it.postId }?.contains(postModelItem.id)

            val incrementLikeCondition =
                currentUser.likedPosts.isNullOrEmpty()

            when {
                (incrementLikeCondition) -> {
                    incrementAndSyncPost(postModelItem, currentUser)
                }
                isUserAlreadyLikedPost == true -> {
                    decrementAndSyncPost(postModelItem, currentUser)
                }
                isUserAlreadyLikedPost == false -> {
                    incrementAndSyncPost(postModelItem, currentUser)
                }
            }
        }
    }

    private suspend fun decrementAndSyncPost(
        postModelItem: PostModelItem,
        currentUser: User
    ) {
        val post = postModelItem.apply {
            this.likesCount = (this.likesCount ?: 0) - 1
        }
        val currentList = (currentUser.likedPosts ?: emptyList()) - (listOf(
            LikedPosts(post.id)
        )).toSet()

        currentUser.likedPosts = currentList
        syncLikedPost(post, currentUser)
    }

    private suspend fun incrementAndSyncPost(
        postModelItem: PostModelItem,
        currentUser: User
    ) {
        val post = postModelItem.apply {
            this.likesCount = (this.likesCount ?: 0) + 1
        }
        val currentList = (currentUser.likedPosts ?: emptyList()) + (listOf(
            LikedPosts(post.id)
        ))

        currentUser.likedPosts = currentList
        syncLikedPost(post, currentUser)
    }

    private suspend fun syncLikedPost(
        post: PostModelItem,
        currentUser: User
    ) {
        syncRepository.updateLikeForPost(post)
        syncRepository.updateUser(currentUser)
    }

    fun getFirstCommentForPost(postId: Int, commentData: ((HomeDataCommentsModel) -> Unit)) {
        viewModelScope.launch {
            val data = roomRepository.commentsDao.getAllCommentsForPostLiveData(postId)
            withContext(Dispatchers.Main) {
                commentData(data)
            }
        }
    }
}