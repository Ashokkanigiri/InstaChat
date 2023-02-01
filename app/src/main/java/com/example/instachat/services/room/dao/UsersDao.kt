package com.example.instachat.services.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.instachat.services.models.dummyjson.User

@Dao
interface UsersDao: BaseDao<User> {

    @Query("SELECT * FROM users")
    suspend fun getallUsers(): List<User>

    @Query("SELECT * FROM users WHERE id =:userId ")
    suspend fun getUser(userId: Int): List<User>

}