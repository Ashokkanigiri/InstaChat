package com.example.instachat.services.client

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.Comment
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.models.dummyjson.UsersModel
import com.example.instachat.services.models.rest.NotificationModel
import com.example.instachat.services.repository.RoomRepository
import com.example.instachat.services.repository.RoomSyncRepository
import com.example.instachat.utils.Response
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseApiClient @Inject constructor(
    @ApplicationContext val context: Context,
    val roomRepository: RoomRepository,
    val roomSyncRepository: RoomSyncRepository
) {

    suspend fun injectCommentsToFirebase(data: Comment): Response<Boolean> {
        return try {
            val db = Firebase.firestore
            db.collection("comments")
                .document(data.id.toString())
                .set(data)
                .await()
            Response.Success(true)
        } catch (e: java.lang.Exception) {
            Response.Failure(e)
        }
    }


    suspend fun injectUsersToFirebase(data: User): Response<Boolean> {
        return try {
            val db = Firebase.firestore
            db.collection("users")
                .document(data.id.toString())
                .set(data)
                .await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    suspend fun injectPostsToFirebase(data: PostModelItem): Response<Boolean> {
        return try {
            val db = Firebase.firestore
            db.collection("posts")
                .document(data.id.toString())
                .set(data)
                .await()
            Response.Success(true)
        } catch (e: java.lang.Exception) {
            Response.Failure(e)
        }

    }

    suspend fun injectNotification(data: NotificationModel): Response<Boolean> {
        val user = Firebase.auth.currentUser?.uid ?:""
        return try {
            val db = Firebase.firestore
            db.collection("notifications")
                .document(data.id.toString())
                .set(data)
                .await()

            updateNotificationToUser(data, user)
            Response.Success(true)
        } catch (e: java.lang.Exception) {
            Response.Failure(e)
        }

    }

    suspend fun updateNotificationToUser(data: NotificationModel, userId: String) :Response<Boolean> {
        return try {
            val db = Firebase.firestore
            var user = db.collection("users")
                .document(data.targetUserId)
                .get()
                .await()
                .toObject(User::class.java)

            user = user?.apply {
                this.notifications = (this.notifications?: emptyList()) + listOf(data.id)
            }


            db.collection("users")
                .document(data.targetUserId)
                .update("notifications", user?.notifications)
                .await()

            Response.Success(true)
        } catch (e: java.lang.Exception) {
            Response.Failure(e)
        }
    }

    suspend fun getAllPostsFromFirebase(): Response<List<PostModelItem>> {
        return try {
            val db = Firebase.firestore
            val posts = db.collection("posts").get().await().toObjects(PostModelItem::class.java)
            Response.Success(posts)
        } catch (e: java.lang.Exception) {
            Response.Failure(e)
        }
    }

    suspend fun getSpecificUser(userId: String): Response<List<User>> {
        return try {
            val db = Firebase.firestore
            val user = db.collection("users").whereEqualTo("id", userId).get().await().toObjects(User::class.java)
            Response.Success(user)
        } catch (e: java.lang.Exception) {
            Response.Failure(e)
        }
    }

    suspend fun getAllCommentsFromFirebase(): Response<List<Comment>> {
        return try {
            val db = Firebase.firestore
            val comments = db.collection("comments").get().await().toObjects(Comment::class.java)
            Response.Success(comments)
        } catch (e: java.lang.Exception) {
            Response.Failure(e)
        }

    }

    suspend fun getAllUsersFromFirebase(): Response<List<User>> {
        return try {
            val db = Firebase.firestore
            val users = db.collection("users").get().await().toObjects(User::class.java)
            Response.Success(users)
        } catch (e: java.lang.Exception) {
            Response.Failure(e)
        }

    }

    suspend fun getAllNotificationsFromFirebase(userId: String): Response<List<NotificationModel>> {
        return try {
            val db = Firebase.firestore
            val users = db.collection("notifications").whereEqualTo("targetUserId", userId).get().await().toObjects(NotificationModel::class.java)
            val op = roomRepository.notificationModelDao.insertNotifications(users)
            Log.d("wkjwljkngf", "OP: ${op}")
            Response.Success(users)
        } catch (e: java.lang.Exception) {
            Response.Failure(e)
        }
    }


    suspend fun isUserAlreadyExists(userEmail: String): Response<Boolean> {
        return try {
            val db = Firebase.firestore
            val data = db.collection("users").whereEqualTo("email", userEmail).get().await()
            if(data?.documents?.size == 0){
                Response.Success(false)
            }else{
                Response.Success(true)
            }
        }catch (e: Exception){
            Response.Failure(e)
        }
    }

    suspend fun uploadPostImageToFirebase(
        postModelItem: PostModelItem,
        postImageUrl: String
    ): String {
        return try {
            val storage = Firebase.storage
            val storageRef = storage.reference
            val imageRef =
                storageRef.child("users/${postModelItem.userId}/posts/${postModelItem.id}/${System.currentTimeMillis()}.jpg")

            val metaData = storageMetadata {
                contentType = "image/jpeg"
            }

            imageRef.putFile(postImageUrl.toUri(), metaData)
                .await().storage.downloadUrl.await().toString()

        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}