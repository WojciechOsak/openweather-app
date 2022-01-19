package com.wojciechosak.openweatherapp.data.service

import com.wojciechosak.openweatherapp.BuildConfig
import com.wojciechosak.openweatherapp.data.dto.response.OpenApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

private const val DEFAULT_EXCLUDE_OPTIONS = "minutely,alerts,hourly"
private const val DEFAULT_UNITS = "metric"

/**
 * OpenWeatherApi provides OneCall API which we can use for 8 days forecast with current weather info.
 */
interface OneCallService {

    @GET("onecall")
    suspend fun fetchData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appId") appId: String = BuildConfig.API_KEY,
        @Query("exclude") days: String = DEFAULT_EXCLUDE_OPTIONS,
        @Query("units") units: String = DEFAULT_UNITS,
    ): OpenApiResponse
}
