package com.example.instachat.services.repository

import com.example.instachat.services.rest.restclient.DummyJsonRestClient
import com.example.instachat.services.rest.restclient.LorealImageListRestClient
import javax.inject.Inject

class RestApiRepository @Inject constructor(
    val lorealImageListRestClient: LorealImageListRestClient,
    val dummyJsonRestClient: DummyJsonRestClient
) {
}