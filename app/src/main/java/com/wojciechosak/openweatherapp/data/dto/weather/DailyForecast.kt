package com.wojciechosak.openweatherapp.data.dto.weather

import com.squareup.moshi.Json

data class DailyForecast(
    @field:Json(name = "dt")
    val date: Long,
    val humidity: Int, // % value
    val clouds: Int, // cloudiness %
    val temp: Temperature,
)
