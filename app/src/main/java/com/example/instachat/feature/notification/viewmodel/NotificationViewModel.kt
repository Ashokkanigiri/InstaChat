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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    val roomRepository: RoomRepository,
    val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val userId = Firebase.auth.currentUser?.uid ?: ""

    val todayAdapter = NotificationsAdapter()
    val todayHeaderAdapter = NotificationHeaderAdapter()

    val yesterdayAdapter = NotificationsAdapter()
    val yesterdayHeaderAdapter = NotificationHeaderAdapter()

    val pastAdapter = NotificationsAdapter()
    val pastHeaderAdapter = NotificationHeaderAdapter()

    val concatAdapter = ConcatAdapter()

    fun loadData() {
        initAdapters()
        loadAllNotificationsForLoggedUser(userId)
    }

    fun injectData() {
        viewModelScope.launch {
            firebaseRepository.injectAllNotificationsFromFirebase(userId)
        }
    }

    fun loadAllNotificationsForLoggedUser(userId: String) {
        viewModelScope.launch (Dispatchers.IO){
            roomRepository.notificationModelDao.getNotificationsForUserId(userId).collect {
               withContext(Dispatchers.Main){
                   segregateData(it)
               }
            }
        }
    }

    fun segregateData(notificationModels: List<NotificationModel>?) {
        notificationModels?.let { notifications ->
            val todayNotifications = notifications.filter { DateUtils.isToday(it.timeStamp) }
            val yesterdayNotifications =
                notifications.filter { DateUtils.isYesterday(it.timeStamp) }

            if(todayNotifications.isNotEmpty() ){
                todayHeaderAdapter.submitList(listOf("Today"))
                todayAdapter.submitList(todayNotifications)
                Log.d("ahjetgkasj", "      yesterdayNotifications.isNotEmpty()")

            }else{
                concatAdapter.removeAdapter(todayHeaderAdapter)
                concatAdapter.removeAdapter(todayAdapter)
                Log.d("ahjetgkasj", "   todayNotifications.isEmpty()")
            }

            if(yesterdayNotifications.isNotEmpty()){
                yesterdayHeaderAdapter.submitList(listOf("Yesterday"))
                yesterdayAdapter.submitList(yesterdayNotifications)
                Log.d("ahjetgkasj", "      yesterdayNotifications.isNotEmpty()")

            }else{
                concatAdapter.removeAdapter(yesterdayHeaderAdapter)
                concatAdapter.removeAdapter(yesterdayAdapter)
                Log.d("ahjetgkasj", " yesterdayNotifications.isEmpty()")
            }
        }
    }

    fun initAdapters(){
        concatAdapter.addAdapter(todayHeaderAdapter)
        concatAdapter.addAdapter(todayAdapter)
        concatAdapter.addAdapter(yesterdayHeaderAdapter)
        concatAdapter.addAdapter(yesterdayAdapter)
    }

    fun clearAdapters(){
        concatAdapter.removeAdapter(todayAdapter)
        concatAdapter.removeAdapter(yesterdayAdapter)
        concatAdapter.removeAdapter(todayHeaderAdapter)
        concatAdapter.removeAdapter(yesterdayHeaderAdapter)
    }
}