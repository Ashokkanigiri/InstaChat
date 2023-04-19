package com.example.instachat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.models.rest.NotificationModel
import com.example.instachat.services.repository.RestApiRepository
import com.example.instachat.services.repository.RoomSyncRepository
import com.example.instachat.utils.Connectiontype
import com.example.instachat.utils.ConnectivityService
import com.example.instachat.utils.SingleLiveEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val restApiRepository: RestApiRepository,
    val roomSyncRepository: RoomSyncRepository,
    val connectivityService: ConnectivityService
) :
    ViewModel() {

    val shouldShowNetworkConnectionErrorSnackBar = SingleLiveEvent<Boolean>()

    val loggedUserId = Firebase.auth.currentUser?.uid?:""

    fun listenToNetworkConnection() {
        connectivityService.startListening {
            when (it) {
                Connectiontype.CELLULAR -> {
                    shouldShowNetworkConnectionErrorSnackBar.postValue(false)
                }
                Connectiontype.NONE -> {
                    shouldShowNetworkConnectionErrorSnackBar.postValue(true)
                }
                Connectiontype.WIFI -> {
                    shouldShowNetworkConnectionErrorSnackBar.postValue(false)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        connectivityService.stopListening()
    }
}