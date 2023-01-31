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
    val body: String,
    @PrimaryKey
    val id: Int,
    val postId: Int,
    @Embedded
    val user: CommentUser
)

data class CommentUser(
    @ColumnInfo("commentedUsersId")
    val id: Int,
    val username: String
)