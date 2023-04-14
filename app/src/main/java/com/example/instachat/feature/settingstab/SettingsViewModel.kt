package com.example.instachat.feature.settingstab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instachat.services.repository.RoomRepository
import com.example.instachat.utils.SingleLiveEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(val roomRepository: RoomRepository): ViewModel() {

    val event = SingleLiveEvent<SettingsViewModelEvent>()

    val currentUser = Firebase.auth.currentUser

    fun loadUserDetails(){
        viewModelScope.launch {
            val user = async { roomRepository.usersDao.getUser(currentUser?.uid?:"") }
                event.postValue(SettingsViewModelEvent.LoadUserDetails(user.await()))

        }
    }

    fun onLogoutClicked(){
        event.postValue(SettingsViewModelEvent.HandleLogoutButtonClicked)
    }

    fun navigateToUserDetail(){
        event.postValue(SettingsViewModelEvent.NavigateToUserDetailFragment(currentUser?.uid?:""))
    }

    fun clearAllDatabases(){
        viewModelScope.launch (Dispatchers.IO){
            roomRepository.clearAllDatabases()
        }
    }
}