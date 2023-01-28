package com.example.instachat.services.models

import com.google.gson.annotations.SerializedName

data class PostModel(
    val limit: Int,
    val posts: List<PostModelItem>,
    val skip: Int,
    val total: Int
)

data class PostModelItem(
    @SerializedName("body")
    val body: String,

    @SerializedName("id")
    val id: Int,

    @SerializedName("reactions")
    val reactions: Int,

    @SerializedName("tags")
    val tags: List<String>,

    @SerializedName("title")
    val title: String,

    @SerializedName("userId")
    val userId: Int,

    var postImageUrl: String = ""
)