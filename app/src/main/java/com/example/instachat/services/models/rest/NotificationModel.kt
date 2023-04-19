package com.example.instachat.services.models.rest

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "notifications")
data class NotificationModel(
    @PrimaryKey
    @SerializedName("id")
    val id: String = "",

    @SerializedName("targetUserId")
    val targetUserId: String = "",

    @SerializedName("triggeredUserId")
    val triggeredUserId: String = "",

    @SerializedName("title")
    val title: String = "",

    @SerializedName("body")
    val body: String = "",

    @SerializedName("triggeredUserImageUrl")
    val triggeredUserImageUrl: String = "",

    @SerializedName("timeStamp")
    val timeStamp: String = ""
)
