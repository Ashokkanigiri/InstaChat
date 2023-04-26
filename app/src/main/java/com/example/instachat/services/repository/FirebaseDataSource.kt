package com.example.instachat.services.repository

import android.content.Context
import com.example.instachat.services.client.FirebaseApiClient
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.Comment
import com.example.instachat.services.models.dummyjson.InterestedUsersModel
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.models.rest.NotificationModel
import com.example.instachat.utils.Response
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseDataSource @Inject constructor(
    @ApplicationContext val context: Context,
    val roomDataSource: RoomDataSource,
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

    suspend fun injectNotificationsToFirebaseForLoggedUser(notificationModel: NotificationModel): Response<Boolean> {
        return firebaseApiClient.injectNotification(notificationModel)
    }

    suspend fun injectAllPostsFromFirebase() {
        when (val posts = firebaseApiClient.getAllPostsFromFirebase()) {
            is Response.Success -> {
                posts.data?.let {
                    roomDataSource.postsDao.insert(posts.data)
                }
            }
            is Response.Failure -> {

            }
            Response.Loading -> {

            }
            is Response.HandleNetworkError ->{

            }
        }
    }


    suspend fun injectAllCommentsFromFirebase() {
        when (val comments = firebaseApiClient.getAllCommentsFromFirebase()) {
            is Response.Success -> {
                comments.data?.let {
                    roomDataSource.commentsDao.insert(comments.data)
                }
            }
            is Response.Failure -> {

            }
            Response.Loading -> {

            }
            is Response.HandleNetworkError ->{

            }
        }

    }

    suspend fun injectAllUsersFromFirebase() {
        when (val users = firebaseApiClient.getAllUsersFromFirebase()) {
            is Response.Success -> {
                users.data?.let {
                    roomDataSource.usersDao.insert(users.data)
                }
            }
            is Response.Failure -> {

            }
            Response.Loading -> {

            }
            is Response.HandleNetworkError ->{

            }
        }
    }

    suspend fun injectAllNotificationsFromFirebase(userId: String) {
        firebaseApiClient.getAllNotificationsFromFirebase(userId)

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

    suspend fun getInterestedUserModel(id: String): Response<InterestedUsersModel>{
        return try {
            val db = Firebase.firestore
            val interestedUserModel = db.collection("interestedUserRequests").whereEqualTo("id", id).get().await().toObjects(InterestedUsersModel::class.java).firstOrNull()
            Response.Success(interestedUserModel)
        } catch (e: java.lang.Exception) {
            Response.Failure(e)
        }
    }

}