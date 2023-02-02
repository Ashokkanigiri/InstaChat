package com.example.instachat.services.room_sync.daoSync

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.instachat.services.room_sync.modelsSync.UserSync

@Dao
interface UsersDaoSync : BaseDaoSync<UserSync> {

    @Query("SELECT * FROM users_sync")
    fun getallUsers(): LiveData<List<UserSync>>

    @Query("SELECT * FROM users_sync WHERE id =:userId ")
    suspend fun getUser(userId: String): UserSync


}