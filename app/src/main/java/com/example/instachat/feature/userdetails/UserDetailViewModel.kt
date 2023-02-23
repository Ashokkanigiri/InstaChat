package com.example.instachat.feature.userdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instachat.services.repository.RoomRepository
import com.example.instachat.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(val roomRepository: RoomRepository) : ViewModel() {

    val event = SingleLiveEvent<UserDetailViewModelEvent>()

    val adapter = UserDetailPostsAdapter()

    fun loadUser(userId: String){
        viewModelScope.launch {
            event.postValue(UserDetailViewModelEvent.LoadUser(roomRepository.usersDao.getUser(userId)))
        }
    }

    fun loadAllPostsForUser(userId: String){
        viewModelScope.launch {
            roomRepository.postsDao.getPostsForUserDetails(userId).collect{
                event.postValue(UserDetailViewModelEvent.LoadPosts(it))
            }
        }
    }


}