package com.example.instachat.services.repository

import com.example.instachat.services.rest.RandomUserRestApiClient
import javax.inject.Inject

class RestApiRepository @Inject constructor(val randomUserRestApiClient: RandomUserRestApiClient){
}