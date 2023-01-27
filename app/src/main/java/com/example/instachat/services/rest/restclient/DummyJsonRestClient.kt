package com.example.instachat.services.rest.restclient

import com.example.instachat.services.models.dummyjson.UsersModel
import retrofit2.http.GET

interface DummyJsonRestClient {

    @GET("users")
    suspend fun getUsers(): UsersModel
}