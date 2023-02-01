package com.example.instachat.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instachat.services.firebase.FirebaseRepository
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.Comment
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.repository.RestApiRepository
import com.example.instachat.services.repository.RoomRepository
import com.example.instachat.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val firebaseRepository: FirebaseRepository,
    val roomRepository: RoomRepository,
    val restApiRepository: RestApiRepository
) : ViewModel() {

    val adapter = HomeDataAdapter(this)
    val usersAdapter = HomeUsersAdapter()
    var currentClickedPostAdapterPosition = 0

    val commentsLayoutClickedEvent = SingleLiveEvent<Int>()

    fun loadViewModel(){
        viewModelScope.launch(Dispatchers.IO) {

        }
    }

    /**
     * This method injects all the data from API into Firebase
     *
     * Need to be used with care
     */
    fun injectDatabases() {

        viewModelScope.launch(Dispatchers.IO) {
            firebaseRepository.getAllPostsFromFirebase()
            firebaseRepository.getAllUsersFromFirebase()
            firebaseRepository.getAllCommentsFromFirebase()
        }
    }

    fun getPostedUser(userId: Int?, userData: ((User?) -> Unit)) {
        viewModelScope.launch(Dispatchers.IO) {
            userId?.let {
                roomRepository.usersDao.getUser(userId)?.let {
                    withContext(Dispatchers.Main) {
                        userData.invoke(it)
                    }
                }
            }
        }
    }

    fun getFirstCommentForPost(postId: Int, comment: ((Comment) -> Unit)) {
        viewModelScope.launch(Dispatchers.IO) {
            roomRepository.commentsDao.getFirstCommentForPost(postId)?.let {
                withContext(Dispatchers.Main) {
                    comment.invoke(it)
                }
            }

        }
    }

    /**
     * This Method will be trigerred when comments edittext
     * in post gets clicked
     */
    fun onCommentsTextClicked(postId: Int, adapterPosition: Int) {
        currentClickedPostAdapterPosition = adapterPosition
        commentsLayoutClickedEvent.postValue(postId)
    }

    fun getUser(userId: Int): User? {
        var user: User? = null
        viewModelScope.async {
            user = roomRepository.usersDao.getUser(userId)
        }
        return user
    }

    fun refreshPost() {
        adapter.notifyItemChanged(currentClickedPostAdapterPosition)
    }
}