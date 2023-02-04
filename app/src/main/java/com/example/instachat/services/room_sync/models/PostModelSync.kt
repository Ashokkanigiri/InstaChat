package com.example.instachat.services.room_sync.models

import androidx.room.*
import com.example.instachat.services.room_sync.typeconverters.TagsTypeConverterSync
import com.google.gson.annotations.SerializedName


@Entity("posts_sync")
data class PostModelItemSync(
    @SerializedName("body")
    val body: String,

    @PrimaryKey
    @SerializedName("id")
    val id: Int,

    @SerializedName("reactions")
    val reactions: Int,

    @SerializedName("tags")
    @TypeConverters(TagsTypeConverterSync::class)
    val tags: List<String>,

    @SerializedName("title")
    val title: String,

    @SerializedName("userId")
    val userId: String,

    var postImageUrl: String = "",

    var likesCount: Int = 0
)