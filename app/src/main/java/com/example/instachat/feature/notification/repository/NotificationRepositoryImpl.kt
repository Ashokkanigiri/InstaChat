package com.example.instachat.feature.notification.repository

import com.example.instachat.services.models.dummyjson.InterestedUsersModel
import com.example.instachat.services.models.rest.NotificationModel
import com.example.instachat.utils.ConnectivityService
import com.example.instachat.utils.Response
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(private val connectivityService: ConnectivityService) :
    NotificationRepository {


    override suspend fun fetchAllNotificationForLoggedUser(userId: String): Response<List<NotificationModel>> {
        Response.Loading
        return try {
            if (connectivityService.hasActiveNetwork()) {
                val db = Firebase.firestore
                val notifications =
                    db.collection("notifications").whereEqualTo("targetUserId", userId).get()
                        .await().toObjects(NotificationModel::class.java)
                Response.Success(notifications)
            } else {
                Response.HandleNetworkError
            }
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun getInterestedUserModel(interestedUSerModelId: String): Response<InterestedUsersModel> {
        return try {
            if (connectivityService.hasActiveNetwork()) {
                val db = Firebase.firestore
                val data =
                    db.collection("interestedUserRequests").document(interestedUSerModelId).get()
                        .await().toObject(InterestedUsersModel::class.java)
                Response.Success(data)
            } else {
                Response.HandleNetworkError
            }
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun updateInterestedUserModel(interestedUsersModel: InterestedUsersModel): Response<Boolean> {
        return try {
            if (connectivityService.hasActiveNetwork()) {
                val db = Firebase.firestore
                db.collection("interestedUserRequests").document(interestedUsersModel.id).update("followAccepted", interestedUsersModel.isFollowAccepted).await()
                Response.Success(true)
            } else {
                Response.HandleNetworkError
            }
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }
}