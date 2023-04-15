package com.example.instachat.services.rest

import com.example.instachat.services.rest.restclient.DummyJsonRestClient
import com.example.instachat.services.rest.restclient.LorealImageListRestClient
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

    @Named("fcm")
    @Provides
    @Singleton
    fun providesFCMRetrofitInstance(@Named("fcm-client") client: OkHttpClient): Retrofit{
        return Retrofit.Builder()
            .baseUrl(FCMRestClient.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Named("fcm-client")
    @Singleton
    @Provides
    fun providesFCMokHttpClient(): OkHttpClient{
        return OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "key=AAAAheSEFdw:APA91bEC1ZSJlMk8G4qxZap4-wVx6FSnBL0lan1i4RyZjepo8xitzKMc_SkeKXBMh5P_jQXRCX46KzpoFSz4tuTWfJHWdNF3Xvlgn0YEyeOIy9DpkfsltwDOEix1wYXzJ2XNEP227Bw_")
                .build()
            chain.proceed(newRequest)
        }.build()
    }

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

    @Provides
    @Singleton
    fun providesFCMRetofitClient(@Named("fcm") retrofit: Retrofit): FCMRestClient{
        return retrofit.create(FCMRestClient::class.java)
    }
}