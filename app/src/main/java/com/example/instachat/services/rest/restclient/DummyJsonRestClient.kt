package com.example.instachat.services.rest.restclient

import com.example.instachat.services.models.PostModel
import com.example.instachat.services.models.dummyjson.CommentsModel
import com.example.instachat.services.models.dummyjson.UsersModel
import com.example.instachat.services.models.dummyjson.UsersRestModel
import retrofit2.http.GET

interface DummyJsonRestClient {

    @GET("users")
    suspend fun getAllUsers(): UsersRestModel

    @GET("posts")
    suspend fun getAllPosts(): PostModel

    @GET("comments")
    suspend fun getAllComments(): CommentsModel
}