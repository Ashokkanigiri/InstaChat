package com.example.instachat.services.repository

import com.example.instachat.services.room_sync.dao.CommentsDaoSync
import com.example.instachat.services.room_sync.dao.PostsDaoSync
import com.example.instachat.services.room_sync.dao.UsersDaoSync
import javax.inject.Inject

class RoomSyncRepository @Inject constructor(
    val usersDao: UsersDaoSync,
    val postsDao: PostsDaoSync,
    val commentsDao: CommentsDaoSync
) {

}