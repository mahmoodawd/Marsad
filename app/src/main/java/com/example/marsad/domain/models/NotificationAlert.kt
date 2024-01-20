package com.example.marsad.domain.models

data class NotificationAlert(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val firesAt: Int = 0,
    val endsAt: Int = 0,
    val type: String = "",
) {
    fun isMatchingWith(start: Int, end: Int): Boolean =
        firesAt in start..end || endsAt in start..end
}

object AlertType {
    const val NOTIFICATION = "notification"
    const val ALARM = "alarm"
}