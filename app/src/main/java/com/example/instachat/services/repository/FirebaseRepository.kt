package com.example.instachat.services.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.instachat.services.client.FirebaseApiClient
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.Comment
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.utils.Response
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FirebaseRepository @Inject constructor(
    @ApplicationContext val context: Context,
    val roomRepository: RoomRepository,
    val roomSyncRepository: RoomSyncRepository,
    private val firebaseApiClient: FirebaseApiClient
) {

    suspend fun injectCommentsToFirebase(data: Comment): Response<Boolean> {
        return firebaseApiClient.injectCommentsToFirebase(data)
    }

    suspend fun injectUsersToFirebase(data: User): Response<Boolean> {
        return firebaseApiClient.injectUsersToFirebase(data)
    }

    suspend fun injectPostsToFirebase(data: PostModelItem): Response<Boolean> {
        return firebaseApiClient.injectPostsToFirebase(data)
    }

    suspend fun injectAllPostsFromFirebase() {
        when (val posts = firebaseApiClient.getAllPostsFromFirebase()) {
            is Response.Success -> {
                posts.data?.let {
                    roomRepository.postsDao.insert(posts.data)
                }
            }
            is Response.Failure -> {

            }
            Response.Loading -> {

            }
        }
    }


    suspend fun injectAllCommentsFromFirebase() {
        when (val comments = firebaseApiClient.getAllCommentsFromFirebase()) {
            is Response.Success -> {
                comments.data?.let {
                    roomRepository.commentsDao.insert(comments.data)
                }
            }
            is Response.Failure -> {

            }
            Response.Loading -> {

            }
        }

    }

    suspend fun injectAllUsersFromFirebase() {
        when (val users = firebaseApiClient.getAllUsersFromFirebase()) {
            is Response.Success -> {
                users.data?.let {
                    roomRepository.usersDao.insert(users.data)
                }
            }
            is Response.Failure -> {

            }
            Response.Loading -> {

            }
        }
    }

    suspend fun isUserAlreadyExists(userEmail: String): Response<Boolean> {
        return firebaseApiClient.isUserAlreadyExists(userEmail)
    }

    suspend fun uploadPostImageToFirebase(postModelItem: PostModelItem): Response<List<String>> {
        return try {
            val list = postModelItem.postImageUrls?.map {
                firebaseApiClient.uploadPostImageToFirebase(
                    postModelItem,
                    it
                )
            }
            return Response.Success(list)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }
}