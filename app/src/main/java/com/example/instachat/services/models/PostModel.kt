package com.example.instachat.services.models

import androidx.room.*
import com.example.instachat.services.room.typeconverters.TagsTypeConverter
import com.google.gson.annotations.SerializedName

data class PostModel(
    val limit: Int,
    val posts: List<PostModelItem>,
    val skip: Int,
    val total: Int
)

@Entity("posts")
data class PostModelItem(
    @SerializedName("body")
    val body: String,

    @PrimaryKey
    @SerializedName("id")
    val id: Int,

    @SerializedName("reactions")
    val reactions: Int,

    @SerializedName("tags")
    @TypeConverters(TagsTypeConverter::class)
    val tags: List<String>,

    @SerializedName("title")
    val title: String,

    @SerializedName("userId")
    val userId: Int,

    var postImageUrl: String = ""
)