package com.example.instachat.services.rest.restclient

import com.example.instachat.services.models.RandomUserModel
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface RandomUserRestApiClient {

    @GET("api/")
    suspend fun getRandomUser(): RandomUserModel
}