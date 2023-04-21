package com.example.instachat.feature.comment

import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.Comment
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.models.dummyjson.UsersModel
import com.example.instachat.utils.Response
import kotlinx.coroutines.flow.Flow

interface CommentsRepository {

    suspend fun loadCurrentLoggedUser(userId: String): Response<User>

    suspend fun loadPostedUser(userId: String):  Response<User>

    suspend fun loadAllCommentsForPost(postId: Int): Flow<List<Comment>>

    suspend fun loadCurrentPost(postId: Int):  Response<PostModelItem>

    suspend fun postComment(postModelItem: PostModelItem) : Response<Boolean>
}