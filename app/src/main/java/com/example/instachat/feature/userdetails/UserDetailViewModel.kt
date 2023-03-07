package com.example.instachat.feature.userdetails

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instachat.services.models.dummyjson.InterestedUsersModel
import com.example.instachat.services.models.dummyjson.RequestedForInterestModel
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.repository.FirebaseRepository
import com.example.instachat.services.repository.RoomRepository
import com.example.instachat.services.repository.SyncRepository
import com.example.instachat.utils.SingleLiveEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
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

    fun loadLoggedUser(){
        viewModelScope.launch {
            roomRepository.usersDao.getUserFlow(currentLoggedInUser?.uid?:"").collect(){
                event.postValue(UserDetailViewModelEvent.LoadLoggedUser(it))
            }
        }
    }

    fun loadFollowingText(user: User) {
        viewModelScope.launch {
            val iList = roomRepository.interestedUsersDao.getAllInterestedUsers(user.id)
            val interestedUser = iList?.firstOrNull { it.interestedUserId == userId }


            if (interestedUser != null) {
                when {
                    interestedUser.isFollowAccepted && interestedUser.isFollowRequested -> {
                        withContext(Dispatchers.Main) {
                            followingStatusUpdate.set("Following")
                        }
                    }
                    interestedUser.isFollowRequested -> {
                        followingStatusUpdate.set("Requested")
                    }
                    else -> {
                        withContext(Dispatchers.Main) {
                            followingStatusUpdate.set("Follow")
                        }
                    }
                }


            } else {
                followingStatusUpdate.set("Follow")
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
        updateUser(userId ?: "")
    }

    fun onMessageButtonClicked() {
        event.postValue(UserDetailViewModelEvent.OnMessageButtonClicked)
    }

    fun isCurrentUserProfile() = ((userId ?: "").equals((currentLoggedInUser?.uid ?: "")))

    private fun updateUser(followedUserId: String) {

        val interestedUser = InterestedUsersModel(
            id = UUID.randomUUID()?.toString() ?: "",
            userId = currentLoggedInUser?.uid ?: "",
            isFollowAccepted = false,
            isFollowRejected = false,
            isFollowRequested = true,
            interestedUserId = followedUserId,
            timeStamp = System.currentTimeMillis().toString()
        )

        val requestedForInterestModel = RequestedForInterestModel(
            id = UUID.randomUUID().toString(),
            requestedUserId = currentLoggedInUser?.uid ?: "",
            interestId = interestedUser.id
        )

        viewModelScope.launch {
            syncRepository.addAndSyncUserInterestedList(interestedUser)
            syncRepository.addAndSyncRequestedInterestsList(requestedForInterestModel) {
                event.postValue(
                    UserDetailViewModelEvent.OnFollowStatusRequested(
                        it,
                        interestedUser,
                        requestedForInterestModel
                    )
                )
            }
        }
    }

    fun addInterestedUserToLoggedUser(interestedUsersModel: InterestedUsersModel) {
        viewModelScope.launch {
            val loggedUser =roomRepository.usersDao.getUser(currentLoggedInUser?.uid?:"")
            val uUser = loggedUser.apply {
                this.interestedUsersList = (this.interestedUsersList?: emptyList()) + listOf(interestedUsersModel.id)
            }
            syncRepository.updateUsersInterestedUsers(uUser)
        }
    }

    fun addRequestForInterestTOCurrentUser(requestedForInterestModel: RequestedForInterestModel) {
        viewModelScope.launch {
            val loggedUser =roomRepository.usersDao.getUser(currentLoggedInUser?.uid?:"")
            val uUser = loggedUser.apply {
                this.requestedForInterestsList = (this.requestedForInterestsList?: emptyList()) + listOf(requestedForInterestModel.id)
            }
            syncRepository.updateRequestedInterestedUsers(uUser)
        }
    }

}