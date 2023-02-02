package com.example.instachat.services.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.example.instachat.services.models.PostModelItem

@Dao
interface PostsDao : BaseDao<PostModelItem> {

   @Query("SELECT * FROM posts")
   fun getAllPosts(): LiveData<List<PostModelItem>>

   @Query("SELECT * FROM posts WHERE id =:postId")
   suspend fun getPost(postId: Int): PostModelItem

   @Query("SELECT * FROM posts where userId =:userId")
   fun getPostsForUser(userId: String): LiveData<List<PostModelItem>>

   @Update
   fun updatePost(postModelItem: PostModelItem)


}