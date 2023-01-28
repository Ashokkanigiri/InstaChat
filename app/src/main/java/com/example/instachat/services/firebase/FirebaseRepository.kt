package com.example.instachat.services.firebase

import androidx.lifecycle.viewModelScope
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.Comment
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.repository.RestApiRepository
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FirebaseRepository @Inject constructor(val restApiRepository: RestApiRepository) {


    suspend fun injectDatabasesToFirebase() {
        supervisorScope {
            injectUsersToFirebase()
            injectPostsToFirebase()
            injectCommentsToFirebase()
        }
    }

    private suspend fun injectPostsToFirebase() {
        val imageListResponse = restApiRepository.lorealImageListRestClient.getAllImages()
        val postsList = restApiRepository.dummyJsonRestClient.getAllPosts().posts

        postsList.forEach { post ->
            post.postImageUrl = imageListResponse.get(post.id - 1).download_url
            FirebaseDataInjector.injectPostsToFirebase(post)
        }
    }

    private suspend fun injectUsersToFirebase() {
        val usersList = restApiRepository.dummyJsonRestClient.getAllUsers().users

        usersList.forEach {
            FirebaseDataInjector.injectUsersToFirebase(it)
        }
    }

    private suspend fun injectCommentsToFirebase() {

        val commentsList = restApiRepository.dummyJsonRestClient.getAllComments().comments
        commentsList.forEach {
            FirebaseDataInjector.injectCommentsToFirebase(it)
        }
    }

    fun getAllPostsFromFirebase(firebaseDataListener: FirebaseDataListener?){
        val db = Firebase.firestore
         db.collection("posts").addSnapshotListener { value, error ->

             val dd = value?.documents?.map { it.data }?.map { it ->
                 Gson().fromJson(Gson().toJson(it), PostModelItem::class.java)
             }
             dd?.let {
                 firebaseDataListener?.getAllPosts(dd)
             }

            for (dc in value!!.documentChanges) {
                when (dc.type) {
                    DocumentChange.Type.ADDED -> {

                    }
                    DocumentChange.Type.MODIFIED -> {

                    }
                    DocumentChange.Type.REMOVED -> {

                    }
                }
            }

        }
    }

    fun getAllCommentsFromFirebase(firebaseDataListener: FirebaseDataListener?){
        val db = Firebase.firestore
        db.collection("comments").addSnapshotListener { value, error ->

            val dd = value?.documents?.map { it.data }?.map { it ->
                Gson().fromJson(Gson().toJson(it), Comment::class.java)
            }
            dd?.let {
                firebaseDataListener?.getAllComments(dd)
            }
            for (dc in value!!.documentChanges) {
                when (dc.type) {
                    DocumentChange.Type.ADDED -> {

                    }
                    DocumentChange.Type.MODIFIED -> {

                    }
                    DocumentChange.Type.REMOVED -> {

                    }
                }
            }

        }
    }

    fun getAllUsersFromFirebase(firebaseDataListener: FirebaseDataListener?){
        val db = Firebase.firestore
        db.collection("users").addSnapshotListener { value, error ->

            val dd = value?.documents?.map { it.data }?.map { it ->
                Gson().fromJson(Gson().toJson(it), User::class.java)
            }
            dd?.let {
                firebaseDataListener?.getAllUsers(dd)
            }

            for (dc in value!!.documentChanges) {
                when (dc.type) {
                    DocumentChange.Type.ADDED -> {

                    }
                    DocumentChange.Type.MODIFIED -> {

                    }
                    DocumentChange.Type.REMOVED -> {

                    }
                }
            }

        }
    }


}