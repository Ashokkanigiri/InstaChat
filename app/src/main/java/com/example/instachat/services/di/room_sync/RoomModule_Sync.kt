package com.example.instachat.services.di.room_sync

import android.content.Context
import androidx.room.Room
import com.example.instachat.services.repository.RoomSyncRepository
import com.example.instachat.services.room.dao.*
import com.example.instachat.services.room_sync.InstaChatDb_sync
import com.example.instachat.services.room_sync.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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

    @Singleton
    @Provides
    fun providesInterestedUsersDao(instaChatDb: InstaChatDb_sync): InterestedUsersDaoSync {
        return instaChatDb.interestedUsersDaoSync()
    }

    @Singleton
    @Provides
    fun providesRequestedInterestedUsersDao(instaChatDb: InstaChatDb_sync): RequestedInterestedUsersDaoSync {
        return instaChatDb.requestedInterestedUsersDaoSync()
    }


    @Provides
    fun providesRoomRepository(
        usersDao: UsersDaoSync,
        commentsDao: CommentsDaoSync,
        postsDao: PostsDaoSync,
        interestedUsersDaoSync: InterestedUsersDaoSync,
        requestedInterestedUsersDaoSync: RequestedInterestedUsersDaoSync
    ): RoomSyncRepository {
        return RoomSyncRepository(
            usersDao,
            postsDao,
            commentsDao,
            interestedUsersDaoSync,
            requestedInterestedUsersDaoSync
        )
    }
}