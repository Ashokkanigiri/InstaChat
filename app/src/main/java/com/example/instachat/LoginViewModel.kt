package com.example.instachat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instachat.services.repository.FirebaseRepository
import com.example.instachat.utils.ConnectivityService
import com.example.instachat.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val firebaseRepository: FirebaseRepository, val connectivityService: ConnectivityService): ViewModel() {

    val showConnectivityErrorDialog = SingleLiveEvent<Boolean>()
    fun injectDataFromFirebase() {
        viewModelScope.launch(Dispatchers.IO) {
           if(connectivityService.hasActiveNetwork()){
               firebaseRepository.getAllPostsFromFirebase()
               firebaseRepository.getAllUsersFromFirebase()
               firebaseRepository.getAllCommentsFromFirebase()
           }else{
               showConnectivityErrorDialog.postValue(true)
           }
        }
    }
}