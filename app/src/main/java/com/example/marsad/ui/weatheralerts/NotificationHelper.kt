package com.example.marsad.ui.weatheralerts

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.WorkerThread
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.marsad.R
import com.example.marsad.ui.AlarmActivity
import com.example.marsad.ui.MainActivity

class NotificationHelper(private val context: Context) {

    companion object {
        private const val CHANNEL_ALERTS = "alerts"
        private const val REQUEST_CODE = 1
    }

    private val notificationManager: NotificationManager =
        (context.getSystemService(Context.NOTIFICATION_SERVICE)
            ?: throw IllegalStateException()) as NotificationManager

    fun setUpNotificationChannel() {
        if (notificationManager.getNotificationChannel(CHANNEL_ALERTS) == null) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ALERTS, context.getString(
                        R.string.alerts_channel_name
                    ), NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = context.getString(R.string.alerts_channel_description)
                }
            )
        }
    }

    @WorkerThread
    fun showNotification(id:Long, title: String, description: String) {
        val pendingIntent = PendingIntent.getActivity(
            context,
            REQUEST_CODE, Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                action = Intent.ACTION_VIEW
            },
            PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ALERTS)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(description)
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        notificationManager.notify(id.toInt(), builder.build())
    }

    fun showAlarm(title: String, description: String) {
        val fullScreenIntent = Intent(context, AlarmActivity::class.java)
        val fullScreenPendingIntent = PendingIntent.getActivity(
            context, 0,
            fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ALERTS)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(description)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setFullScreenIntent(fullScreenPendingIntent, true)
        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())

    }
}