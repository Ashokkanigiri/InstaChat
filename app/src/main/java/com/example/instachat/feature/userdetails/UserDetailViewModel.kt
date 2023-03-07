package com.example.instachat.feature.userdetails

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.repository.FirebaseRepository
import com.example.instachat.services.repository.RoomRepository
import com.example.instachat.services.repository.SyncRepository
import com.example.instachat.utils.SingleLiveEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    val roomRepository: RoomRepository,
    val firebaseRepository: FirebaseRepository,
    val syncRepository: SyncRepository
) : ViewModel() {

    val followingStatusUpdate = ObservableField<String>()
    val event = SingleLiveEvent<UserDetailViewModelEvent>()

    val adapter = UserDetailPostsAdapter()

    private val currentLoggedInUser = Firebase.auth.currentUser

    var userId: String? = null

    fun loadUser(userId: String) {
        viewModelScope.launch {
            roomRepository.usersDao.getUserFlow(userId).collect {
                event.postValue(UserDetailViewModelEvent.LoadUser(it))
            }
        }
    }

    fun loadFollowingText(){
        viewModelScope.launch {
            val loggedUser = roomRepository.usersDao.getUser(currentLoggedInUser?.uid ?: "")
            if(loggedUser.followedUserIds?.contains(userId) == true){
               withContext(Dispatchers.Main){
                   followingStatusUpdate.set("Following")
               }
            }else{
                withContext(Dispatchers.Main){
                    followingStatusUpdate.set("Follow")
                }
            }
        }

    }

    fun loadAllPostsForUser(userId: String) {
        viewModelScope.launch {
            roomRepository.postsDao.getPostsForUserDetails(userId).collect {
                event.postValue(UserDetailViewModelEvent.LoadPosts(it))
            }
        }
    }

    fun onFollowButtonClicked() {
        updateUser(userId?:"")
        loadUser(userId?:"")
    }

    fun onMessageButtonClicked() {
        event.postValue(UserDetailViewModelEvent.OnMessageButtonClicked)
    }

    fun isCurrentUserProfile() = ((userId ?: "").equals((currentLoggedInUser?.uid ?: "")))

    private fun updateUser(followedUserId: String){
        viewModelScope.launch {
            val loggedUser = roomRepository.usersDao.getUser(currentLoggedInUser?.uid ?: "")
            loggedUser.apply {
                this.followedUserIds = this.followedUserIds?.plus(followedUserId)
            }
            syncRepository.updateFollowingStatus(loggedUser){
                event.postValue(UserDetailViewModelEvent.OnFollowStatusRequested(it))
            }
        }
    }

}