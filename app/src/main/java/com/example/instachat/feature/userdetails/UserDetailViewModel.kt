package com.example.instachat.feature.userdetails

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instachat.services.client.FirebaseApiClient
import com.example.instachat.services.models.dummyjson.InterestedUsersModel
import com.example.instachat.services.models.dummyjson.RequestedForInterestModel
import com.example.instachat.services.models.fcm.FCMSendNotificationBody
import com.example.instachat.services.models.fcm.FCMSendNotificationData
import com.example.instachat.services.repository.FirebaseRepository
import com.example.instachat.services.repository.RoomRepository
import com.example.instachat.services.repository.SyncRepository
import com.example.instachat.services.rest.FCMRestClient
import com.example.instachat.utils.ConnectivityService
import com.example.instachat.utils.Response
import com.example.instachat.utils.SingleLiveEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    val roomRepository: RoomRepository,
    val firebaseRepository: FirebaseRepository,
    val syncRepository: SyncRepository,
    val fcmRestClient: FCMRestClient,
    val firebaseApiClient: FirebaseApiClient,
    val connectivityService: ConnectivityService
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

    fun loadFollowingText() {
        viewModelScope.launch {

            val intrestedUser = roomRepository.interestedUsersDao.getAllInterestedUsersForUserId(
                currentLoggedInUser?.uid ?: ""
            )?.firstOrNull { it.interestedUserId == userId }

            when {
                intrestedUser == null -> {
                    followingStatusUpdate.set("Follow")
                }
                intrestedUser.isFollowRequested -> {
                    followingStatusUpdate.set("Requested")
                }
                intrestedUser.isFollowAccepted -> {
                    followingStatusUpdate.set("Following")
                }
                else -> {
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
        if (followingStatusUpdate.get()?.toLowerCase() == "follow") {
            updateUser(userId ?: "")
        }
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
            val loggedUser = roomRepository.usersDao.getUser(currentLoggedInUser?.uid ?: "")
            val uUser = loggedUser.apply {
                this.interestedUsersList =
                    (this.interestedUsersList ?: emptyList()) + listOf(interestedUsersModel.id)
            }
            syncRepository.updateUsersInterestedUsers(uUser)
        }
    }

    fun addRequestForInterestTOCurrentUser(requestedForInterestModel: RequestedForInterestModel) {
        viewModelScope.launch {
            val loggedUser = roomRepository.usersDao.getUser(userId ?: "")
            val uUser = loggedUser.apply {
                this.requestedForInterestsList =
                    (this.requestedForInterestsList ?: emptyList()) + listOf(
                        requestedForInterestModel.id
                    )
            }
            syncRepository.updateRequestedInterestedUsers(uUser)
        }
    }

    fun notifyAllUserSession(userId: String) {
        if (connectivityService.hasActiveNetwork()) {
            viewModelScope.launch(Dispatchers.IO) {
                val userResponse = firebaseApiClient.getSpecificUser(userId)
                val loggedU = roomRepository.usersDao.getUser(currentLoggedInUser?.uid?:"")

                when (userResponse) {
                    is Response.Failure -> {

                    }
                    is Response.Loading -> {

                    }
                    is Response.Success -> {
                        val user = userResponse.data?.firstOrNull()

                        user?.userSessions?.forEach {
                            viewModelScope.launch {
                                val data = FCMSendNotificationData(title = "${loggedU.email} just requested to follow you", body = "${loggedU.email} requested to follow")
                                val body = FCMSendNotificationBody(to = it.registrationToken, data)
                                fcmRestClient.sendFCMNotification( body)
                            }
                        }

                    }
                }
            }
        }
    }

}