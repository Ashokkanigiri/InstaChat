package com.example.instachat.services.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.instachat.services.models.dummyjson.User

@Dao
interface UsersDao {

    @Query("SELECT * FROM users")
    suspend fun getallUsers(): List<User>

    @Insert
    suspend fun insertUsers(users: List<User>)
}