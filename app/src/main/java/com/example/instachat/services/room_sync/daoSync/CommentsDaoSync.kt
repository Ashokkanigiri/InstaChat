package com.example.instachat.services.room_sync.daoSync

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.instachat.services.room_sync.modelsSync.CommentSync

@Dao
interface CommentsDaoSync : BaseDaoSync<CommentSync> {

    @Query("SELECT * FROM comments_sync")
    fun getAllCommments(): LiveData<List<CommentSync>>

    @Query("SELECT * FROM comments_sync WHERE id =:commentId")
    suspend fun getComment(commentId: Int): CommentSync

    @Query("SELECT * FROM comments_sync WHERE postId =:postId")
    suspend fun getAllCommentsForPost(postId: Int): List<CommentSync>

    @Query("SELECT * FROM comments_sync WHERE postId =:postId LIMIT 1")
    suspend fun getFirstCommentForPost(postId: Int): CommentSync
}