package com.example.instachat.services.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.example.instachat.feature.hometab.models.HomeDataModel
import com.example.instachat.feature.userdetails.UserDetailPostsModel
import com.example.instachat.services.models.PostModelItem
import kotlinx.coroutines.flow.Flow

@Dao
interface PostsDao : BaseDao<PostModelItem> {

    @Query("SELECT * FROM posts")
    fun getAllPosts(): LiveData<List<PostModelItem>>

    @Query("SELECT * FROM posts")
    fun getAllPostsFlow(): Flow<List<PostModelItem>>

    @Query("SELECT * FROM posts WHERE id =:postId")
    suspend fun getPost(postId: Int): PostModelItem

    @Query("DELETE FROM posts")
    suspend fun deletePostsTable()

    @Query("SELECT * FROM posts where userId =:userId")
    fun getPostsForUser(userId: String): LiveData<List<PostModelItem>>

    @Update
    fun updatePost(postModelItem: PostModelItem)

    @Query("SELECT posts.id AS postId, posts.title AS postTitle, posts.body AS postBody, posts.postImageUrls as postImageUrls, posts.likesCount as postLikesCount, users.id AS userId, users.username AS userName, users.image as userImageUrl, users.firstName as firstName, users.lastName as lastName, users.likedPosts as likedPosts  FROM posts INNER JOIN users ON posts.userId  = users.id  WHERE users.id =:userId")
    fun getPostsHomeData(userId: String): Flow<List<HomeDataModel>>

    @Query("SELECT posts.id AS postId, posts.title AS postTitle, posts.body AS postBody, posts.postImageUrls as postImageUrls, posts.likesCount as postLikesCount, users.id AS userId, users.username AS userName, users.image as userImageUrl, users.firstName as firstName, users.lastName as lastName, users.likedPosts as likedPosts  FROM posts INNER JOIN users ON posts.userId  = users.id  WHERE users.id =:userId")
    fun getPostsHomeDataLive(userId: String): LiveData<List<HomeDataModel>>

    @Query("UPDATE posts SET likesCount =:likesCount WHERE id =:postId")
    fun updateLikedCountForPost(postId: Int, likesCount: Int)

    @Query("SELECT posts.id AS postId, posts.title AS postTitle, posts.body AS postBody, posts.postImageUrls as postImageUrls, posts.likesCount as postLikesCount, users.id AS userId, users.username AS userName, users.image as userImageUrl, users.firstName as firstName, users.lastName as lastName, users.likedPosts as likedPosts  FROM posts LEFT JOIN users ON posts.userId  = users.id")
    fun getAllPostsHomeData(): LiveData<List<HomeDataModel>>

    @Query("SELECT posts.id AS postId, posts.title AS postTitle, posts.body AS postBody, posts.postImageUrls as postImageUrls, posts.likesCount as postLikesCount, users.id AS userId, users.username AS userName, users.image as userImageUrl, users.firstName as firstName, users.lastName as lastName, users.likedPosts as likedPosts  FROM posts LEFT JOIN users ON posts.userId  = users.id")
    fun getAllPostsHomeDataFlow(): Flow<List<HomeDataModel>>

    @Query("SELECT posts.id AS postId, posts.title AS postTitle, posts.body AS postBody, posts.postImageUrls as postImageUrls, posts.likesCount as postLikesCount, users.id AS userId, users.username AS userName, users.image as userImageUrl, users.firstName as firstName, users.lastName as lastName, users.likedPosts as likedPosts  FROM posts INNER JOIN users ON posts.userId  = users.id  WHERE users.id =:userId ORDER BY postCreatedDate DESC")
    fun getPostsHomeDataFlow(userId: String): Flow<List<HomeDataModel>>

    @Query("SELECT posts.id as postId, posts.postImageUrls as postImageUrls from posts INNER JOIN users ON posts.userId == users.id WHERE users.id =:userId")
    fun getPostsForUserDetails(userId: String): Flow<List<UserDetailPostsModel>>
}