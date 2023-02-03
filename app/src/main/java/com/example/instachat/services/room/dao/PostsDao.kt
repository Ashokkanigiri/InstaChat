package com.example.instachat.services.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.example.instachat.feature.home.HomeDataModel
import com.example.instachat.services.models.PostModelItem
import kotlinx.coroutines.flow.Flow

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

    @Query("SELECT posts.id AS postId, posts.title AS postTitle, posts.body AS postBody, posts.postImageUrl as postImageUrl, posts.likesCount as postLikesCount, users.id AS userId, users.username AS userName, users.image as userImageUrl, users.firstName as firstName, users.lastName as lastName, comments.body as commentBody FROM users INNER JOIN posts ON users.id  = posts.userId INNER JOIN comments ON users.id = comments.commentedUsersId WHERE users.id =:userId")
    fun getPostsHomeData(userId: String): Flow<List<HomeDataModel>>

}