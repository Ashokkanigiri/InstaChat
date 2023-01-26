package com.example.instachat.services.rest

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RestModule {

    @Provides
    @Singleton
    fun providesRetrofitInstance(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(BaseUrlConstants.RANDOM_USER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesRandomUserModelApiClient(retrofit: Retrofit) : RandomUserRestApiClient{
        return retrofit.create(RandomUserRestApiClient::class.java)
    }
}