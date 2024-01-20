package com.example.marsad.core.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.marsad.R
import com.example.marsad.core.utils.ALERT_DESCRIPTION_KEY
import com.example.marsad.core.utils.ALERT_ID_KEY
import com.example.marsad.core.utils.ALERT_TITLE_KEY
import com.example.marsad.core.utils.ALERT_TYPE_KEY
import com.example.marsad.domain.models.AlertType
import com.example.marsad.ui.AlarmActivity

class AlertReceiver : BroadcastReceiver() {
    private lateinit var notificationHelper: NotificationHelper
    override fun onReceive(context: Context, intent: Intent) {
        notificationHelper = NotificationHelper(context)

        val id = intent.getIntExtra(ALERT_ID_KEY, 0)
        val title = intent.getStringExtra(ALERT_TITLE_KEY) ?: ""
        val description = intent.getStringExtra(ALERT_DESCRIPTION_KEY) ?: ""
        val alertType = intent.getStringExtra(ALERT_TYPE_KEY)

        when (alertType) {
            AlertType.ALARM -> launchAlertActivity(context, title, description)

            AlertType.NOTIFICATION -> notificationHelper
                .showNotification(id, title, description)

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
