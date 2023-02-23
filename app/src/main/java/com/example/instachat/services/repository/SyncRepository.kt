package com.example.instachat.services.repository

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.Comment
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.room_sync.SyncTables
import com.example.instachat.services.workManager.SyncWorker
import com.example.instachat.utils.ObjectConverterUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject

class SyncRepository @Inject constructor(
    val roomSyncRepository: RoomSyncRepository,
    val roomRepository: RoomRepository,
    val apiRepository: RestApiRepository,
    @ApplicationContext val context: Context
) {

    fun launchWorker(syncTables: SyncTables, itemId: Int =0, userId: String="", commentId: Int = 0, addNewPostWorkId: ((UUID) -> Unit)? = null) {

        val data = Data.Builder()
        data.putInt("ID", itemId)
        data.putString("SYNC_TABLE", syncTables.name)
        data.putString("USER_ID", userId)
        data.putInt("COMMENT_ID", commentId)

        val tag : String = when {
            syncTables.name.equals(SyncTables.POSTS.name) ->{
                "itemId"
            }
            syncTables.name.equals(SyncTables.COMMENTS.name) ->{
                userId
            }
            syncTables.name.equals(SyncTables.USERS.name) ->{
                "commentId"
            }
            else->""
        }

        val workRequest =
            OneTimeWorkRequest.Builder(SyncWorker::class.java)
                .setInputData(data.build())
                .addTag(tag).build()

        addNewPostWorkId?.invoke(workRequest.id)

        WorkManager.getInstance(context).enqueue(workRequest)
    }

    suspend fun updateLikeForPost(post: PostModelItem) {
        roomSyncRepository.postsDao.insert(
            ObjectConverterUtil.convertPostToPostSync(post)
        )
        launchWorker(SyncTables.POSTS, post.id)
    }

    suspend fun updateUser(user: User) {
        roomSyncRepository.usersDao.insert(
            ObjectConverterUtil.convertUserToUserSync(user)
        )
        launchWorker(SyncTables.USERS, userId = user.id)
    }

    suspend fun addNewComment(comment: Comment){
        roomSyncRepository.commentsDao.insert(
            ObjectConverterUtil.convertCommentToCommentSync(comment)
        )
        launchWorker(SyncTables.COMMENTS, commentId = comment.id)
    }

    suspend fun addNewPost(postModelItem: PostModelItem, newPostWorkId: (UUID) -> Unit){
        roomSyncRepository.postsDao.insert(
            ObjectConverterUtil.convertPostToPostSync(postModelItem)
        )
        launchWorker(SyncTables.NEW_POST, postModelItem.id){ workId ->
            newPostWorkId.invoke(workId)
        }

    }
}