package com.cinema.entract.data.platform

expect class NetworkUtils {
    fun isNetworkAvailable(): Boolean
    fun isConnectedOnWifi(): Boolean
}
