package com.example.instachat.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instachat.services.firebase.FirebaseRepository
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.Comment
import com.example.instachat.services.models.dummyjson.LikedPosts
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.repository.RestApiRepository
import com.example.instachat.services.repository.RoomRepository
import com.example.instachat.services.repository.RoomRepositorySync
import com.example.instachat.services.repository.SyncRepository
import com.example.instachat.utils.SingleLiveEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val firebaseRepository: FirebaseRepository,
    val roomRepository: RoomRepository,
    val restApiRepository: RestApiRepository,
    val roomRepositorySync: RoomRepositorySync,
    val syncRepository: SyncRepository
) : ViewModel() {

    val adapter = HomeDataAdapter(this)
    val usersAdapter = HomeUsersAdapter()
    var currentClickedPostAdapterPosition = 0
    val commentsLayoutClickedEvent = SingleLiveEvent<Int>()
    val auth = Firebase.auth

    fun loadViewModel() {
        viewModelScope.launch(Dispatchers.IO) {
            roomRepository.postsDao.getPostsHomeData(auth.currentUser?.uid?:"").collect {
                withContext(Dispatchers.Main){
                    adapter.submitList(it)
                    usersAdapter.submitList(it)
                }
            }
        }
    }

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
    fun onCommentsTextClicked(postId: Int, adapterPosition: Int) {
        currentClickedPostAdapterPosition = adapterPosition
        commentsLayoutClickedEvent.postValue(postId)
    }

    fun getUser(userId: String): User? {
        var user: User? = null
        viewModelScope.async {
            user = roomRepository.usersDao.getUser(userId)
        }
        return user
    }

    fun onLikeButtonClicked(homeDataModel: HomeDataModel) {
        viewModelScope.launch {
            val currentUser = roomRepository.usersDao.getUser(auth.currentUser?.uid ?: "0")
            val postModelItem = roomRepository.postsDao.getPost(homeDataModel.postId)

            if (currentUser.likedPosts != null) {

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

                    val currentList = (currentUser.likedPosts ?: emptyList()) - (listOf<LikedPosts>(
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

                val currentList = currentUser.likedPosts ?: emptyList()

                currentList.toMutableList().add(LikedPosts(post.id))

                currentUser.likedPosts = currentList


                syncRepository.updateLikeForPost(post)
                syncRepository.updateUser(currentUser)
            }
        }
    }

    fun refreshPost() {
        adapter.notifyItemChanged(currentClickedPostAdapterPosition)
    }
}