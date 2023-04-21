package com.example.instachat.feature.newpostDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.repository.FirebaseDataSource
import com.example.instachat.services.repository.SyncRepository
import com.example.instachat.utils.ConnectivityService
import com.example.instachat.utils.SingleLiveEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Random
import javax.inject.Inject

@HiltViewModel
class NewPostDetailViewModel
@Inject constructor(
    val firebaseDataSource: FirebaseDataSource,
    val syncRepository: SyncRepository,
    val connectivityService: ConnectivityService
) :
    ViewModel() {
    val adapter = NewPostDetailAdapter()

    val event = SingleLiveEvent<NewPostDetailViewModelEvent>()

    var selectedImagesList: List<String>? = null

    val newPost = PostModelItem(
        id = Random().nextInt(),
        userId = Firebase.auth.currentUser?.uid ?: "",
        tags = emptyList(),
        title = "",
        body = "",
        postImageUrls = emptyList(),
        likesCount = 0,
        reactions = 0
    )

    fun onPostButtonClicked(postModelItem: PostModelItem) {
        postModelItem.apply {
            this.postImageUrls = selectedImagesList ?: emptyList()
            this.postCreatedDate = System.currentTimeMillis().toString()
        }
        if(connectivityService.hasActiveNetwork()){
            viewModelScope.launch(Dispatchers.IO) {
                syncRepository.addNewPost(postModelItem){
                    event.postValue(NewPostDetailViewModelEvent.IsPostAdded(it))
                }
            }
        }else{
            event.postValue(NewPostDetailViewModelEvent.ShouldShowNetworkConnectionDialog)
        }
    }
}