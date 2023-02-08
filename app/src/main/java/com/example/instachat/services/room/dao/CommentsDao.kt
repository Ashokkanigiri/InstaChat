package com.example.instachat.services.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.instachat.feature.hometab.models.HomeDataCommentsModel
import com.example.instachat.services.models.dummyjson.Comment
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentsDao : BaseDao<Comment> {

    @Query("SELECT * FROM comments")
    fun getAllCommments(): LiveData<List<Comment>>

    @Query("SELECT * FROM comments WHERE id =:commentId")
    suspend fun getComment(commentId: Int): Comment

    @Query("SELECT * FROM comments WHERE postId =:postId")
    fun getAllCommentsForPost(postId: Int): LiveData<List<Comment>>

    @Query("SELECT * FROM comments WHERE postId =:postId LIMIT 1")
    suspend fun getFirstCommentForPost(postId: Int): Comment

    @Query("SELECT COUNT(*) FROM comments WHERE comments.postId =:postId")
    fun getTotalCommentsCount(postId: Int): Flow<Int>

    @Query("SELECT * FROM comments WHERE postId =:postId")
    fun getCommentsForUser(postId: Int): Flow<List<Comment>>

    @Query("SELECT * FROM comments WHERE postId =:postId")
    fun getAllCommentsForPostLive(postId: Int): LiveData<List<Comment>>

    @Query("SELECT comments.id as commentId, comments.body as commentBody, comments.username as commentedUserName, posts.id as postId, COUNT(comments.id) as totalCommentsForPost FROM posts LEFT JOIN comments ON posts.id = comments.postId WHERE posts.id =:postId")
    suspend fun getAllCommentsForPostLiveData(postId: Int): HomeDataCommentsModel
}