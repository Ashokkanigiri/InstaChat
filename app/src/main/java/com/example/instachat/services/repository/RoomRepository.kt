package com.example.instachat.services.repository
import com.example.instachat.services.room.dao.CommentsDao
import com.example.instachat.services.room.dao.PostsDao
import com.example.instachat.services.room.dao.UsersDao
import javax.inject.Inject

class RoomRepository @Inject constructor(
    val usersDao: UsersDao,
    val postsDao: PostsDao,
    val commentsDao: CommentsDao
) {


}