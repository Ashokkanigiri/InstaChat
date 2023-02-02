package com.example.instachat.services.room_sync.modelsSync

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("comments_sync")
data class CommentSync(
    val body: String,
    @PrimaryKey
    val id: Int,
    val postId: Int,
    @Embedded
    val user: CommentUserSync
)

data class CommentUserSync(
    @ColumnInfo("commentedUsersId")
    val id: String,
    val username: String
)