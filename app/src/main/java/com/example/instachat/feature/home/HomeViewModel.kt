package com.example.instachat.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instachat.services.firebase.FirebaseDataInjector
import com.example.instachat.services.models.PostModel
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.repository.RestApiRepository
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val restApiRepository: RestApiRepository): ViewModel() {

    val adapter =  HomeDataAdapter()

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
            val data = value?.documents?.map { it.data }?.map {it->
                Gson().fromJson(Gson().toJson(it), PostModelItem::class.java)
            }

            for(dc in value!!.documentChanges){
                when(dc.type){
                    DocumentChange.Type.ADDED ->{
                        adapter.submitList(data)
                    }
                    DocumentChange.Type.MODIFIED ->{

                    }
                    DocumentChange.Type.REMOVED ->{

                    }

                }
            }

        }

    }
}