package com.example.instachat.services.models.fcm

data class FCMSendNotificationsResponse(
    val success: Int,
    val failure: Int,
    val failed_registration_ids: List<String>
)

