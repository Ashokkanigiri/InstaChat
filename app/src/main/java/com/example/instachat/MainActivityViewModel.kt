package com.example.instachat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instachat.services.client.FirebaseApiClient
import com.example.instachat.services.repository.FirebaseDataSource
import com.example.instachat.services.repository.RestApiRepository
import com.example.instachat.services.repository.RoomSyncRepository
import com.example.instachat.utils.Connectiontype
import com.example.instachat.utils.ConnectivityService
import com.example.instachat.utils.SingleLiveEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val restApiRepository: RestApiRepository,
    val roomSyncRepository: RoomSyncRepository,
    val connectivityService: ConnectivityService,
    val firebaseDataSource: FirebaseDataSource,
    val firebaseApiClient: FirebaseApiClient
) :
    ViewModel() {

    val shouldShowNetworkConnectionErrorSnackBar = SingleLiveEvent<Boolean>()

    val loggedUserId = Firebase.auth.currentUser?.uid?:""

    fun injectAllNotifications(){
        viewModelScope.launch(Dispatchers.IO) {
            firebaseDataSource.injectAllNotificationsFromFirebase(loggedUserId)
        }
    }

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