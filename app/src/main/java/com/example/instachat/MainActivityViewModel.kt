package com.example.instachat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instachat.services.firebase.FirebaseDataInjector
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.repository.RestApiRepository
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(val restApiRepository: RestApiRepository) :
    ViewModel() {

    fun injectPostsToFirebase() {
        viewModelScope.launch (Dispatchers.IO){
            val imageListResponse = restApiRepository.lorealImageListRestClient.getAllImages()
            val postsList = restApiRepository.jsonPlaceHolderApiClient.getAllPosts()

            postsList.forEach { post ->
                post.postImageUrl = imageListResponse.get(post.id - 1).download_url
                viewModelScope.launch(Dispatchers.IO) {
                    FirebaseDataInjector.injectPostsToFirebase(post)
                }
            }
        }
        getAllPosts()
    }

    fun getAllPosts(){
        val db = Firebase.firestore
        db.collection("posts").addSnapshotListener { value, error ->
            val hp = value?.documents?.map { it.data }

            for(dc in value!!.documentChanges){
                when(dc.type){
                     DocumentChange.Type.ADDED ->{
                         Log.d("added", "DATA : ${value?.documents}")
                    }
                    DocumentChange.Type.MODIFIED ->{
                        Log.d("added", "DATA : ${value?.documents}")

                    }
                    DocumentChange.Type.REMOVED ->{
                        Log.d("removed", "DATA : ${value?.documents}")

                    }

                }
            }

        }

    }

}