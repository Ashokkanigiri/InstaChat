package com.example.instachat.services.models.fcm

data class FCMSendNotificationBody (val to: String, val notification: FCMSendNotificationData)

data class FCMSendNotificationData(val title: String, val body: String)