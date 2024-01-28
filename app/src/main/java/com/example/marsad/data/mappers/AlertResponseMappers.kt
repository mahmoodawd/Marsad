package com.example.marsad.data.mappers

import com.example.marsad.domain.models.NotificationAlert
import com.example.marsad.data.network.response.Alert as AlertResponse

fun AlertResponse.toNotificationAlert(): NotificationAlert = NotificationAlert(
    firesAt = start!!,
    endsAt = end!!,
    title = event!!,
    description = description!!
)
