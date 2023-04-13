package com.example.instachat

import androidx.lifecycle.ViewModel
import com.example.instachat.services.repository.RestApiRepository
import com.example.instachat.services.repository.RoomSyncRepository
import com.example.instachat.utils.Connectiontype
import com.example.instachat.utils.ConnectivityService
import com.example.instachat.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val restApiRepository: RestApiRepository,
    val roomSyncRepository: RoomSyncRepository,
    val connectivityService: ConnectivityService
) :
    ViewModel() {

    val shouldShowNetworkConnectionErrorSnackBar = SingleLiveEvent<Boolean>()

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