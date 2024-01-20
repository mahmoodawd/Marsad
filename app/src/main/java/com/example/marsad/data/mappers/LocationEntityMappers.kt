package com.example.marsad.data.mappers

import com.example.marsad.data.database.entities.LocationEntity
import com.example.marsad.domain.models.FavoriteLocation

fun LocationEntity.toFavoriteLocation(): FavoriteLocation =
    FavoriteLocation(
        lat = lat,
        lon = lon,
        city = city,
        country = country
    )

fun FavoriteLocation.toLocationEntity(): LocationEntity =
    LocationEntity(
        lat = lat,
        lon = lon,
        city = city,
        country = country,
        description = "",
        icon = "",
        lastTemp = 0,
//        offset = offset
    )