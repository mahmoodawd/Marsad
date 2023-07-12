package com.example.marsad.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.marsad.R
import com.example.marsad.data.model.AlertType
import com.example.marsad.ui.AlarmActivity

class AlertReceiver : BroadcastReceiver() {
    lateinit var notificationHelper: NotificationHelper
    override fun onReceive(context: Context, intent: Intent) {
        notificationHelper = NotificationHelper(context).apply {
            setUpNotificationChannel()
        }
        val id = intent.getLongExtra(context.getString(R.string.alert_id_key), 0)
        val title = intent.getStringExtra(context.getString(R.string.alert_title_key)).toString()
        val description =
            intent.getStringExtra(context.getString(R.string.alert_description_key)).toString()
        val alertType = intent.getStringExtra(context.getString(R.string.alert_type_key))
        when (alertType) {
            AlertType.ALARM -> {
                launchAlertActivity(context, title, description)
            }
            AlertType.NOTIFICATION -> {
                notificationHelper.showNotification(
                    id,
                    title, description
                )
            }
        }
    }

    private fun launchAlertActivity(context: Context, title: String, description: String) {
        val myIntent = Intent(context, AlarmActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(context.getString(R.string.alert_title_key), title)
            putExtra(context.getString(R.string.alert_description_key), description)
        }
        context.startActivity(myIntent)
        Log.i("TAG", "onReceive: Alarm Activity Started")
    }
}
