package com.example.instachat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instachat.services.client.FirebaseApiClient
import com.example.instachat.services.models.dummyjson.Session
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.repository.SyncRepository
import com.example.instachat.utils.ConnectivityService
import com.example.instachat.utils.Response
import com.example.instachat.utils.SingleLiveEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val firebaseApiClient: FirebaseApiClient,
    val connectivityService: ConnectivityService,
    val syncRepository: SyncRepository,
) : ViewModel() {

    val showConnectivityErrorDialog = SingleLiveEvent<Boolean>()
    val updateUserWorkId = SingleLiveEvent<UUID>()
    val navigateToMainActivity = SingleLiveEvent<Boolean>()
    val showNetworkConnectivityDialog = SingleLiveEvent<Boolean>()
    val handleError = SingleLiveEvent<String>()

    fun updateSessionIdToCurrentUser(sessionId: String, registrationToken: String) {

        if (connectivityService.hasActiveNetwork()) {
            val userId = Firebase.auth.currentUser?.uid ?: ""

            viewModelScope.launch(Dispatchers.IO) {
                val userResponse = firebaseApiClient.getSpecificUser(userId)

                when (userResponse) {
                    is Response.Failure -> {
                        handleError.postValue(userResponse.e.localizedMessage)
                    }
                    is Response.Loading -> {

                    }
                    is Response.Success -> {
                        val updatedUser = userResponse.data?.firstOrNull()

                        updatedUser?.userSessions?.let { sessions ->
                            if (!sessions.map { it.sessionID }.contains(sessionId)) {
                                syncUser(updatedUser, sessionId, registrationToken)
                            } else {
                                navigateToMainActivity.postValue(true)
                            }
                        }

                    }
                    is Response.HandleNetworkError ->{

                    }
                }
            }
        } else {
            showNetworkConnectivityDialog.postValue(true)
        }
    }

    private suspend fun syncUser(updatedUser: User, sessionId: String, registrationToken: String) {
        updatedUser.apply {
            this.userSessions = (this.userSessions ?: emptyList()) + (listOf(
                Session(
                    sessionID = sessionId,
                    registrationToken = registrationToken,
                    timeStamp = LocalDateTime.now().toString()
                )
            ))
        }
        if (connectivityService.hasActiveNetwork()) {
            syncRepository.updateUser(updatedUser) { uuid ->
                updateUserWorkId.postValue(uuid)
            }
        } else {
            showNetworkConnectivityDialog.postValue(true)
        }
    }

}