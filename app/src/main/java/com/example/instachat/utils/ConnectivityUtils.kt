package com.example.instachat.utils

import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.example.instachat.InstaChatApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectivityService @Inject constructor(@ApplicationContext val application: Context) {

    private val connectivityManager: ConnectivityManager =
        application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private var receiver: BroadcastReceiver? = null

    fun hasActiveNetwork(): Boolean{
        val connectionType: Connectiontype = connectionType(connectivityManager)
        return listOf(Connectiontype.WIFI, Connectiontype.CELLULAR).contains(connectionType)
    }

    fun startListening(handle: (Connectiontype) -> Unit){
        this.receiver = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                handle(connectionType(connectivityManager))
            }

        }
        application.registerReceiver(this.receiver, IntentFilter().apply { addAction(ConnectivityManager.CONNECTIVITY_ACTION) })
    }

    fun stopListening(){
        this.receiver?.also { application.unregisterReceiver(it) }
        this.receiver = null
    }

    private fun connectionType(connectivityManager: ConnectivityManager): Connectiontype {
        val activeNetwork: Network = connectivityManager.activeNetwork ?: return Connectiontype.NONE
        val capabilities: NetworkCapabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return Connectiontype.NONE

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> Connectiontype.CELLULAR
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> Connectiontype.WIFI
            else -> Connectiontype.NONE
        }
    }
}

enum class Connectiontype {
    NONE,
    CELLULAR,
    WIFI
}