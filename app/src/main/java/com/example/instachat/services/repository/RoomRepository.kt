package com.example.instachat.services.repository

import com.example.instachat.feature.home.HomeDataModel
import com.example.instachat.feature.home.HomeDataModel1
import com.example.instachat.feature.home.HomeViewModel
import com.example.instachat.services.models.dummyjson.Comment
import com.example.instachat.services.room.dao.CommentsDao
import com.example.instachat.services.room.dao.PostsDao
import com.example.instachat.services.room.dao.UsersDao
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class RoomRepository @Inject constructor(
    val usersDao: UsersDao,
    val postsDao: PostsDao,
    val commentsDao: CommentsDao
) {
     fun getHomeData(userId: String) = flow {
        val postsForUserFlow = postsDao.getPostsHomeData(userId)

         postsForUserFlow.collect(){
             val data = ArrayList<HomeDataModel1>()
             it.forEach {
                 val comment = commentsDao.getAllCommentsForPost(it.postId)
                 data.add(HomeDataModel1(homeDataModel = it, comments = comment))
             }
             emit(data)
         }
    }

}