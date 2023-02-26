package com.example.instachat.feature.settingstab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instachat.services.repository.RoomRepository
import com.example.instachat.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(val roomRepository: RoomRepository): ViewModel() {

    val event = SingleLiveEvent<SettingsViewModelEvent>()

    fun onLogoutClicked(){
        event.postValue(SettingsViewModelEvent.HandleLogoutButtonClicked)
    }

    fun clearAllDatabases(){
        viewModelScope.launch (Dispatchers.IO){
            roomRepository.clearAllDatabases()
        }
    }
}