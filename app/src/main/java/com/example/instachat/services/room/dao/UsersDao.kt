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

    @Query("SELECT * FROM users WHERE id =:userId ")
    fun getUserFlow(userId: String): Flow<User>

    @Update
    fun updateUser(user: User)

    @Query("DELETE FROM users")
    suspend fun deleteUserTable()

    @Query("UPDATE users SET likedPosts =:likedPosts WHERE id =:userId")
    fun updateUserLikedPosts(likedPosts: List<LikedPosts>, userId: String)

    @Query("UPDATE users SET followedUserIds =:followingUserIds WHERE id =:userId")
    fun updateFollowing(followingUserIds: List<String>, userId: String)

    @Query("SELECT * FROM users")
    fun getallUsersFlow(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE users.username LIKE '%' || :match || '%'")
    fun getAllUsersWithMatchingUserNameFlow(match: String): Flow<List<User>>

    @Query("UPDATE users SET interestedUsersList =:list WHERE id =:userId")
    fun updateInterestedUsersList(list: List<String>, userId: String)

    @Query("UPDATE users SET requestedForInterestsList =:list WHERE id =:userId")
    fun updateRequestedInterestedUsersList(list: List<String>, userId: String)

}