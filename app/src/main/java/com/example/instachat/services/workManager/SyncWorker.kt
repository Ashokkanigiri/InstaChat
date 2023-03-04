package com.example.instachat.services.workManager

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.Comment
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.repository.FirebaseRepository
import com.example.instachat.services.repository.RoomRepository
import com.example.instachat.services.repository.RoomSyncRepository
import com.example.instachat.services.room_sync.SyncTables
import com.example.instachat.utils.ObjectConverterUtil
import com.example.instachat.utils.Response
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    val roomSyncRepository: RoomSyncRepository,
    val roomRepository: RoomRepository,
    val firebaseRepository: FirebaseRepository
) : CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result {

        val item_id = inputData.getInt("ID", 0)
        val sync_table = inputData.getString("SYNC_TABLE")
        val user_id = inputData.getString("USER_ID")
        val commentId = inputData.getInt("COMMENT_ID", 0)

        when (sync_table) {
            SyncTables.POSTS.name -> {
                val updatedPost = roomSyncRepository.postsDao.getPost(item_id)
                val convertToPostModelItem = ObjectConverterUtil.convertPostSyncToPost(updatedPost)
                updateToFirebase(item_id, convertToPostModelItem)
            }
            SyncTables.COMMENTS.name -> {
                val newComment = roomSyncRepository.commentsDao.getComment(commentId)
                val convertToComment = ObjectConverterUtil.convertCommentSyncToComment(newComment)
                addNewCommentToFirebase(convertToComment)
            }
            SyncTables.USERS.name -> {
                val updatedUser = roomSyncRepository.usersDao.getUser(user_id ?: "")
                val convertToUser = ObjectConverterUtil.convertUserSyncToUser(updatedUser)
                updateUserToFirebase(user_id ?: "", convertToUser)
            }
            SyncTables.NEW_POST.name -> {
                val updatedPost = roomSyncRepository.postsDao.getPost(item_id)
                val convertToPostModelItem = ObjectConverterUtil.convertPostSyncToPost(updatedPost)
                uploadPostedImagesToFirebaseAndGetUrl(convertToPostModelItem)
            }
            SyncTables.NEW_USER.name -> {
                val updatedUser = roomSyncRepository.usersDao.getUser(user_id ?: "")
                val convertToUser = ObjectConverterUtil.convertUserSyncToUser(updatedUser)
                addNewUserToFirebase(convertToUser)
            }
            SyncTables.USERS_UPDATE_FOLLOWING.name ->{
                val updatedUser = roomSyncRepository.usersDao.getUser(user_id ?: "")
                val convertToUser = ObjectConverterUtil.convertUserSyncToUser(updatedUser)
                updateFollowingStatusUser(user_id ?: "", convertToUser)
            }
        }
        Log.d("wlfkqwnbgf", "MAIN CLASS sucess")

        return Result.success()
    }

    /**
     * This method uploads postImageUrl into firebase storage
     *
     * Then gets the downloadable url from firebase and later sync with room & Firestore
     */
    private suspend fun uploadPostedImagesToFirebaseAndGetUrl(
        convertToPostModelItem: PostModelItem
    ) {

        when (val uploadedImageUrls = firebaseRepository.uploadPostImageToFirebase(convertToPostModelItem)) {
            is Response.Success<List<String>> -> {
                convertToPostModelItem.apply {
                    this.postImageUrls = uploadedImageUrls.data
                }
                addNewPost(convertToPostModelItem)
            }
            is Response.Failure -> {
                Result.failure()
            }
            Response.Loading -> {
            }
        }

    }

    private fun addNewCommentToFirebase(comment: Comment) {
        val db = Firebase.firestore
        db.collection("comments")
            .document("${comment.id}")
            .set(comment)
            .addOnCompleteListener {
                roomSyncRepository.commentsDao.deleteComment(comment.id)
                roomRepository.commentsDao.insertBlocking(comment)
                Result.success()
            }.addOnCanceledListener {
                ListenableWorker.Result.Retry.retry()
            }.addOnFailureListener {
                Result.failure()
            }.addOnSuccessListener {
                Result.success()
            }
    }

    private fun addNewPost(newPost: PostModelItem) {
        val db = Firebase.firestore
        db.collection("posts")
            .document("${newPost.id}")
            .set(newPost)
            .addOnCompleteListener {
                roomSyncRepository.postsDao.deletePost(newPost.id)
                roomRepository.postsDao.insert(newPost)
                Log.d("jnqwljngfq", "DD: ${Gson().toJson(newPost)}")
            }.addOnCanceledListener {
                ListenableWorker.Result.Retry.retry()
            }.addOnFailureListener {
                Result.failure()
            }.addOnSuccessListener {
                Result.success()
            }
    }

    private fun updateToFirebase(item_id: Int, updatedPost: PostModelItem) {
        val db = Firebase.firestore
        db.collection("posts")
            .document("${item_id}")
            .set(updatedPost)
            .addOnCompleteListener {
                roomRepository.postsDao.updateLikedCountForPost(
                    updatedPost.id,
                    updatedPost.likesCount ?: 0
                )
                roomSyncRepository.postsDao.deletePost(item_id)
                Log.d("wlfkqwnbgf", "update post addOnCompleteListener")
            }.addOnCanceledListener {
                ListenableWorker.Result.Retry.retry()
            }.addOnFailureListener {
                Result.failure()
            }.addOnSuccessListener {
                Result.success()
                Log.d("wlfkqwnbgf", "update post addOnSuccessListener")
            }
    }

    private fun updateFollowingStatusUser(user_id: String, user: User) {
        val db = Firebase.firestore
        db.collection("users")
            .document(user.id)
            .set(user)
            .addOnCompleteListener {
                roomRepository.usersDao.updateFollowing(
                    user.followedUserIds ?: emptyList(),
                    user.id
                )
                roomSyncRepository.usersDao.deleteUser(user_id)
            }.addOnCanceledListener {
                ListenableWorker.Result.Retry.retry()
            }.addOnFailureListener {
                Result.failure()
            }.addOnSuccessListener {
                Result.success()
                Log.d("wlfkqwnbgf", "update user sucess")

            }
    }


    private fun updateUserToFirebase(user_id: String, user: User) {
        val db = Firebase.firestore
        db.collection("users")
            .document(user.id)
            .set(user)
            .addOnCompleteListener {
                roomRepository.usersDao.updateUserLikedPosts(
                    user.likedPosts ?: emptyList(),
                    user.id
                )
                roomSyncRepository.usersDao.deleteUser(user_id)
            }.addOnCanceledListener {
                ListenableWorker.Result.Retry.retry()
            }.addOnFailureListener {
                Result.failure()
            }.addOnSuccessListener {
                Result.success()
                Log.d("wlfkqwnbgf", "update user sucess")

            }
    }

    private fun addNewUserToFirebase(user: User) {
        val db = Firebase.firestore
        db.collection("users")
            .document(user.id)
            .set(user)
            .addOnCompleteListener {
                roomRepository.usersDao.insert(user)
                roomSyncRepository.usersDao.deleteUser(user.id)
            }.addOnCanceledListener {
                ListenableWorker.Result.Retry.retry()
            }.addOnFailureListener {
                Result.failure()
            }.addOnSuccessListener {
                Result.success()
                Log.d("wlfkqwnbgf", "added user sucess")

            }
    }
}