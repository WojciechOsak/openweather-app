package com.wojciechosak.openweatherapp.data.repository

import com.wojciechosak.openweatherapp.data.ResultWrapper
import com.wojciechosak.openweatherapp.data.dto.location.City
import com.wojciechosak.openweatherapp.data.dto.response.OpenApiResponse
import com.wojciechosak.openweatherapp.data.service.OneCallService
import com.wojciechosak.openweatherapp.utils.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class WeatherRepository(private val service: OneCallService) {

    fun fetchCityWeather(city: City): Flow<ResultWrapper<OpenApiResponse>> {
        Timber.d("Fetch weather for city = $city")
        return safeApiCall(dispatcher = Dispatchers.IO) {
            service.fetchData(
                lat = city.lat,
                lon = city.lon
            ).apply {
                timestamp = System.currentTimeMillis()
            }
        }
    }
}
