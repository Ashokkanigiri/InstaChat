package com.example.instachat.services.rest

import com.example.instachat.BuildConfig
import com.example.instachat.services.rest.restclient.JsonPlaceHolderApiClient
import com.example.instachat.services.rest.restclient.LorealImageListRestClient
import com.example.instachat.services.rest.restclient.RandomUserRestApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RestModule {

    @Named("random_user_retrofit_instance")
    @Provides
    @Singleton
    fun providesRetrofitInstance(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(BaseUrlConstants.RANDOM_USER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Named("json_placeholder_retrofit_instance")
    @Provides
    @Singleton
    fun providesJsonPlaceholderRetrofitInstance(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(BaseUrlConstants.JSON_PLACEHOLDER_BASE_RUL)
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
    fun providesRandomImagesOkhttpClient(): OkHttpClient{
        return OkHttpClient().newBuilder().addInterceptor {chain->
           val originalRequest = chain.request()
            val request = originalRequest.newBuilder().header("X-Api-Key", "").build()
            chain.proceed(request)
        }.build()
    }

    @Provides
    @Singleton
    fun providesRandomUserModelApiClient(@Named("random_user_retrofit_instance") retrofit: Retrofit) : RandomUserRestApiClient {
        return retrofit.create(RandomUserRestApiClient::class.java)
    }

    @Provides
    @Singleton
    fun providesJsonPlaceholderApiClient(@Named("json_placeholder_retrofit_instance") retrofit: Retrofit) : JsonPlaceHolderApiClient {
        return retrofit.create(JsonPlaceHolderApiClient::class.java)
    }

    @Provides
    @Singleton
    fun provideslorealApiClient(@Named("loreal_retrofit_instance") retrofit: Retrofit) : LorealImageListRestClient {
        return retrofit.create(LorealImageListRestClient::class.java)
    }
}