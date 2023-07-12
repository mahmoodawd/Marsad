package com.example.marsad.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast
import com.example.marsad.R
import com.example.marsad.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class ConnectivityReceiver : BroadcastReceiver() {

    private lateinit var networkConnectivityObserver: NetworkConnectivityObserver
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            networkConnectivityObserver = NetworkConnectivityObserver(context)
            CoroutineScope(Dispatchers.Main).launch {
                networkConnectivityObserver.netState.collectLatest {
                    if (it == NetworkConnectivityObserver.Status.UnAvailable || it == NetworkConnectivityObserver.Status.Lost) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.no_internet_msg),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            networkConnectivityObserver.getNetState()
        }
    }
}