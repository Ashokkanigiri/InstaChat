package com.example.instachat.services.repository

import com.example.instachat.services.room_sync.dao.*
import javax.inject.Inject

class RoomSyncRepository @Inject constructor(
    val usersDao: UsersDaoSync,
    val postsDao: PostsDaoSync,
    val commentsDao: CommentsDaoSync,
    val interestedUsersDaoSync: InterestedUsersDaoSync,
    val requestedInterestedUsersDaoSync: RequestedInterestedUsersDaoSync
) {

}