package com.example.instachat.feature.notification.viewmodel

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ConcatAdapter
import com.example.instachat.feature.notification.repository.NotificationRepository
import com.example.instachat.feature.notification.view.NotificationHeaderAdapter
import com.example.instachat.feature.notification.view.NotificationsAdapter
import com.example.instachat.feature.userdetails.UserDetailPostsAdapter
import com.example.instachat.services.models.rest.NotificationModel
import com.example.instachat.services.repository.FirebaseDataSource
import com.example.instachat.services.repository.RoomDataSource
import com.example.instachat.utils.DateUtils
import com.example.instachat.utils.Response
import com.example.instachat.utils.SingleLiveEvent
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
    val roomDataSource: RoomDataSource,
    val firebaseDataSource: FirebaseDataSource,
    val notificationRepository: NotificationRepository
) : ViewModel() {

    private val userId = Firebase.auth.currentUser?.uid ?: ""

    private val todayAdapter = NotificationsAdapter(this)
    private val todayHeaderAdapter = NotificationHeaderAdapter()
    private val yesterdayAdapter = NotificationsAdapter(this)
    private val yesterdayHeaderAdapter = NotificationHeaderAdapter()
    private val pastAdapter = NotificationsAdapter(this)
    private val pastHeaderAdapter = NotificationHeaderAdapter()

    val concatAdapter = ConcatAdapter()
    val progressbarVisibility = ObservableField<Boolean>()

    val connectivityDialogEvent = SingleLiveEvent<Boolean>()
    val handleError = SingleLiveEvent<String>()


    private fun loadProgressbar(){
        progressbarVisibility.set(true)
    }

    private fun dismissProgressBar(){
        progressbarVisibility.set(false)
    }

    fun loadData() {
        loadProgressbar()
        viewModelScope.launch (Dispatchers.IO){
            when(val notifications = notificationRepository.fetchAllNotificationForLoggedUser(userId)){
                is Response.Failure -> {
                    handleError.postValue(notifications.e.localizedMessage)
                    dismissProgressBar()
                }
                is Response.HandleNetworkError -> {
                    connectivityDialogEvent.postValue(true)
                    dismissProgressBar()
                }
                is Response.Loading -> {
                    loadProgressbar()
                }
                is Response.Success -> {
                    segregateData(notifications.data)
                    dismissProgressBar()
                }
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

                } else {
                    concatAdapter.removeAdapter(yesterdayHeaderAdapter)
                    concatAdapter.removeAdapter(yesterdayAdapter)
                }

                if (twoDaysAgoNotifications.isNotEmpty()) {
                    concatAdapter.addAdapter(pastHeaderAdapter)
                    concatAdapter.addAdapter(pastAdapter)
                    pastHeaderAdapter.submitList(listOf("Older"))
                    pastAdapter.submitList(twoDaysAgoNotifications)

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