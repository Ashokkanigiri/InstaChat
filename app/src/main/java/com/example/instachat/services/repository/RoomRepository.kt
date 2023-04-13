package com.example.instachat.services.repository
import com.example.instachat.services.room.dao.*
import javax.inject.Inject

class RoomRepository @Inject constructor(
    val usersDao: UsersDao,
    val postsDao: PostsDao,
    val commentsDao: CommentsDao,
    val interestedUsersDao: InterestedUsersDao,
    val requestedInterestedUsersDao: RequestedInterestedUsersDao
) {

    suspend fun clearAllDatabases(){
        usersDao.deleteUserTable()
        postsDao.deletePostsTable()
        commentsDao.deleteCommentsTable()
    }

}