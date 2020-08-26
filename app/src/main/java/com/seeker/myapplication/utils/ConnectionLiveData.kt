package com.seeker.myapplication.utils


import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.LiveData
import java.lang.ref.WeakReference

class ConnectionLiveData(private val ctxRef: WeakReference<Context>) : LiveData<Boolean>() {

    private var ConnectivityManager: ConnectivityManager =
        ctxRef.get()?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private lateinit var connectivityManagerCallback: ConnectivityManager.NetworkCallback

    override fun onActive() {
        super.onActive()

        updateConnection()
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> ConnectivityManager.registerDefaultNetworkCallback(getConnectivityManagerCallback())
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> lollipopNetworkAvailableRequest()
            else -> {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    ctxRef.get()?.registerReceiver(
                        networkReceiver,
                        IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
                    )
                }
            }
        }
    }

    override fun onInactive() {
        super.onInactive()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
        } else {
            ctxRef.get()?.unregisterReceiver(networkReceiver)
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun lollipopNetworkAvailableRequest() {
        val builder = NetworkRequest.Builder()
            .addTransportType(android.net.NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(android.net.NetworkCapabilities.TRANSPORT_WIFI)


        ConnectivityManager.registerNetworkCallback(
            builder.build(),
            getConnectivityManagerCallback()
        )

    }


    private fun getConnectivityManagerCallback(): ConnectivityManager.NetworkCallback {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManagerCallback =
                object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network?) {
                        postValue(true)
                    }

                    override fun onLost(network: Network?) {
                        postValue(false)
                    }
                }
            return connectivityManagerCallback

        } else {
            throw IllegalAccessError("Should not happened")
        }
    }

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updateConnection()
        }
    }


    private fun updateConnection() {
        var activeNetwork: NetworkInfo? = ConnectivityManager.activeNetworkInfo
        postValue(activeNetwork?.isConnected == true)
    }
}





