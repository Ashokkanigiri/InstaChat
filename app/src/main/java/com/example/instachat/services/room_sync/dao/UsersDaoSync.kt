package com.example.instachat.services.room_sync.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.instachat.services.room_sync.models.UserSync

@Dao
interface UsersDaoSync : BaseDaoSync<UserSync> {

    @Query("SELECT * FROM users_sync")
    fun getallUsers(): LiveData<List<UserSync>>

    @Query("SELECT * FROM users_sync WHERE id =:userId ")
    suspend fun getUser(userId: String): UserSync

    @Query("DELETE FROM users_sync WHERE id =:userId")
    fun deleteUser(userId: String)


}