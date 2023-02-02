package com.example.instachat.services.models.rest



data class RestCommentModel(
    val comments: List<RestComment>,
    val limit: Int,
    val skip: Int,
    val total: Int
)

data class RestComment(
    val body: String,
    val id: Int,
    val postId: Int,
    val user: RestCommentUser
)

data class RestCommentUser(
    val id: Int,
    val username: String
)