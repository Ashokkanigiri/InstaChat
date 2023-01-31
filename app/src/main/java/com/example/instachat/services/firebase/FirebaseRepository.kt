package com.example.instachat.services.firebase

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.Comment
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.repository.RestApiRepository
import com.example.instachat.services.repository.RoomRepository
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FirebaseRepository @Inject constructor(
    val restApiRepository: RestApiRepository,
    val roomRepository: RoomRepository,
    @ApplicationContext val context: Context
) {


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
            roomRepository.postsDao.insertPost(post)
            FirebaseDataInjector.injectPostsToFirebase(post)
        }
    }

    private suspend fun injectUsersToFirebase() {
        val usersList = restApiRepository.dummyJsonRestClient.getAllUsers().users
        roomRepository.usersDao.insertUsers(usersList)
        usersList.forEach {
            FirebaseDataInjector.injectUsersToFirebase(it)
        }
    }

    private suspend fun injectCommentsToFirebase() {

        val commentsList = restApiRepository.dummyJsonRestClient.getAllComments().comments

        roomRepository.commentsDao.insertComments(commentsList)
        commentsList.forEach {
            FirebaseDataInjector.injectCommentsToFirebase(it)
        }
    }

    fun getAllPostsFromFirebase(firebaseDataListener: FirebaseDataListener?) {
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

    fun getAllCommentsFromFirebase(firebaseDataListener: FirebaseDataListener?) {
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

    fun getAllUsersFromFirebase(firebaseDataListener: FirebaseDataListener?) {
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

    fun getUserPosts(userId: Int, listener: FirebaseDataListener) {
        val db = Firebase.firestore

        db.collection("posts").whereLessThanOrEqualTo("userId", userId)
            .addSnapshotListener { value, error ->

                val dd = value?.documents?.map { it.data }?.map { it ->
                    Gson().fromJson(Gson().toJson(it), PostModelItem::class.java)
                }
                dd?.let {
                    listener.getDisplayablePosts(dd)
                }

            }
    }

    fun getAllCommentsForPost(postId: Int, listener: FirebaseCommentsListener?) {
        val db = Firebase.firestore

        db.collection("comments").whereEqualTo("postId", postId)
            .addSnapshotListener { value, error ->

                val commentsList = value?.documents?.map { it.data }?.map {
                    Gson().fromJson(Gson().toJson(it), Comment::class.java)
                }

                commentsList?.let {
                    listener?.getAllCommentsFromFirebase(commentsList)
                }
            }
    }

    fun getPost(postId: Int, listener: FirebasePostListener?) {
        val db = Firebase.firestore

        db.collection("posts").whereEqualTo("id", postId).addSnapshotListener { value, error ->

            val post = value?.documents?.map { it.data }?.map {
                Gson().fromJson(Gson().toJson(it), PostModelItem::class.java)
            }

            post?.let { post ->
                post.isNotEmpty()?.let {
                    listener?.getPost(post.get(0))
                }
            }
        }
    }

    fun getUser(userId: Int, listener: FirebaseUserListener?, getLoggedInUser: Boolean) {
        val db = Firebase.firestore

        db.collection("users").whereEqualTo("id", userId).addSnapshotListener { value, error ->

            val user = value?.documents?.map { it.data }?.map {
                Gson().fromJson(Gson().toJson(it), User::class.java)
            }

            user?.let { user ->
                user.isNotEmpty()?.let {
                    listener?.getUser(user.get(0), getLoggedInUser)
                }
            }
        }
    }

    fun postComment(comment: Comment) {
        val db = Firebase.firestore

        db.collection("comments").add(comment)
            .addOnFailureListener {
                Toast.makeText(context, "Error While adding comment", Toast.LENGTH_SHORT).show()
            }.addOnSuccessListener {
                Toast.makeText(context, "Comment sucessfully added", Toast.LENGTH_SHORT).show()

            }
    }


}