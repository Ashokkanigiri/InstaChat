package com.example.instachat.services.firebase

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.Comment
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.models.dummyjson.UserRest
import com.example.instachat.services.repository.RestApiRepository
import com.example.instachat.services.repository.RoomRepository
import com.example.instachat.services.repository.RoomRepositorySync
import com.example.instachat.services.room_sync.modelsSync.PostModelItemSync
import com.example.instachat.utils.ObjectConverterUtil
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
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseRepository @Inject constructor(
    @ApplicationContext val context: Context,
    val roomRepository: RoomRepository,
    val roomRepositorySync: RoomRepositorySync
) {

    fun injectCommentsToFirebase(data: Comment) {
        val db = Firebase.firestore
        db.collection("comments")
            .document(data.id.toString())
            .set(data)
            .addOnCompleteListener {
                Log.d("wkjbfqa", "OnComplete")
            }.addOnCanceledListener {
                Log.d("wkjbfqa", "addOnCanceledListener")
            }
    }


    fun injectUsersToFirebase(data: User) {
        val db = Firebase.firestore
        db.collection("users")
            .document(data.id.toString())
            .set(data)
            .addOnCompleteListener {
                Log.d("wkjbfqa", "OnComplete")
            }.addOnCanceledListener {
                Log.d("wkjbfqa", "addOnCanceledListener")
            }
    }

    fun injectPostsToFirebase(data: PostModelItem) {
        val db = Firebase.firestore
        db.collection("posts")
            .document(data.id.toString())
            .set(data)
            .addOnCompleteListener {
                Log.d("wkjbfqa", "OnComplete")
            }.addOnCanceledListener {
                Log.d("wkjbfqa", "addOnCanceledListener")
            }
    }

    suspend fun getAllPostsFromFirebase() {
        val db = Firebase.firestore
        db.collection("posts").get().await().documents.let {it->
            val data = it?.map { it.data }?.map {data->
                Gson().fromJson(Gson().toJson(data), PostModelItem::class.java)
            }
            data?.let {
                roomRepository.postsDao.insert(it)
            }
        }
    }

    suspend fun getAllCommentsFromFirebase() {
        val db = Firebase.firestore
        db.collection("comments").get().await().documents.let {it->
            val data = it?.map { it.data }?.map {data->
                Gson().fromJson(Gson().toJson(data), Comment::class.java)
            }
            data?.let {
                roomRepository.commentsDao.insert(it)
            }
        }
    }

    suspend fun getAllUsersFromFirebase() {
        val db = Firebase.firestore
        db.collection("users").get().await().documents.let {it->
            val data = it?.map { it.data }?.map {data->
                Gson().fromJson(Gson().toJson(data), User::class.java)
            }
            data?.let {
                roomRepository.usersDao.insert(it)
            }
        }
    }

    fun getUserPosts(userId: Int) {
        val db = Firebase.firestore

        db.collection("posts").whereLessThanOrEqualTo("userId", userId)
            .addSnapshotListener { value, error ->

                val dd = value?.documents?.map { it.data }?.map { it ->
                    Gson().fromJson(Gson().toJson(it), PostModelItem::class.java)
                }
                dd?.let {
                }

            }
    }

    fun getAllCommentsForPost(postId: Int) {
        val db = Firebase.firestore

        db.collection("comments").whereEqualTo("postId", postId)
            .addSnapshotListener { value, error ->

                val commentsList = value?.documents?.map { it.data }?.map {
                    Gson().fromJson(Gson().toJson(it), Comment::class.java)
                }

                commentsList?.let {
                }
            }
    }

    fun getPost(postId: Int) {
        val db = Firebase.firestore

        db.collection("posts").whereEqualTo("id", postId).addSnapshotListener { value, error ->

            val post = value?.documents?.map { it.data }?.map {
                Gson().fromJson(Gson().toJson(it), PostModelItem::class.java)
            }

            post?.let { post ->
                post.isNotEmpty()?.let {
                }
            }
        }
    }

    fun getUser(userId: Int, getLoggedInUser: Boolean) {
        val db = Firebase.firestore

        db.collection("users").whereEqualTo("id", userId).addSnapshotListener { value, error ->

            val user = value?.documents?.map { it.data }?.map {
                Gson().fromJson(Gson().toJson(it), User::class.java)
            }

            user?.let { user ->
                user.isNotEmpty()?.let {
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