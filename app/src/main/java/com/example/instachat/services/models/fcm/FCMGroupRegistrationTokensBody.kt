package com.example.instachat.services.models.fcm

data class FCMGroupRegistrationTokensBody(
    val operation: String,
    val notification_key_name: String,
    val registration_ids: List<String>
)
