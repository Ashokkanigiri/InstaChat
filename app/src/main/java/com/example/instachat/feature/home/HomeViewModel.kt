package com.example.instachat.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instachat.services.firebase.FirebaseDataInjector
import com.example.instachat.services.firebase.FirebaseDataListener
import com.example.instachat.services.firebase.FirebaseRepository
import com.example.instachat.services.models.PostModel
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.Comment
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.repository.RestApiRepository
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val firebaseRepository: FirebaseRepository) : ViewModel(),
    FirebaseDataListener {

    val adapter = HomeDataAdapter()

    fun injectDatabases() {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseRepository.injectDatabasesToFirebase()
        }
    }

    fun getAllDataFromFirebase() {
        firebaseRepository.getAllPostsFromFirebase(this)
        firebaseRepository.getAllCommentsFromFirebase(this)
        firebaseRepository.getAllUsersFromFirebase(this)
    }


    override fun getAllPosts(posts: List<PostModelItem>) {
        adapter.submitList(posts)

    }

    override fun getAllComments(comments: List<Comment>) {
    }

    override fun getAllUsers(users: List<User>) {
    }

    override fun onCleared() {
        firebaseRepository.getAllPostsFromFirebase(null)
        firebaseRepository.getAllCommentsFromFirebase(null)
        firebaseRepository.getAllUsersFromFirebase(null)
    }

}