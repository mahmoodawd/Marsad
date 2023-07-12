package com.example.marsad.utils

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

fun getDateAndTime(time: Long): String {
    val dateFormat = SimpleDateFormat("d MMM h:mm a", UnitsUtils.getCurrentLocale())
    return dateFormat.format(time * 1000)
}
fun getDate(date: Long): String {
    val dateFormat = SimpleDateFormat("EEE dd, MMM", UnitsUtils.getCurrentLocale())
    return dateFormat.format(date)
}

fun getHour(time: Long): String {
    val dateFormat = SimpleDateFormat("h:mm a", UnitsUtils.getCurrentLocale())
    return dateFormat.format(time * 1000)
}

fun getLocalTime(lat: Double, lon: Double): String {
    val utcTime = System.currentTimeMillis()
    val timeZone = TimeZone.getTimeZone(TimeZone.getAvailableIDs()[0])
    val localTime = Date(utcTime + timeZone.getOffset(utcTime))
    val pattern = "yy-MMM-dd HH:mm"
    val sdf = android.icu.text.SimpleDateFormat(pattern, UnitsUtils.getCurrentLocale())
    return sdf.format(localTime)
}