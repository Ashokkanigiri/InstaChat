package com.example.instachat.services.rest.restclient

import com.example.instachat.services.models.LorealImagesListModel
import retrofit2.http.GET
import retrofit2.http.Query

interface LorealImageListRestClient {

    @GET("list")
    suspend fun getAllImages(@Query("page") page: Int = 1, @Query("limit") limit: Int = 100, @Query("blur") blur: Int = 2) : LorealImagesListModel

    @GET("list")
    suspend fun getAllImagesv1() : LorealImagesListModel


}