package com.example.instachat.services.models.fcm

import com.google.gson.annotations.SerializedName

data class FCMSendNotificationsResponse(
    @SerializedName("multicast_id"  ) var multicastId  : Long?               = null,
    @SerializedName("success"       ) var success      : Int?               = null,
    @SerializedName("failure"       ) var failure      : Int?               = null,
    @SerializedName("canonical_ids" ) var canonicalIds : Int?               = null,
    @SerializedName("results"       ) var results      : ArrayList<Results> = arrayListOf()
)

data class Results (

    @SerializedName("message_id" ) var messageId : String? = null

)