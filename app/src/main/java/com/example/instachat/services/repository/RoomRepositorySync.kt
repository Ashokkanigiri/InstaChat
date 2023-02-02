package com.example.instachat.services.repository

import com.example.instachat.services.room_sync.daoSync.CommentsDaoSync
import com.example.instachat.services.room_sync.daoSync.PostsDaoSync
import com.example.instachat.services.room_sync.daoSync.UsersDaoSync
import javax.inject.Inject

class RoomRepositorySync @Inject constructor(
    val usersDao: UsersDaoSync,
    val postsDao: PostsDaoSync,
    val commentsDao: CommentsDaoSync
) {

}