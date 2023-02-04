package com.example.instachat.services.di.room_sync

import android.content.Context
import androidx.room.Room
import com.example.instachat.services.repository.RoomSyncRepository
import com.example.instachat.services.room.dao.*
import com.example.instachat.services.room_sync.InstaChatDb_sync
import com.example.instachat.services.room_sync.dao.CommentsDaoSync
import com.example.instachat.services.room_sync.dao.PostsDaoSync
import com.example.instachat.services.room_sync.dao.UsersDaoSync
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RoomModule_Sync {

    @Provides
    fun providesRoomInstance(@ApplicationContext context: Context): InstaChatDb_sync {
        return Room.databaseBuilder(context, InstaChatDb_sync::class.java, "insta-chat-db-sync")
            .allowMainThreadQueries().build()
    }

    @Provides
    fun providesUserDao(instaChatDb: InstaChatDb_sync): UsersDaoSync {
        return instaChatDb.usersDaoSync()
    }

    @Provides
    fun providesPostsDao(instaChatDb: InstaChatDb_sync): PostsDaoSync {
        return instaChatDb.postsDaoSync()
    }

    @Provides
    fun providesCommentsDao(instaChatDb: InstaChatDb_sync): CommentsDaoSync {
        return instaChatDb.commentsDaoSync()
    }

    @Provides
    fun providesRoomRepository(
        usersDao: UsersDaoSync,
        commentsDao: CommentsDaoSync,
        postsDao: PostsDaoSync
    ): RoomSyncRepository {
        return RoomSyncRepository(usersDao, postsDao, commentsDao)
    }
}