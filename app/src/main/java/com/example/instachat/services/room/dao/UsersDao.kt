package com.example.instachat.services.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.example.instachat.services.models.dummyjson.LikedPosts
import com.example.instachat.services.models.dummyjson.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDao: BaseDao<User> {

    @Query("SELECT * FROM users")
    fun getallUsers(): LiveData<List<User>>

    @Query("SELECT * FROM users WHERE id =:userId ")
    suspend fun getUser(userId: String): User

    @Update
    fun updateUser(user: User)

    @Query("UPDATE users SET likedPosts =:likedPosts WHERE id =:userId")
    fun updateUserLikedPosts(likedPosts: List<LikedPosts>, userId: String)

    @Query("SELECT * FROM users")
    fun getallUsersFlow(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE users.username LIKE '%' || :match || '%'")
    fun getAllUsersWithMatchingUserNameFlow(match: String): Flow<List<User>>


}