package com.example.instachat.feature.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instachat.services.firebase.FirebaseRepository
import com.example.instachat.services.models.dummyjson.LikedPosts
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.repository.RestApiRepository
import com.example.instachat.services.repository.RoomRepository
import com.example.instachat.services.repository.RoomRepositorySync
import com.example.instachat.services.repository.SyncRepository
import com.example.instachat.utils.SingleLiveEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val firebaseRepository: FirebaseRepository,
    val roomRepository: RoomRepository,
    val roomRepositorySync: RoomRepositorySync,
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

            if (currentUser.likedPosts != null || (currentUser.likedPosts?.isNotEmpty() ?: false)) {

                val isUserLikedPost =
                    currentUser.likedPosts?.map { it.postId }?.contains(postModelItem.id)

                /**
                 * user already had liked the post
                 *
                 * removed likes (likes - 1) count in post table
                 *
                 * removed the post from current user likedPosts list
                 */
                if (isUserLikedPost == true) {

                    val post = postModelItem.apply {
                        this.likesCount = (this.likesCount ?: 0) - 1
                    }

                    val currentList = (currentUser.likedPosts ?: emptyList()) - (listOf(
                        LikedPosts(post.id)
                    ))

                    currentUser.likedPosts = currentList

                    syncRepository.updateLikeForPost(post)
                    syncRepository.updateUser(currentUser)


                }

                /**
                 * User havent liked the post yet -> adding post id into user
                 *
                 * adding postitem likes count
                 */
                else {

                    val post = postModelItem.apply {
                        this.likesCount = (this.likesCount ?: 0) + 1
                    }

                    val currentList = (currentUser.likedPosts ?: emptyList()) + (listOf<LikedPosts>(
                        LikedPosts(post.id)
                    ))


                    currentUser.likedPosts = currentList


                    syncRepository.updateLikeForPost(post)
                    syncRepository.updateUser(currentUser)

                }
            } else {

                val post = postModelItem.apply {
                    this.likesCount = (this.likesCount ?: 0) + 1
                }

                val currentList = (currentUser.likedPosts ?: emptyList()) + (listOf<LikedPosts>(
                    LikedPosts(post.id)
                ))


                currentUser.likedPosts = currentList

                syncRepository.updateLikeForPost(post)
                syncRepository.updateUser(currentUser)
            }
        }
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