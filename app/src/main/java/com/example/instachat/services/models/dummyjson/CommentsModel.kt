package com.example.instachat.services.models.dummyjson

data class CommentsModel(
    val comments: List<Comment>,
    val limit: Int,
    val skip: Int,
    val total: Int
)

data class Comment(
    val body: String,
    val id: Int,
    val postId: Int,
    val user: CommentUser
)

data class CommentUser(
    val id: Int,
    val username: String
)