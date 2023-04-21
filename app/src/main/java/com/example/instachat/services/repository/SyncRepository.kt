package com.example.instachat.services.repository

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.Comment
import com.example.instachat.services.models.dummyjson.InterestedUsersModel
import com.example.instachat.services.models.dummyjson.RequestedForInterestModel
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.room_sync.SyncTables
import com.example.instachat.services.workManager.SyncWorker
import com.example.instachat.utils.ObjectConverterUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

class SyncRepository @Inject constructor(
    val roomSyncRepository: RoomSyncRepository,
    val roomDataSource: RoomDataSource,
    val apiRepository: RestApiRepository,
    @ApplicationContext val context: Context
) {

    fun launchWorker(
        syncTables: SyncTables,
        itemId: Int = 0,
        userId: String = "",
        commentId: Int = 0,
        itemIdString: String = "",
        addNewPostWorkId: ((UUID) -> Unit)? = null,
        followingStatusWorkId: ((UUID) -> Unit)? = null,
        updateUserWorkId: ((UUID) -> Unit)? = null
    ) {

        val data = Data.Builder()
        data.putInt("ID", itemId)
        data.putString("SYNC_TABLE", syncTables.name)
        data.putString("USER_ID", userId)
        data.putString("ITEM_ID_STRING", itemIdString)
        data.putInt("COMMENT_ID", commentId)

        val tag: String = when {
            syncTables.name.equals(SyncTables.POSTS.name) -> {
                "itemId"
            }
            syncTables.name.equals(SyncTables.COMMENTS.name) -> {
                userId
            }
            syncTables.name.equals(SyncTables.USERS.name) -> {
                "commentId"
            }
            syncTables.name.equals(SyncTables.ADD_INTERESTED_LIST.name) ->{
                "ADD_INTERESTED_LIST"
            }
            syncTables.name.equals(SyncTables.ADD_REQUEST_INTERESTED_LIST.name) ->{
                "ADD_REQUEST_INTERESTED_LIST"
            }
            syncTables.name.equals(SyncTables.NEW_POST.name) ->{
                "NEW_POST"
            }
            else ->""
        }

        val workRequest =
            OneTimeWorkRequest.Builder(SyncWorker::class.java)
                .setInputData(data.build())
                .addTag(tag).build()

        when {
            syncTables.name.equals(SyncTables.POSTS.name) -> {
                addNewPostWorkId?.invoke(workRequest.id)
            }
            syncTables.name.equals(SyncTables.NEW_POST.name) -> {
                addNewPostWorkId?.invoke(workRequest.id)
            }
            syncTables.name.equals(SyncTables.COMMENTS.name) -> {

            }
            syncTables.name.equals(SyncTables.USERS.name) -> {
                updateUserWorkId?.invoke(workRequest.id)
            }
            syncTables.name.equals(SyncTables.USERS_UPDATE_FOLLOWING.name) -> {
                followingStatusWorkId?.invoke(workRequest.id)
            }

            syncTables.name.equals(SyncTables.ADD_REQUEST_INTERESTED_LIST.name) -> {
                followingStatusWorkId?.invoke(workRequest.id)
            }

            else -> ""
        }

        WorkManager.getInstance(context).enqueue(workRequest)
    }

    suspend fun updateLikeForPost(post: PostModelItem) {
        roomSyncRepository.postsDao.insert(
            ObjectConverterUtil.convertPostToPostSync(post)
        )
        launchWorker(SyncTables.POSTS, post.id)
    }

    suspend fun updateUser(user: User, workId: ((UUID)-> Unit)? = null) {
        roomSyncRepository.usersDao.insert(
            ObjectConverterUtil.convertUserToUserSync(user)
        )
        launchWorker(SyncTables.USERS, userId = user.id, updateUserWorkId = workId)
    }

    suspend fun addAndSyncUserInterestedList(interestedUsersModel: InterestedUsersModel) {
        roomSyncRepository.interestedUsersDaoSync.insert(
            ObjectConverterUtil.convertInterestedUsersToInterestedUsersSync(
                interestedUsersModel
            )
        )
        launchWorker(SyncTables.ADD_INTERESTED_LIST, itemIdString = interestedUsersModel.id)
    }

    suspend fun addAndSyncRequestedInterestsList(
        requestedForInterestModel: RequestedForInterestModel,
        onFollowRequested: ((UUID) -> Unit)? = null
    ) {
        roomSyncRepository.requestedInterestedUsersDaoSync.insert(
            ObjectConverterUtil.convertRequestedInterestedModelToRequestedInterestedModelSync(
                requestedForInterestModel
            )
        )
        launchWorker(SyncTables.ADD_REQUEST_INTERESTED_LIST, itemIdString = requestedForInterestModel.id, followingStatusWorkId = onFollowRequested)
    }

    suspend fun removeAndSyncUserInterestedList(interestedUsersModel: InterestedUsersModel) {
        roomSyncRepository.interestedUsersDaoSync.insert(
            ObjectConverterUtil.convertInterestedUsersToInterestedUsersSync(
                interestedUsersModel
            )
        )
        launchWorker(SyncTables.REMOVE_INTERESTED_LIST_ITEM, itemIdString = interestedUsersModel.id)
    }

    suspend fun removeAndSyncRequestedInterestsList(
        requestedForInterestModel: RequestedForInterestModel,
        onFollowRequested: ((UUID) -> Unit)? = null
    ) {
        roomSyncRepository.requestedInterestedUsersDaoSync.insert(
            ObjectConverterUtil.convertRequestedInterestedModelToRequestedInterestedModelSync(
                requestedForInterestModel
            )
        )
        launchWorker(SyncTables.REMOVE_REQUEST_INTERESTED_LIST_ITEM, itemIdString = requestedForInterestModel.id, followingStatusWorkId = onFollowRequested)
    }

    suspend fun updateFollowingStatus(user: User, onFollowingStatusUpdated: ((UUID) -> Unit)) {
        roomSyncRepository.usersDao.insert(
            ObjectConverterUtil.convertUserToUserSync(user)
        )
        launchWorker(
            SyncTables.USERS_UPDATE_FOLLOWING,
            userId = user.id,
            followingStatusWorkId = onFollowingStatusUpdated
        )
    }

    suspend fun addNewUser(user: User) {
        roomSyncRepository.usersDao.insert(
            ObjectConverterUtil.convertUserToUserSync(user)
        )
        launchWorker(SyncTables.USERS, userId = user.id)
    }

    suspend fun addNewComment(comment: Comment) {
        roomSyncRepository.commentsDao.insert(
            ObjectConverterUtil.convertCommentToCommentSync(comment)
        )
        launchWorker(SyncTables.COMMENTS, commentId = comment.id)
    }

    suspend fun addNewPost(postModelItem: PostModelItem, newPostWorkId: (UUID) -> Unit) {
        roomSyncRepository.postsDao.insert(
            ObjectConverterUtil.convertPostToPostSync(postModelItem)
        )
        launchWorker(SyncTables.NEW_POST, postModelItem.id, addNewPostWorkId = newPostWorkId)
    }

    suspend fun updateUsersInterestedUsers(user: User){
        roomSyncRepository.usersDao.insert(
            ObjectConverterUtil.convertUserToUserSync(user)
        )
        launchWorker(
            SyncTables.USERS_UPDATE_INTERESTED_USERS,
            userId = user.id,
        )
    }

    suspend fun updateRequestedInterestedUsers(user: User){
        roomSyncRepository.usersDao.insert(
            ObjectConverterUtil.convertUserToUserSync(user)
        )
        launchWorker(
            SyncTables.USERS_UPDATE_REQUESTED_INTERESTED_USERS,
            userId = user.id,
        )
    }

}