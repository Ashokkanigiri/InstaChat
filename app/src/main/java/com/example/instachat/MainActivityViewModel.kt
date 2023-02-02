package com.example.instachat

import androidx.lifecycle.ViewModel
import com.example.instachat.services.repository.RestApiRepository
import com.example.instachat.services.repository.RoomRepositorySync
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val restApiRepository: RestApiRepository,
    val roomRepositorySync: RoomRepositorySync
) :
    ViewModel() {



}