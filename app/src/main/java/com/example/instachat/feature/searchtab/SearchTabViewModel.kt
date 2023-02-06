package com.example.instachat.feature.searchtab

import androidx.lifecycle.ViewModel
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.repository.RoomRepository
import com.example.instachat.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchTabViewModel @Inject constructor(val roomRepository: RoomRepository): ViewModel() {

    val adapter = SearchTabHomeAdapter(this)

    val event = SingleLiveEvent<Int>()

    fun onPostItemClicked(postId: Int){
        event.postValue(postId)
    }


}