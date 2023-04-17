package com.example.instachat.services.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.instachat.services.models.rest.NotificationModel
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationModelDao : BaseDao<NotificationModel>{

    @Query("SELECT * FROM notifications WHERE triggeredUserId =:userId")
    fun getNotificationsForUserId(userId: String): Flow<List<NotificationModel>>

    @Query("DELETE FROM notifications")
    fun deleteCommentsTable()

}