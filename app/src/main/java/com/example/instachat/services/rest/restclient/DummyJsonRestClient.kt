package com.example.instachat.services.rest.restclient

import com.example.instachat.services.models.PostModel
import com.example.instachat.services.models.dummyjson.CommentsModel
import com.example.instachat.services.models.dummyjson.UsersModel
import retrofit2.http.GET

interface DummyJsonRestClient {

    @GET("users")
    suspend fun getAllUsers(): UsersModel

    @GET("posts")
    suspend fun getAllPosts(): PostModel

    @GET("comments")
    suspend fun getAllComments(): CommentsModel
}