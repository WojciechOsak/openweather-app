package com.wojciechosak.openweatherapp.data.dto.weather

import com.squareup.moshi.Json

data class DailyForecast(
    @Json(name = "dt")
    val date: Long,
    val humidity: Int,
    val pressure: Int,
    val temp: Temperature,
    val weather: List<Weather>
)
