package com.example.instachat.services.repository

import com.example.instachat.services.rest.restclient.JsonPlaceHolderApiClient
import com.example.instachat.services.rest.restclient.LorealImageListRestClient
import com.example.instachat.services.rest.restclient.RandomUserRestApiClient
import javax.inject.Inject

class RestApiRepository @Inject constructor(
    val randomUserRestApiClient: RandomUserRestApiClient,
    val jsonPlaceHolderApiClient: JsonPlaceHolderApiClient,
    val lorealImageListRestClient: LorealImageListRestClient
) {
}