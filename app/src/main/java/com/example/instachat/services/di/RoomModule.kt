package com.example.instachat.services.di

import android.content.Context
import androidx.room.Room
import com.example.instachat.services.repository.RoomRepository
import com.example.instachat.services.room.InstaChatDb
import com.example.instachat.services.room.dao.CommentsDao
import com.example.instachat.services.room.dao.PostsDao
import com.example.instachat.services.room.dao.UsersDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun providesRoomInstance(@ApplicationContext context: Context): InstaChatDb {
        return Room.databaseBuilder(context, InstaChatDb::class.java, "insta-chat-db")
            .allowMainThreadQueries().build()
    }

    @Singleton
    @Provides
    fun providesUserDao(instaChatDb: InstaChatDb): UsersDao {
        return instaChatDb.usersDao()
    }

    @Singleton
    @Provides
    fun providesPostsDao(instaChatDb: InstaChatDb): PostsDao {
        return instaChatDb.postsDao()
    }

    @Singleton
    @Provides
    fun providesCommentsDao(instaChatDb: InstaChatDb): CommentsDao {
        return instaChatDb.commentsDao()
    }

    @Singleton
    @Provides
    fun providesRoomRepository(
        usersDao: UsersDao,
        commentsDao: CommentsDao,
        postsDao: PostsDao
    ): RoomRepository {
        return RoomRepository(usersDao, postsDao, commentsDao)
    }
}