package com.example.instachat.services.sharedprefs

import android.content.Context
import java.time.LocalDateTime
import java.util.UUID


object SharedPreferenceService {

    private const val FCM_REGISTRATION_TOKEN_CREATED_DATE_KEY ="FCM_REGISTRATION_TOKEN_CREATED_DATE_KEY"
    private const val FCM_NOTIFICATION_KEY ="FCM_NOTIFICATION_KEY"
    private const val SESSION_ID ="UNIQUE_SESSION_ID"

    fun putFCMRegistrationTokenCreatedDate(context: Context){
        getSharedPrefs(context).edit()
            .putString(FCM_REGISTRATION_TOKEN_CREATED_DATE_KEY, LocalDateTime.now().toString()).apply()
    }

    fun getFCMRegistrationTokenCreatedDate(context: Context): String?{
        return getSharedPrefs(context).getString(
            FCM_REGISTRATION_TOKEN_CREATED_DATE_KEY, null)
    }

    fun putFcmNotificationKey(context: Context, notificationKey: String){
        getSharedPrefs(context).edit().putString(FCM_NOTIFICATION_KEY, notificationKey).apply()
    }

    fun getFcmNotificationKey(context: Context): String?{
        return getSharedPrefs(context).getString(FCM_NOTIFICATION_KEY, null)
    }

    fun putUniqueSessionId(context: Context, sessionId: String){
        getSharedPrefs(context).edit().putString(SESSION_ID, sessionId).apply()
    }

    fun getUniqueSessionId(context: Context): String?{
        return getSharedPrefs(context).getString(SESSION_ID, null)
    }

    fun clearAllKeys(context: Context){
        getSharedPrefs(context).edit().clear().apply()
    }

    private fun getSharedPrefs(context: Context) = context.getSharedPreferences("insta-chat", Context.MODE_PRIVATE)
}