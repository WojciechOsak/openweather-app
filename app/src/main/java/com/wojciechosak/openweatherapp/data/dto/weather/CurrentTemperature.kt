package com.wojciechosak.openweatherapp.data.dto.weather

data class CurrentTemperature(
    val temp: Double,
    val weather: List<Weather>
)
