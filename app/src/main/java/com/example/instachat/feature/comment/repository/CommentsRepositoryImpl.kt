package com.example.instachat.feature.comment.repository

import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.repository.RoomDataSource
import com.example.instachat.utils.Response
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CommentsRepositoryImpl @Inject constructor(
    private val roomDataSource: RoomDataSource
) : CommentsRepository {

    override suspend fun loadCurrentLoggedUser(userId: String): Response<User> {
        Response.Loading
        return try {
            Response.Success(roomDataSource.getUserByUserId(userId))
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun loadPostedUser(userId: String): Response<User> {
        Response.Loading
        return try {
            Response.Success(roomDataSource.getUserByUserId(userId))
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun loadAllCommentsForPost(postId: Int) = flow {
        roomDataSource.getAllCommentsForPost(postId).collect { comments ->
            emit(comments.sortedBy { it.commentCreatedDate })
        }
    }

    override suspend fun loadCurrentPost(postId: Int): Response<PostModelItem> {
        Response.Loading
        return try {
            Response.Success(roomDataSource.getPostByPostId(postId))
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun postComment(postModelItem: PostModelItem): Response<Boolean> {
        Response.Loading
        return try {
            val isInserted = roomDataSource.insertPost(postModelItem)
            isInserted?.let {
                Response.Success(true)
            }
            Response.Success(false)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

}