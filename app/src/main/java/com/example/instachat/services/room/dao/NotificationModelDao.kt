package com.example.instachat.services.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.instachat.services.models.rest.NotificationModel
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationModelDao : BaseDao<NotificationModel>{

    @Query("SELECT * FROM notifications WHERE targetUserId =:userId")
    fun getNotificationsForUserId(userId: String): Flow<List<NotificationModel>?>

    @Query("DELETE FROM notifications")
    fun deleteNotificationsTable()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(items: List<NotificationModel>): List<Long>

}