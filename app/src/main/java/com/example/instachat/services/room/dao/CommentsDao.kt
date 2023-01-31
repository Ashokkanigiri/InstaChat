package com.example.instachat.services.room.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.instachat.services.models.dummyjson.Comment

@Dao
interface CommentsDao {

    @Insert
    suspend fun insertComments(comments: List<Comment>)
}