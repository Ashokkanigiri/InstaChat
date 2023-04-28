package com.example.instachat.services.di

import com.example.instachat.feature.chatDetail.repository.ChatDetailRepository
import com.example.instachat.feature.chatDetail.repository.ChatDetailRepositoryImpl
import com.example.instachat.feature.comment.repository.CommentsRepository
import com.example.instachat.feature.comment.repository.CommentsRepositoryImpl
import com.example.instachat.feature.hometab.repository.HomeRepository
import com.example.instachat.feature.hometab.repository.HomeRepositoryImpl
import com.example.instachat.feature.notification.repository.NotificationRepository
import com.example.instachat.feature.notification.repository.NotificationRepositoryImpl
import com.example.instachat.feature.userdetails.UserDetailsRepository
import com.example.instachat.feature.userdetails.UserDetailsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Singleton
    @Binds
    abstract fun bindsCommentsRepository(commentsRepositoryImpl: CommentsRepositoryImpl): CommentsRepository

    @Singleton
    @Binds
    abstract fun bindsHomeRepository(homeRepositoryImpl: HomeRepositoryImpl): HomeRepository

    @Singleton
    @Binds
    abstract fun bindsUserDetailRepository(userDetailsRepositoryImpl: UserDetailsRepositoryImpl): UserDetailsRepository

    @Singleton
    @Binds
    abstract fun bindsNotificationRepository(notificationRepositoryImpl: NotificationRepositoryImpl): NotificationRepository

    @Singleton
    @Binds
    abstract fun bindsChatDetailRepository(chatDetailRepositoryImpl: ChatDetailRepositoryImpl): ChatDetailRepository
}