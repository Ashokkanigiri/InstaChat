package com.example.instachat.feature.home

import androidx.room.Embedded
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.Comment
import com.example.instachat.services.models.dummyjson.User

data class HomeDataModel(
    val postId: Int,
    val postTitle: String,
    val postBody: String,
    val postImageUrl: String,
    val postLikesCount: Int,

    val userId: String,
    val userName: String,
    val userImageUrl: String,
    val firstName: String,
    val lastName: String,

    val commentBody: String
)