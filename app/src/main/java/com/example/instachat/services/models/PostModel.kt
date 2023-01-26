package com.example.instachat.services.models

import com.google.gson.annotations.SerializedName

class PostModel : ArrayList<PostModelItem>()

data class PostModelItem(
    @SerializedName("body")
    val body: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("userId")
    val userId: Int,

    var postImageUrl: String = ""
)