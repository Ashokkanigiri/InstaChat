package com.example.instachat.feature.userdetails

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instachat.services.client.FirebaseApiClient
import com.example.instachat.services.models.dummyjson.InterestedUsersModel
import com.example.instachat.services.models.dummyjson.Session
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.models.fcm.FCMSendNotificationBody
import com.example.instachat.services.models.fcm.FCMSendNotificationData
import com.example.instachat.services.models.rest.NotificationModel
import com.example.instachat.services.repository.FirebaseDataSource
import com.example.instachat.services.repository.RoomDataSource
import com.example.instachat.services.repository.SyncRepository
import com.example.instachat.utils.ConnectivityService
import com.example.instachat.utils.Response
import com.example.instachat.utils.SingleLiveEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    val roomDataSource: RoomDataSource,
    val firebaseDataSource: FirebaseDataSource,
    val syncRepository: SyncRepository,
    val firebaseApiClient: FirebaseApiClient,
    val connectivityService: ConnectivityService,
    val userDetailsRepository: UserDetailsRepository
) : ViewModel() {

    val followingStatusUpdate = ObservableField<String>()

    val event = SingleLiveEvent<UserDetailViewModelEvent>()

    val progressBarVisibility = ObservableField<Boolean>()

    val connectivityDialogEvent = SingleLiveEvent<Boolean>()

    val adapter = UserDetailPostsAdapter()

    val handleError = SingleLiveEvent<String>()

    private val currentLoggedInUser = Firebase.auth.currentUser

    var userId: String? = null

    init {
        progressBarVisibility.set(true)
    }

    fun loadAllPostsForUser(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            roomDataSource.postsDao.getPostsForUserDetails(userId).collect {
                event.postValue(UserDetailViewModelEvent.LoadPosts(it))
            }
        }
    }

    fun onFollowButtonClicked() {
        event.postValue(UserDetailViewModelEvent.OnFollowButtonClicked)
    }

    fun onMessageButtonClicked() {
        event.postValue(UserDetailViewModelEvent.OnMessageButtonClicked)
    }

    fun isCurrentUserProfile() = ((userId ?: "").equals((currentLoggedInUser?.uid ?: "")))

    fun loadUser() {
        loadFollowingText()
        viewModelScope.launch(Dispatchers.IO) {
            when (val userDetails = userDetailsRepository.getUserDetails(userId?:"")) {
                is Response.Failure -> {
                    handleError.postValue(userDetails.e.localizedMessage)
                }
                is Response.Loading -> {

                }
                is Response.Success -> {
                    userDetails.data?.let {
                        event.postValue(UserDetailViewModelEvent.LoadUser(it))
                    }
                }
                is Response.HandleNetworkError -> {
                    connectivityDialogEvent.postValue(true)
                }
            }
        }
    }

    private fun startFollowProgressBar() {
        progressBarVisibility.set(true)
        followingStatusUpdate.set("")
    }

    private fun dismissFollowProgressBar() {
        progressBarVisibility.set(false)
    }

    private fun loadFollowingText() {
        viewModelScope.launch(Dispatchers.IO) {

            val interestedUserId = "${currentLoggedInUser?.uid}|${userId}"

            when (val interestedUserModel =
                userDetailsRepository.getInterestedUserModel(interestedUserId)) {

                is Response.Failure -> {
                    handleError.postValue(interestedUserModel.e.localizedMessage)
                    dismissFollowProgressBar()
                }
                is Response.Loading -> {
                    startFollowProgressBar()
                }
                is Response.Success -> {
                    updateFollowText(interestedUserModel.data)
                    dismissFollowProgressBar()
                }
                is Response.HandleNetworkError -> {
                    connectivityDialogEvent.postValue(true)
                }
            }
        }
    }

    private fun updateFollowText(interestedUserModel: InterestedUsersModel?) {
        when {
            interestedUserModel == null -> {
                followingStatusUpdate.set("Follow")
            }
            interestedUserModel.isFollowRequested && interestedUserModel.isFollowAccepted ->{
                followingStatusUpdate.set("Following")
            }
            interestedUserModel.isFollowRequested -> {
                followingStatusUpdate.set("Requested")
            }
            else -> {
                followingStatusUpdate.set("Follow")
            }
        }
    }

    /**
     * interestedUserId -> is created in such a way that
     *
     * (currentuserId) | (userId which you are placing interest)
     */
    fun handleFollowButtonClicked() {
        startFollowProgressBar()
        viewModelScope.launch(Dispatchers.IO) {

            val interestedUserId = "${currentLoggedInUser?.uid}|${userId}"

            when (val interestedUserModel =
                userDetailsRepository.getInterestedUserModel(interestedUserId)) {

                is Response.Failure -> {
                    handleError.postValue(interestedUserModel.e.localizedMessage)
                    dismissFollowProgressBar()
                }
                is Response.Loading -> {
                    startFollowProgressBar()
                }
                is Response.Success -> {
                    when (interestedUserModel.data?.isFollowRequested) {
                        true -> {
                            deleteInterestedModelAndDeLinkToUser(interestedUserModel.data)
                            deleteNotificationTriggeredByUser()
                            followingStatusUpdate.set("Follow")
                        }
                        false -> {
                            updateInterestedModel(interestedUserModel.data)
                            notifyAllUserSession(userId ?: "")
                            followingStatusUpdate.set("Requested")
                        }
                        null -> {
                            createNewInterestedModelAndLinkToUser(userId ?: "")
                            followingStatusUpdate.set("Requested")
                        }
                    }
                }
                is Response.HandleNetworkError -> {
                    connectivityDialogEvent.postValue(true)
                }
            }
        }
    }

    private suspend fun createNewInterestedModelAndLinkToUser(interestedUserId: String) {

        val interestedUser = InterestedUsersModel(
            id = "${currentLoggedInUser?.uid ?: ""}|${interestedUserId}",
            userId = currentLoggedInUser?.uid ?: "",
            isFollowAccepted = false,
            isFollowRejected = false,
            isFollowRequested = true,
            interestedUserId = interestedUserId,
            timeStamp = System.currentTimeMillis().toString()
        )

        when (val response =
            userDetailsRepository.addAndLinkInterestedModelToUser(interestedUser)) {
            is Response.Failure -> {
                handleError.postValue(response.e.localizedMessage)
                dismissFollowProgressBar()
            }
            is Response.Loading -> {
                startFollowProgressBar()
            }
            is Response.Success -> {
                getUserDetails(currentLoggedInUser?.uid ?: "") { loggedUser ->
                    linkInterestedModelToUser(loggedUser, interestedUser.id)
                    notifyAllUserSession(userId ?: "")
                    dismissFollowProgressBar()
                }
            }
            is Response.HandleNetworkError -> {
                connectivityDialogEvent.postValue(true)
            }
        }
    }

    private fun linkInterestedModelToUser(
        loggedUser: User?,
        interestedUserModelId: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val uUser = loggedUser?.apply {
                this.interestedUsersList =
                    (this.interestedUsersList ?: emptyList()) + listOf(
                        interestedUserModelId
                    )
            }
            uUser?.let {
                updateUserAndPushToFirebase(uUser)
            }
        }
    }

    private suspend fun getUserDetails(userId: String, user: ((User?) -> Unit)) {
        when (val userDetails = userDetailsRepository.getUserDetails(userId)) {
            is Response.Failure -> {
                handleError.postValue(userDetails.e.localizedMessage)
            }
            is Response.Loading -> {

            }
            is Response.Success -> {
                user.invoke(userDetails.data)
            }
            is Response.HandleNetworkError -> {
                connectivityDialogEvent.postValue(true)
            }
        }
    }

    private suspend fun updateInterestedModel(interestedUser: InterestedUsersModel) {
        when (val response = userDetailsRepository.updateInterestedModel(interestedUser)) {
            is Response.Failure -> {
                handleError.postValue(response.e.localizedMessage)
                dismissFollowProgressBar()
            }
            is Response.Loading -> {

            }
            is Response.Success -> {
                dismissFollowProgressBar()
            }
            is Response.HandleNetworkError -> {
                connectivityDialogEvent.postValue(true)
            }
        }
    }

    private suspend fun deleteInterestedModelAndDeLinkToUser(interestedUser: InterestedUsersModel) {
        when (val response =
            userDetailsRepository.deleteAndDeLinkInterestedModelToUser(interestedUser.id)) {
            is Response.Failure -> {
                handleError.postValue(response.e.localizedMessage)
                dismissFollowProgressBar()
            }
            is Response.Loading -> {

            }
            is Response.Success -> {
                getUserDetails(currentLoggedInUser?.uid ?: "") { loggedUser ->
                    deLinkInterestedModelFromUser(loggedUser, interestedUser.id)
                    dismissFollowProgressBar()
                }
            }
            is Response.HandleNetworkError -> {
                connectivityDialogEvent.postValue(true)
            }
        }
    }

    private suspend fun deleteNotificationTriggeredByUser(){
        val notificationId = "${currentLoggedInUser?.uid ?: ""}|${userId}"
        when(val response = userDetailsRepository.deleteNotificationTriggeredByUser(notificationId)){
            is Response.Failure -> {
                handleError.postValue(response.e.localizedMessage)
            }
            is Response.HandleNetworkError -> {
                connectivityDialogEvent.postValue(true)
            }
            is Response.Loading -> {

            }
            is Response.Success -> {

            }
        }

    }

    private fun deLinkInterestedModelFromUser(loggedUser: User?, interestedUserModelId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val uUser = loggedUser?.apply {
                this.interestedUsersList = (this.interestedUsersList?.toMutableList()
                    ?.minus(interestedUserModelId))
            }
            viewModelScope.launch(Dispatchers.IO) {
                uUser?.let {
                    updateUserAndPushToFirebase(uUser)
                }
            }
        }
    }

    private suspend fun updateUserAndPushToFirebase(user: User) {
        when (val response = userDetailsRepository.updateUser(user)) {
            is Response.Failure -> {
                handleError.postValue(response.e.localizedMessage)
            }
            is Response.Loading -> {

            }
            is Response.Success -> {
            }
            is Response.HandleNetworkError -> {
                connectivityDialogEvent.postValue(true)
            }
        }
    }

    private fun notifyAllUserSession(userId: String) {
        if (connectivityService.hasActiveNetwork()) {
            viewModelScope.launch(Dispatchers.IO) {
                val userResponse = userDetailsRepository.getUserDetails(userId)
                val loggedUser = roomDataSource.usersDao.getUser(currentLoggedInUser?.uid ?: "")

                when (userResponse) {
                    is Response.Failure -> {
                        handleError.postValue(userResponse.e.localizedMessage)
                    }
                    is Response.Loading -> {

                    }
                    is Response.Success -> {

                        val data = FCMSendNotificationData(
                            title = "${loggedUser.email} just requested to follow you",
                            body = "${loggedUser.email} requested to follow"
                        )

                        userResponse.data?.userSessions?.forEach {
                            triggerFCMNotification(it, data)
                        }

                        createNewNotificationForLoggedUser(data, loggedUser)
                    }
                    is Response.HandleNetworkError -> {
                        connectivityDialogEvent.postValue(true)
                    }
                }
            }
        }
    }

    private suspend fun triggerFCMNotification(session: Session, data: FCMSendNotificationData) {
        viewModelScope.launch(Dispatchers.IO) {
            val body = FCMSendNotificationBody(to = session.registrationToken, data)
            when (val response = userDetailsRepository.sendFCMNotification(body)) {
                is Response.Failure -> {
                    handleError.postValue(response.e.localizedMessage)
                }
                is Response.HandleNetworkError -> {
                    connectivityDialogEvent.postValue(true)
                }
                is Response.Loading -> {

                }
                is Response.Success -> {

                }
            }
        }
    }

    private suspend fun createNewNotificationForLoggedUser(
        data: FCMSendNotificationData,
        loggedUser: User
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val notification = NotificationModel(
                id = "${currentLoggedInUser?.uid ?: ""}|${userId}",
                targetUserId = userId ?: "",
                triggeredUserId = currentLoggedInUser?.uid ?: "",
                title = data.title,
                body = data.body,
                timeStamp = com.example.instachat.utils.DateUtils.getCurrentTimeInMillis()
                    ?: "",
                triggeredUserImageUrl = loggedUser.image
            )

            when (val response = userDetailsRepository.injectNotification(notification)) {
                is Response.Failure -> {
                    handleError.postValue(response.e.localizedMessage)
                }
                is Response.HandleNetworkError -> {
                    connectivityDialogEvent.postValue(true)
                }
                is Response.Loading -> {

                }
                is Response.Success -> {

                }
            }
        }
    }

}