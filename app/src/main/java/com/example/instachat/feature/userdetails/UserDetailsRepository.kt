package com.example.instachat.feature.userdetails

import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.InterestedUsersModel
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.models.fcm.FCMSendNotificationBody
import com.example.instachat.services.models.rest.NotificationModel
import com.example.instachat.utils.Response

interface UserDetailsRepository {

    suspend fun getInterestedUserModel(id: String): Response<InterestedUsersModel>

    suspend fun getUserDetails(userId: String): Response<User>

    suspend fun getInterestedUserModelByUserId(userId: String): Response<InterestedUsersModel>

    suspend fun addAndLinkInterestedModelToUser(interestedUsersModel: InterestedUsersModel): Response<Boolean>

    suspend fun updateInterestedModel(interestedUsersModel: InterestedUsersModel): Response<Boolean>

    suspend fun deleteAndDeLinkInterestedModelToUser(interestedModelId: String): Response<Boolean>

    suspend fun updateUser(user: User): Response<Boolean>

    suspend fun injectNotification(data: NotificationModel): Response<Boolean>

    suspend fun sendFCMNotification(body: FCMSendNotificationBody): Response<Boolean>
}