package com.example.instachat.services.rest.restclient

import com.example.instachat.services.models.PostModel
import retrofit2.http.GET

interface JsonPlaceHolderApiClient {

    @GET("posts")
    suspend fun getAllPosts(): PostModel
}