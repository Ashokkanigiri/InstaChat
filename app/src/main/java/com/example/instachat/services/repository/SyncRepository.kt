package com.example.instachat.services.repository

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.room_sync.modelsSync.PostModelItemSync
import com.example.instachat.services.room_sync.modelsSync.UserSync
import com.example.instachat.services.sync.SyncTables
import com.example.instachat.services.workManager.SyncWorker
import com.example.instachat.utils.ObjectConverterUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.http.POST
import javax.inject.Inject

class SyncRepository @Inject constructor(
    val roomRepositorySync: RoomRepositorySync,
    val roomRepository: RoomRepository,
    val apiRepository: RestApiRepository,
    @ApplicationContext val context: Context
) {

    fun launchWorker(syncTables: SyncTables, itemId: Int =0, userId: String="") {

        val data = Data.Builder()
        data.putInt("ID", itemId)
        data.putString("SYNC_TABLE", syncTables.name)
        data.putString("USER_ID", userId)

        val tag = if(itemId == 0) userId else "${itemId}"

        val workRequest =
            OneTimeWorkRequest.Builder(SyncWorker::class.java)
                .setInputData(data.build())
                .addTag(tag).build()


        WorkManager.getInstance(context).enqueue(workRequest)
    }

    suspend fun updateLikeForPost(post: PostModelItem) {
        roomRepositorySync.postsDao.insert(
            ObjectConverterUtil.convertPostToPostSync(post)
        )
        launchWorker(SyncTables.POSTS, post.id)
    }

    suspend fun updateUser(user: User) {
        roomRepositorySync.usersDao.insert(
            ObjectConverterUtil.convertUserToUserSync(user)
        )
        launchWorker(SyncTables.USERS, userId = user.id)
    }
}