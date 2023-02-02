package com.example.instachat.services.room_sync.daoSync

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.instachat.services.room_sync.modelsSync.PostModelItemSync

@Dao
interface PostsDaoSync : BaseDaoSync<PostModelItemSync> {

   @Query("SELECT * FROM posts_sync")
   fun getAllPosts(): LiveData<List<PostModelItemSync>>

   @Query("SELECT * FROM posts_sync WHERE id =:postId")
   suspend fun getPost(postId: Int): PostModelItemSync

   @Query("SELECT * FROM posts_sync where userId =:userId")
   fun getPostsForUser(userId: String): LiveData<List<PostModelItemSync>>


}