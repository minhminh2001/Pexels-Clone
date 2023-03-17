package com.example.image.utils

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*

class CheckInternetConnection {
        public fun hasInternetConnection(app :Application): Boolean {
            val connectivityManager = app.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val networkCapabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                networkCapabilities.hasTransport(TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                networkCapabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
            return false
        }
}