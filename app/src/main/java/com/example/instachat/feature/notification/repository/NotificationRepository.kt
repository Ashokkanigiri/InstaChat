package com.example.instachat.feature.notification.repository

import com.example.instachat.services.models.dummyjson.InterestedUsersModel
import com.example.instachat.services.models.rest.NotificationModel
import com.example.instachat.utils.Response

interface NotificationRepository {

    suspend fun fetchAllNotificationForLoggedUser(userId: String): Response<List<NotificationModel>>

    suspend fun getInterestedUserModel(interestedUSerModelId: String): Response<InterestedUsersModel>

    suspend fun updateInterestedUserModel(interestedUsersModel: InterestedUsersModel): Response<Boolean>

}