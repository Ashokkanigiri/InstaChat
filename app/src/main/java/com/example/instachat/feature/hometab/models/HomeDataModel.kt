package com.example.instachat.feature.hometab.models

import com.example.instachat.services.models.dummyjson.LikedPosts

data class HomeDataModel(
    var postId: Int=0,
    val postTitle: String?="",
    val postBody: String?="",
    val postImageUrls: List<String>? = emptyList(),
    val postLikesCount: Int=0,

    val userId: String?="",
    val userName: String?="",
    val userImageUrl: String?="",
    val firstName: String?="",
    val lastName: String?="",
    val likedPosts: List<LikedPosts>? = emptyList()
)