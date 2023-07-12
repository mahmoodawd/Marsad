package com.example.marsad.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class NetworkConnectivityObserver(context: Context) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()
    private var _netState = MutableStateFlow(Status.UnAvailable)
    val netState = _netState.asStateFlow()

    @SuppressLint("MissingPermission")
    fun getNetState() {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                _netState.value = Status.Available
            }


            override fun onUnavailable() {
                super.onUnavailable()
                _netState.value = Status.UnAvailable
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                _netState.value = Status.Lost
            }
        }

        connectivityManager.registerNetworkCallback(networkRequest, callback)

    }

    enum class Status {
        Available, UnAvailable, Lost
    }
}