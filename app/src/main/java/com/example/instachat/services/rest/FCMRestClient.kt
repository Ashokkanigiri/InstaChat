package com.example.instachat.services.rest

import com.example.instachat.services.models.fcm.FCMGroupRegistrationTokensBody
import com.example.instachat.services.models.fcm.FCMGroupRegistrationTokensModel
import com.example.instachat.services.models.fcm.FCMSendNotificationBody
import com.example.instachat.services.models.fcm.FCMSendNotificationsResponse
import retrofit2.http.*


interface FCMRestClient {

    companion object {
        const val BASE_URL = "https://fcm.googleapis.com/fcm/"
    }

    @POST("/notification")
    suspend fun groupNotificationsWithRegistrationToken(
        @Body fcmGroupRegistrationTokensBody: FCMGroupRegistrationTokensBody,
        @HeaderMap headers: Map<String, String>
    ): FCMGroupRegistrationTokensModel


    @GET("/notification")
    suspend fun retrieveNotificationToken(
        @Query("notification_key_name") notification_key_name: String,
        @HeaderMap headers: Map<String, String>
    ): FCMGroupRegistrationTokensModel


    @POST("send")
    suspend fun sendFCMNotification(
        @Body fcmSendNotificationBody: FCMSendNotificationBody
    ): FCMSendNotificationsResponse

}