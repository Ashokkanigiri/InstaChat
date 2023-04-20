package com.example.instachat

import android.app.Application
import android.graphics.Color
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.example.instachat.services.sharedprefs.SharedPreferenceService
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import dagger.hilt.android.HiltAndroidApp
import java.util.UUID
import javax.inject.Inject

@HiltAndroidApp
class InstaChatApplication: Application() , Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory


    override fun onCreate() {
        super.onCreate()

        /**
         * SESSION_ID is an unique key which helps in managing the current user session
         *
         * SESSION_ID will only be null if user logouts/clear data/ uninstalls the application
         */
        if(SharedPreferenceService.getUniqueSessionId(this) == null){
            SharedPreferenceService.putUniqueSessionId(this, UUID.randomUUID().toString())
        }

    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

}