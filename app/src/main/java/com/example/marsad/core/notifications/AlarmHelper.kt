package com.example.marsad.core.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.marsad.core.utils.ALERT_DESCRIPTION_KEY
import com.example.marsad.core.utils.ALERT_ID_KEY
import com.example.marsad.core.utils.ALERT_TITLE_KEY
import com.example.marsad.core.utils.ALERT_TYPE_KEY
import com.example.marsad.domain.models.NotificationAlert

class AlarmHelper(val context: Context, private val alarmManager: AlarmManager) {


    private val pendingIntentFlag = when (Build.VERSION.SDK_INT) {
        Build.VERSION_CODES.TIRAMISU -> PendingIntent.FLAG_IMMUTABLE
        else -> PendingIntent.FLAG_UPDATE_CURRENT
    }

    fun scheduleAlert(alertItem: NotificationAlert) {
        Log.d(TAG, "scheduleAlert: Begin")
        val alarmIntent =
            Intent(context, AlertReceiver::class.java).apply {
                putExtra(ALERT_ID_KEY, alertItem.id)
                putExtra(ALERT_TITLE_KEY, alertItem.title)
                putExtra(ALERT_DESCRIPTION_KEY, alertItem.description)
                putExtra(ALERT_TYPE_KEY, alertItem.type)
            }
        val pendingIntent = PendingIntent.getBroadcast(
            context, alertItem.id, alarmIntent, pendingIntentFlag
        )
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            alertItem.firesAt.toLong(),
            pendingIntent
        )
    }

    fun cancelAlert(alertId: Int) {
        val alarmIntent =
            Intent(context, AlertReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context, alertId, alarmIntent, pendingIntentFlag
        )
        alarmManager.cancel(pendingIntent)
    }

    companion object {
        private val TAG = AlarmHelper::class.java.simpleName
    }
}