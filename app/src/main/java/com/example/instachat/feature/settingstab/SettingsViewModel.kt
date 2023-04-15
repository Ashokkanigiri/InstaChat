package com.example.instachat.feature.settingstab

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instachat.services.client.FirebaseApiClient
import com.example.instachat.services.repository.RoomRepository
import com.example.instachat.services.repository.SyncRepository
import com.example.instachat.utils.ConnectivityService
import com.example.instachat.utils.Response
import com.example.instachat.utils.SingleLiveEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val roomRepository: RoomRepository,
    val firebaseApiClient: FirebaseApiClient,
    val syncRepository: SyncRepository,
    val connectivityService: ConnectivityService
) : ViewModel() {

    val event = SingleLiveEvent<SettingsViewModelEvent>()

    val currentUser = Firebase.auth.currentUser

    fun loadUserDetails() {
        viewModelScope.launch {
            val user = async { roomRepository.usersDao.getUser(currentUser?.uid ?: "") }
            event.postValue(SettingsViewModelEvent.LoadUserDetails(user.await()))

        }
    }

    fun onLogoutClicked() {
        event.postValue(SettingsViewModelEvent.HandleLogoutButtonClicked)
    }

    fun navigateToUserDetail() {
        event.postValue(SettingsViewModelEvent.NavigateToUserDetailFragment(currentUser?.uid ?: ""))
    }

    fun clearAllDatabases() {
        viewModelScope.launch(Dispatchers.IO) {
            roomRepository.clearAllDatabases()
        }
    }

    fun clearCurrentSession(sessionId: String?) {
        if(connectivityService.hasActiveNetwork()){
            val userId = Firebase.auth.currentUser?.uid ?: ""

            viewModelScope.launch(Dispatchers.IO) {
                val userResponse = firebaseApiClient.getSpecificUser(userId)

                when (userResponse) {
                    is Response.Failure -> {
                        event.postValue(SettingsViewModelEvent.HandleError(userResponse.e.localizedMessage))
                    }
                    Response.Loading -> {

                    }
                    is Response.Success -> {
                        val updatedUser = userResponse.data?.firstOrNull()

                        updatedUser?.userSessions?.let { sessions ->
                            updatedUser.userSessions =
                                updatedUser.userSessions?.toMutableList()?.apply {
                                    this.remove(updatedUser.userSessions?.filter { it.sessionID == sessionId }
                                        ?.firstOrNull())

                                }
                        }


                        updatedUser?.let {
                            syncRepository.updateUser(updatedUser){uuid->
                                event.postValue(SettingsViewModelEvent.ListenToRegistrationTokenDeleteWorkId(uuid))
                            }
                        }

                    }
                }


            }
        }else{
            event.postValue(SettingsViewModelEvent.ShowConnectivityErrorDialog)
        }
    }
}