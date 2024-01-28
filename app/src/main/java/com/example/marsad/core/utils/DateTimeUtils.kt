package com.example.marsad.core.utils

import androidx.appcompat.app.AppCompatDelegate
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Various methods to get readable texts from date and time objects
 */

private const val FULL_DATE_TIME_PATTERN = "EEE, d MMM h:mm a"
private const val MID_DATE_TIME_PATTERN = "d MMM h:mm a"
private const val SHORT_TIME_PATTERN = "h:mm a"
private const val HOUR_PATTERN = "h a"
private const val WEEK_DAY_PATTERN = "EEEE"

fun getTimeFromOffset(offset: Int): String {
    val seconds = offset.toLong()
    val instant = Instant.now().plusSeconds(seconds)
    val dateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC)
    return DateTimeFormatter.ofPattern(FULL_DATE_TIME_PATTERN)
        .withLocale(getCurrentLocale())
        .format(dateTime)
}


fun getDateAndTime(time: Long): String {
    val dateFormat = SimpleDateFormat(MID_DATE_TIME_PATTERN, getCurrentLocale())
    return dateFormat.format(time * 1000)
}


fun Int.getShortTime(): String {
    val dateFormat = SimpleDateFormat(SHORT_TIME_PATTERN, getCurrentLocale())
    return dateFormat.format(this * 1000)
}

fun Int.getHour(): String {
    val dateFormat = SimpleDateFormat(HOUR_PATTERN, getCurrentLocale())
    return dateFormat.format(this * 1000)
}


fun getWeekday(timestamp: Long): String {
    val dateFromTimestamp =
        Instant.ofEpochSecond(timestamp).atZone(ZoneId.systemDefault()).toLocalDate()

    val dayOfWeek = dateFromTimestamp.dayOfWeek
    return DateTimeFormatter.ofPattern(WEEK_DAY_PATTERN, getCurrentLocale()).format(dayOfWeek)

}


fun getCurrentLocale(): Locale =
    AppCompatDelegate.getApplicationLocales()[0] ?: Locale.getDefault()


fun getCountryName(countryCode: String, locale: Locale = getCurrentLocale()): String =
    Locale(locale.language, countryCode).getDisplayCountry(locale)



