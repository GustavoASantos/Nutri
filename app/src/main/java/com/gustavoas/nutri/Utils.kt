package com.gustavoas.nutri

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object Utils {
    fun dpToPx(context: Context, dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }

    fun getRatingString(rating: Int, ratingCount: Int): String {
        var ratingEmojis = ""

        for (i in 1..5) {
            ratingEmojis += if (i > rating) {
                "☆"
            } else {
                "★"
            }
        }
        return "$ratingEmojis $rating/5 ($ratingCount)"
    }

    fun isInternetAvailable(context: Context) : Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}