package com.wojciechosak.openweatherapp.data.dto.weather

data class DailyForecast(
    val humidity: Int,
    val pressure: Int,
    val temp: Temperature,
    val weather: List<Weather>
)
