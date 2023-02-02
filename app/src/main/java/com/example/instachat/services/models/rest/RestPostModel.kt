package com.example.instachat.services.models.rest

data class PostModelRest(
    val limit: Int,
    val posts: List<PostModelItemRest>,
    val skip: Int,
    val total: Int
)

data class PostModelItemRest(
    val body: String,
    val id: Int,
    val reactions: Int,
    val tags: List<String>,
    val title: String,
    val userId: Int,
    var postImageUrl: String = ""
)