package com.example.instachat.services.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.Comment

@Dao
interface CommentsDao : BaseDao<Comment> {

    @Query("SELECT * FROM comments")
    suspend fun getAllCommments(): List<Comment>

    @Query("SELECT * FROM comments WHERE id =:commentId")
    suspend fun getComment(commentId: Int): Comment
}