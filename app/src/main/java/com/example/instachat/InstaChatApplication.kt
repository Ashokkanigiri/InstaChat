package com.example.instachat

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class InstaChatApplication: Application() {

    override fun onCreate() {
        super.onCreate()

    }
}