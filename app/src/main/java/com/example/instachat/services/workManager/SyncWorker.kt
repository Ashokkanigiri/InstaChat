package com.example.instachat.services.workManager

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.ListenableWorker.Result.*
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.repository.RoomRepository
import com.example.instachat.services.repository.RoomRepositorySync
import com.example.instachat.services.room_sync.modelsSync.PostModelItemSync
import com.example.instachat.services.room_sync.modelsSync.UserSync
import com.example.instachat.services.sync.SyncTables
import com.example.instachat.utils.ObjectConverterUtil
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    val roomRepositorySync: RoomRepositorySync,
    val roomRepository: RoomRepository
) : CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result {

        val item_id = inputData.getInt("ID", 0)
        val sync_table = inputData.getString("SYNC_TABLE")
        val user_id = inputData.getString("USER_ID")

        when (sync_table) {
            SyncTables.POSTS.name -> {
                val updatedPost = roomRepositorySync.postsDao.getPost(item_id)
                val convertToPostModelItem = ObjectConverterUtil.convertPostSyncToPost(updatedPost)
                updateToFirebase(item_id, convertToPostModelItem)
            }
            SyncTables.COMMENTS.name -> {

            }
            SyncTables.USERS.name -> {
                val updatedUser = roomRepositorySync.usersDao.getUser(user_id?:"")
                val convertToUser = ObjectConverterUtil.convertUserSyncToUser(updatedUser)
                updateUserToFirebase(user_id?:"", convertToUser)
            }
        }

        return Result.failure()
    }

    private fun updateToFirebase(item_id: Int, updatedPost: PostModelItem) {
        val db = Firebase.firestore
        db.collection("posts")
            .document("${item_id}")
            .set(updatedPost)
            .addOnCompleteListener {
                roomRepository.postsDao.updatePost(updatedPost)
                roomRepositorySync.postsDao.deletePost(item_id)
                success()
            }.addOnCanceledListener {
                ListenableWorker.Result.Retry.retry()
            }.addOnFailureListener {
                retry()
            }
    }

    private fun updateUserToFirebase(user_id: String, user: User) {
        val db = Firebase.firestore
        db.collection("users")
            .document("1")
            .set(user)
            .addOnCompleteListener {
                roomRepository.usersDao.updateUser(user)
                roomRepositorySync.usersDao.deleteUser(user_id)
                success()
            }.addOnCanceledListener {
                ListenableWorker.Result.Retry.retry()
            }.addOnFailureListener {
                retry()
            }
    }
}