package com.example.marsad.ui.weatherdetails.adapters

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Property(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    var value: String,
    @StringRes val symbol: Int,
)
