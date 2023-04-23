package com.example.instachat.services.repository
import com.example.instachat.feature.hometab.models.HomeDataModel
import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.Comment
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.room.dao.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomDataSource @Inject constructor(
    val usersDao: UsersDao,
    val postsDao: PostsDao,
    val commentsDao: CommentsDao,
    val interestedUsersDao: InterestedUsersDao,
    val requestedInterestedUsersDao: RequestedInterestedUsersDao,
    val notificationModelDao: NotificationModelDao
) {

    suspend fun clearAllDatabases(){
        usersDao.deleteUserTable()
        postsDao.deletePostsTable()
        commentsDao.deleteCommentsTable()
        notificationModelDao.deleteNotificationsTable()
    }

    suspend fun getUserByUserId(userId: String): User{
        return usersDao.getUser(userId)
    }

    fun getAllCommentsForPost(postId: Int): Flow<List<Comment>>{
        return commentsDao.getAllCommentsForPost(postId)
    }

    suspend fun getPostByPostId(postId: Int): PostModelItem{
        return postsDao.getPost(postId)
    }

    suspend fun insertPost(postModelItem: PostModelItem) : Long?{
        return postsDao.insert(postModelItem)
    }

    suspend fun getSearchHomeData(): Flow<List<HomeDataModel>>{
        return postsDao.getAllPostsHomeDataFlow()
    }

    fun getHomeDataForUser(userId: String): Flow<List<HomeDataModel>>{
        return postsDao.getPostsHomeDataFlow(userId)
    }

    fun getAllUsersInDB(): Flow<List<User>>{
        return usersDao.getAllUsersFlow()
    }

}