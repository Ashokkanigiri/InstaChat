package com.example.instachat.services.rest

import com.example.instachat.services.rest.restclient.DummyJsonRestClient
import com.example.instachat.services.rest.restclient.LorealImageListRestClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RestModule {

    @Named("dummy_json")
    @Provides
    @Singleton
    fun providesRetrofitInstance(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(BaseUrlConstants.DUMMY_JSON_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Named("loreal_retrofit_instance")
    @Provides
    @Singleton
    fun providesLorealRetrofitInstance(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(BaseUrlConstants.LOREAL_IMAGES_LIST_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideslorealApiClient(@Named("loreal_retrofit_instance") retrofit: Retrofit) : LorealImageListRestClient {
        return retrofit.create(LorealImageListRestClient::class.java)
    }

    @Provides
    @Singleton
    fun providesDummyJsonApiClient(@Named("dummy_json") retrofit: Retrofit) : DummyJsonRestClient {
        return retrofit.create(DummyJsonRestClient::class.java)
    }
}