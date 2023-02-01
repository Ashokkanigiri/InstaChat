package com.example.instachat.services.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.instachat.services.models.PostModelItem

@Dao
interface PostsDao : BaseDao<PostModelItem> {

    @Query("SELECT * FROM posts")
   suspend fun getAllPosts(): List<PostModelItem>

   @Query("SELECT * FROM posts WHERE id =:postId")
   suspend fun getPost(postId: Int): PostModelItem

}