package com.example.instachat.feature.home.models

data class HomeDataCommentsModel(
    val commentId: Int?,
    val commentBody: String?,
    val commentedUserName: String?,
    val postId: Int?,
    val totalCommentsForPost: Int?
)