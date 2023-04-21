package com.example.instachat.services.models.dummyjson

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

data class CommentsModel(
    val comments: List<Comment>,
    val limit: Int,
    val skip: Int,
    val total: Int
)

@Entity("comments")
data class Comment(
    val body: String = "",
    @PrimaryKey
    val id: Int = 0,
    val postId: Int = 0,
    @Embedded
    val user: CommentUser = CommentUser(),

    val commentCreatedDate: String? = ""
)

data class CommentUser(
    @ColumnInfo("commentedUsersId")
    val id: String = "",
    val username: String = "",
    val userImageUrl: String = ""
)