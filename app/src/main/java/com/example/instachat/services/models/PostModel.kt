package com.example.instachat.services.models

import androidx.room.*
import com.example.instachat.services.room_sync.typeconverters.TagsTypeConverterSync
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
    var body: String ="",

    @PrimaryKey
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("reactions")
    val reactions: Int = 0,

    @SerializedName("tags")
    @TypeConverters(TagsTypeConverterSync::class)
    var tags: List<String> = emptyList(),

    @SerializedName("title")
    var title: String ="",

    @SerializedName("userId")
    var userId: String = "",

    @TypeConverters(TagsTypeConverterSync::class)
    var postImageUrls: List<String>? = emptyList(),

    @SerializedName("likesCount")
    var likesCount: Int? = 0,

    @SerializedName("postCreatedDate")
    var postCreatedDate: String? = ""
)