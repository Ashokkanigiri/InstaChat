package com.example.instachat.feature.hometab.models

data class HomeDataCommentsModel(
    val commentId: Int?,
    val commentBody: String?,
    val commentedUserName: String?,
    val postId: Int?,
    val totalCommentsForPost: Int?
)