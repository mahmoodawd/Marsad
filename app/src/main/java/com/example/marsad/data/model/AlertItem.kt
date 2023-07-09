package com.example.marsad.data.model

import android.app.PendingIntent
import androidx.room.Entity
import androidx.room.Ignore
import java.io.Serializable

object AlertType {
    const val NOTIFICATION = "notification"
    const val ALARM = "alarm"
}

@Entity(tableName = "alert", primaryKeys = ["start", "end"])
data class AlertItem(
    var id: Long,
    var alertType: String,
    var start: Long,
    var end: Long,
    @Ignore
    var event: String = "",
    @Ignore
    var description: String = "",
    @Ignore
    val senderName: String? = "",
    @Ignore
    val tags: List<String> = listOf()
) : Serializable {
    @Ignore
    var pendingIntent: PendingIntent? = null

    constructor(
        id: Long,
        alertType: String,
        start: Long,
        end: Long
    ) : this(id, alertType, start, end, "", "", "", listOf())
}