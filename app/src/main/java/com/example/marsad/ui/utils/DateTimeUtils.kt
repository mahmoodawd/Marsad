package com.example.marsad.ui.utils

import androidx.appcompat.app.AppCompatDelegate
import java.text.SimpleDateFormat
import java.time.ZoneOffset
import java.util.*

fun getFullDateAndTime(offset: Int, time: Long): String {
    val timezoneOffset = ZoneOffset.ofTotalSeconds(offset)
    val dateFormat = SimpleDateFormat("EEE, d MMM h:mm a", UnitsUtils.getCurrentLocale()).also {
        it.timeZone = TimeZone.getTimeZone(timezoneOffset)
    }
    return dateFormat.format(time * 1000)
}

fun getHour(time: Long): String {
    val dateFormat = SimpleDateFormat("h:mm a", UnitsUtils.getCurrentLocale())
    return dateFormat.format(time * 1000)
}
