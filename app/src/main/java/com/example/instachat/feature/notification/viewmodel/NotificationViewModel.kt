package com.example.instachat.feature.notification.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ConcatAdapter
import com.example.instachat.feature.notification.view.NotificationHeaderAdapter
import com.example.instachat.feature.notification.view.NotificationsAdapter
import com.example.instachat.services.models.rest.NotificationModel
import com.example.instachat.services.repository.FirebaseRepository
import com.example.instachat.services.repository.RoomRepository
import com.example.instachat.utils.DateUtils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    val roomRepository: RoomRepository,
    val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val userId = Firebase.auth.currentUser?.uid ?: ""

    val todayAdapter = NotificationsAdapter(this)
    val todayHeaderAdapter = NotificationHeaderAdapter()

    val yesterdayAdapter = NotificationsAdapter(this)
    val yesterdayHeaderAdapter = NotificationHeaderAdapter()

    val pastAdapter = NotificationsAdapter(this)
    val pastHeaderAdapter = NotificationHeaderAdapter()

    val concatAdapter = ConcatAdapter()

    fun loadData() {
        loadAllNotificationsForLoggedUser(userId)
    }

    fun injectData() {
        viewModelScope.launch {
            async { firebaseRepository.injectAllNotificationsFromFirebase(userId) }.await()
        }
    }

    fun loadAllNotificationsForLoggedUser(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            roomRepository.notificationModelDao.getNotificationsForUserId(userId).collect {
                segregateData(it)
            }
        }
    }

    suspend fun segregateData(notificationModels: List<NotificationModel>?) {
        notificationModels?.let { notifications ->
            val todayNotifications = notifications.filter { DateUtils.isToday(it.timeStamp) }
            val yesterdayNotifications =
                notifications.filter { DateUtils.isYesterday(it.timeStamp) }

            val twoDaysAgoNotifications = notifications.filter {
                DateUtils.isDateTwoDaysAgo(it.timeStamp)
            }

            withContext(Dispatchers.Main){
                if (todayNotifications.isNotEmpty()) {
                    concatAdapter.addAdapter(todayHeaderAdapter)
                    concatAdapter.addAdapter(todayAdapter)
                    todayHeaderAdapter.submitList(listOf("Today"))
                    todayAdapter.submitList(todayNotifications)


                } else {
                    concatAdapter.removeAdapter(todayHeaderAdapter)
                    concatAdapter.removeAdapter(todayAdapter)
                }

                if (yesterdayNotifications.isNotEmpty()) {
                    concatAdapter.addAdapter(yesterdayHeaderAdapter)
                    concatAdapter.addAdapter(yesterdayAdapter)
                    yesterdayHeaderAdapter.submitList(listOf("Yesterday"))
                    yesterdayAdapter.submitList(yesterdayNotifications)
                    Log.d("qfiqiofnq", "todayNotifications: ${Gson().toJson(yesterdayNotifications)}")

                } else {
                    concatAdapter.removeAdapter(yesterdayHeaderAdapter)
                    concatAdapter.removeAdapter(yesterdayAdapter)
                }

                if (twoDaysAgoNotifications.isNotEmpty()) {
                    concatAdapter.addAdapter(pastHeaderAdapter)
                    concatAdapter.addAdapter(pastAdapter)
                    pastHeaderAdapter.submitList(listOf("Older"))
                    pastAdapter.submitList(twoDaysAgoNotifications)
                    Log.d("qfiqiofnq", "todayNotifications: ${Gson().toJson(twoDaysAgoNotifications)}")

                } else {
                    concatAdapter.removeAdapter(pastHeaderAdapter)
                    concatAdapter.removeAdapter(pastAdapter)
                }
                concatAdapter.notifyDataSetChanged()
            }

        }
    }


    fun clearAdapters() {
        concatAdapter.removeAdapter(todayAdapter)
        concatAdapter.removeAdapter(yesterdayAdapter)
        concatAdapter.removeAdapter(todayHeaderAdapter)
        concatAdapter.removeAdapter(yesterdayHeaderAdapter)
        concatAdapter.removeAdapter(pastHeaderAdapter)
        concatAdapter.removeAdapter(pastAdapter)
    }

    fun onFollowButtonClicked() {
        // remove request object
        // update intrestedlist object to request - true && follow true
    }
}