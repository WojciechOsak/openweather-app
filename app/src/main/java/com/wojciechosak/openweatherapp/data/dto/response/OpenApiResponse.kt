package com.wojciechosak.openweatherapp.data.dto.response

import com.wojciechosak.openweatherapp.data.dto.weather.DailyForecast

data class OpenApiResponse(
    val daily: List<DailyForecast>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int
)
