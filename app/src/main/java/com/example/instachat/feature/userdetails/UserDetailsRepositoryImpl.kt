package com.example.instachat.feature.userdetails

import com.example.instachat.services.models.dummyjson.InterestedUsersModel
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.models.fcm.FCMSendNotificationBody
import com.example.instachat.services.models.rest.NotificationModel
import com.example.instachat.services.repository.FirebaseDataSource
import com.example.instachat.services.rest.FCMRestClient
import com.example.instachat.utils.ConnectivityService
import com.example.instachat.utils.Response
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserDetailsRepositoryImpl @Inject constructor(
    val firebaseDataSource: FirebaseDataSource,
    val connectivityService: ConnectivityService,
    val fcmRestClient: FCMRestClient
) :
    UserDetailsRepository {

    override suspend fun getInterestedUserModel(id: String): Response<InterestedUsersModel> {
        return try {
            Response.Loading
            if (connectivityService.hasActiveNetwork()) {
                val db = Firebase.firestore
                val interestedUserModel =
                    db.collection("interestedUserRequests").whereEqualTo("id", id).get().await()
                        .toObjects(InterestedUsersModel::class.java).firstOrNull()
                Response.Success(interestedUserModel)
            } else {
                Response.HandleNetworkError
            }
        } catch (e: java.lang.Exception) {
            Response.Failure(e)
        }

    }

    override suspend fun getUserDetails(userId: String): Response<User> {
        return try {
            Response.Loading
            if (connectivityService.hasActiveNetwork()) {
                val db = Firebase.firestore
                val interestedUserModel =
                    db.collection("users").whereEqualTo("id", userId).get().await()
                        .toObjects(User::class.java).firstOrNull()
                Response.Success(interestedUserModel)
            } else {
                Response.HandleNetworkError
            }
        } catch (e: java.lang.Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun getInterestedUserModelByUserId(userId: String): Response<InterestedUsersModel> {
        return try {
            Response.Loading
            if (connectivityService.hasActiveNetwork()) {
                val db = Firebase.firestore
                val interestedUserModel =
                    db.collection("interestedUserRequests").whereEqualTo("userId", userId).get()
                        .await()
                        .toObjects(InterestedUsersModel::class.java).firstOrNull()
                Response.Success(interestedUserModel)
            } else {
                Response.HandleNetworkError
            }
        } catch (e: java.lang.Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun addAndLinkInterestedModelToUser(interestedUsersModel: InterestedUsersModel): Response<Boolean> {
        return try {
            Response.Loading
            if (connectivityService.hasActiveNetwork()) {
                val db = Firebase.firestore
                db.collection("interestedUserRequests").document(interestedUsersModel.id)
                    .set(interestedUsersModel).await()
                Response.Success(true)
            } else {
                Response.HandleNetworkError
            }
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun updateInterestedModel(interestedUsersModel: InterestedUsersModel): Response<Boolean> {
        return try {
            Response.Loading
            if (connectivityService.hasActiveNetwork()) {
                val db = Firebase.firestore
                db.collection("interestedUserRequests").document(interestedUsersModel.id)
                    .set(interestedUsersModel).await()
                Response.Success(true)
            } else {
                Response.HandleNetworkError
            }
        } catch (e: java.lang.Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun deleteAndDeLinkInterestedModelToUser(interestedModelId: String): Response<Boolean> {
        return try {
            Response.Loading
            if (connectivityService.hasActiveNetwork()) {
                val db = Firebase.firestore
                db.collection("interestedUserRequests").document(interestedModelId).delete().await()
                Response.Success(true)
            } else {
                Response.HandleNetworkError
            }
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun updateUser(user: User): Response<Boolean> {
        return try {
            Response.Loading
            if (connectivityService.hasActiveNetwork()) {
                val db = Firebase.firestore
                db.collection("users").document(user.id).set(user).await()
                Response.Success(true)
            } else {
                Response.HandleNetworkError
            }
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun injectNotification(data: NotificationModel): Response<Boolean> {
        Response.Loading
        return try {
            if (connectivityService.hasActiveNetwork()) {
                val db = Firebase.firestore
                db.collection("notifications")
                    .document(data.id.toString())
                    .set(data)
                    .await()

                updateNotificationToUser(data)
                Response.Success(true)
            } else {
                Response.HandleNetworkError
            }
        } catch (e: java.lang.Exception) {
            Response.Failure(e)
        }

    }

    private suspend fun updateNotificationToUser(data: NotificationModel): Response<Boolean> {
        return try {
            val db = Firebase.firestore
            var user = db.collection("users")
                .document(data.targetUserId)
                .get()
                .await()
                .toObject(User::class.java)

            user = user?.apply {
                this.notifications = (this.notifications ?: emptyList()) + listOf(data.id)
            }


            db.collection("users")
                .document(data.targetUserId)
                .update("notifications", user?.notifications)
                .await()

            Response.Success(true)
        } catch (e: java.lang.Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun sendFCMNotification(body: FCMSendNotificationBody): Response<Boolean> {
        return try {
            Response.Loading
            if (connectivityService.hasActiveNetwork()) {
                fcmRestClient.sendFCMNotification(body)
                Response.Success(true)
            } else {
                Response.HandleNetworkError
            }
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }
}