package com.wojciechosak.openweatherapp.data.dto.location

import com.wojciechosak.openweatherapp.R

enum class City(
    val lat: Double,
    val lon: Double,
    val nameResource: Int
) {
    LONDON(lat = 51.507351, lon = -0.127758, R.string.london),
    WARSAW(lat = 52.229675, lon = 21.012230, R.string.warsaw),
    TOKYO(lat = 35.689487, lon = 139.691711, R.string.tokyo),
    NEW_YORK(lat = 40.730610, lon = -73.935242, R.string.new_york)
}
