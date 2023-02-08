package com.example.instachat.feature.searchtab

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.repository.RoomRepository
import com.example.instachat.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchTabViewModel @Inject constructor(val roomRepository: RoomRepository) : ViewModel() {

    val adapter = SearchTabHomeAdapter(this)

    val event = SingleLiveEvent<SearchViewModelEvent>()


    fun onPostItemClicked(postId: Int) {
        event.postValue(SearchViewModelEvent.NavigateToHomeFragment(postId))
    }

    fun loadAllPosts() {
        viewModelScope.launch {
            roomRepository.postsDao.getAllPostsFlow().collect() {
                event.postValue(SearchViewModelEvent.GetAllPosts(it))
            }
        }
    }


    fun onSearchTextChanged(text: CharSequence){

    }

    fun onSearchFocusChanged(view: View, hasFocus: Boolean){
        event.postValue(SearchViewModelEvent.HandleSearchFocus(hasFocus))
    }

    fun onSearchBackButtonPressed(){
        event.postValue(SearchViewModelEvent.HandleSearchFocus(false))
    }


}