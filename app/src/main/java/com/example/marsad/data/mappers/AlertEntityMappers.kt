package com.example.marsad.data.mappers

import com.example.marsad.data.database.entities.AlertEntity
import com.example.marsad.domain.models.Alert

fun AlertEntity.toAlert(): Alert = Alert(
    id = id,
    lat = lat,
    lon = lon,
    city = city,
    startTime = start,
    endTime = end,
    type = type
)

fun Alert.toAlertEntity(): AlertEntity = AlertEntity(
    id = id,
    lat = lat,
    lon = lon,
    city = city,
    start = startTime,
    end = endTime,
    type = type
)