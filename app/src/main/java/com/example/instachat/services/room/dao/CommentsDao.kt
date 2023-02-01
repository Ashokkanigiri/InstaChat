package com.example.instachat.services.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.Comment

@Dao
interface CommentsDao : BaseDao<Comment> {

    @Query("SELECT * FROM comments")
    fun getAllCommments(): LiveData<List<Comment>>

    @Query("SELECT * FROM comments WHERE id =:commentId")
    suspend fun getComment(commentId: Int): Comment

    @Query("SELECT * FROM comments WHERE postId =:postId")
    suspend fun getAllCommentsForPost(postId: Int): List<Comment>

    @Query("SELECT * FROM comments WHERE postId =:postId LIMIT 1")
    suspend fun getFirstCommentForPost(postId: Int): Comment
}