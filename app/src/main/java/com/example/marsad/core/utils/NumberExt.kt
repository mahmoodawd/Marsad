package com.example.marsad.core.utils

import java.text.NumberFormat
import java.util.Locale

fun Double.round(decimals: Int = 2): Double = "%.${decimals}f".format(Locale.US, this).toDouble()

/**
 * Extended property to convert any number to localized one ex. 6 --> ٦
 */
val Number.localized: String
    get() = NumberFormat
        .getNumberInstance(getCurrentLocale())
        .format(this)
