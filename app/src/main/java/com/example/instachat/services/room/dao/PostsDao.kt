package com.example.instachat.services.room.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.instachat.services.models.PostModelItem

@Dao
interface PostsDao {

    @Insert
    suspend fun insertPosts(posts: List<PostModelItem>)

    @Insert
    suspend fun insertPost(posts: PostModelItem)
}