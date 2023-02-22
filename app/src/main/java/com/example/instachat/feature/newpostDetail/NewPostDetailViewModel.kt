package com.example.instachat.feature.newpostDetail

import android.net.Uri
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.repository.FirebaseRepository
import com.example.instachat.services.repository.SyncRepository
import com.example.instachat.utils.SingleLiveEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Random
import javax.inject.Inject

@HiltViewModel
class NewPostDetailViewModel
@Inject constructor(val firebaseRepository: FirebaseRepository, val syncRepository: SyncRepository) :
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
        postImageUrl = "",
        likesCount = 0,
        reactions = 0
    )

    fun onPostButtonClicked(postModelItem: PostModelItem) {
        postModelItem.apply {
            this.postImageUrl = selectedImagesList?.get(0) ?: ""
        }
        viewModelScope.launch (Dispatchers.IO){
            firebaseRepository.uploadPostImageToFirebase(postModelItem){
               if(true){
                   event.postValue(NewPostDetailViewModelEvent.IsPostAddedSuccessFully(postModelItem))
               }
            }
        }
    }

    fun addNewPost(postModelItem: PostModelItem){
        viewModelScope.launch (Dispatchers.IO){
            syncRepository.addNewPost(postModelItem)
        }
    }
}