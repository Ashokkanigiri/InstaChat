package com.example.instachat.feature.searchtab

import androidx.lifecycle.ViewModel
import com.example.instachat.services.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchTabViewModel @Inject constructor(val roomRepository: RoomRepository): ViewModel() {

    val adapter = SearchTabHomeAdapter()


}