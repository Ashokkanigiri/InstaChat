package com.example.instachat.feature.home

import androidx.room.Embedded
import androidx.room.Ignore
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.Comment
import com.example.instachat.services.models.dummyjson.LikedPosts
import com.example.instachat.services.models.dummyjson.User

data class HomeDataModel(
    var postId: Int=0,
    val postTitle: String="",
    val postBody: String="",
    val postImageUrl: String="",
    val postLikesCount: Int=0,

    val userId: String="",
    val userName: String="",
    val userImageUrl: String="",
    val firstName: String="",
    val lastName: String="",
    val likedPosts: List<LikedPosts> = emptyList()
)