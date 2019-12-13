package com.cinema.entract.data.platform

import android.content.Context
import android.net.ConnectivityManager

actual class NetworkUtils(context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Suppress("DEPRECATION")
    actual fun isNetworkAvailable(): Boolean {
        return connectivityManager.activeNetworkInfo?.isConnected ?: false
    }

    @Suppress("DEPRECATION")
    actual fun isConnectedOnWifi(): Boolean =
        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)?.isConnected ?: false
}
