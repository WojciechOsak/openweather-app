package com.wojciechosak.openweatherapp.data.dto.location

enum class City(val lat: Double, val lon: Double) {
    LONDON(lat = 51.507351, lon = -0.127758),
    WARSAW(lat = 52.229675, lon = 21.012230),
    TOKYO(lat = 35.689487, lon = 139.691711),
    NEW_YORK(lat = 40.730610, lon = -73.935242)
}
