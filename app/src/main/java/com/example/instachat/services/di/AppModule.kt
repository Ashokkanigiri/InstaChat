package com.example.instachat.services.di

import com.example.instachat.feature.comment.repository.CommentsRepository
import com.example.instachat.feature.comment.repository.CommentsRepositoryImpl
import com.example.instachat.feature.hometab.repository.HomeRepository
import com.example.instachat.feature.hometab.repository.HomeRepositoryImpl
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
}