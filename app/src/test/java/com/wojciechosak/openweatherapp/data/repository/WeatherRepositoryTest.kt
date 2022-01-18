package com.wojciechosak.openweatherapp.data.repository

import com.wojciechosak.openweatherapp.data.dto.location.City
import com.wojciechosak.openweatherapp.data.service.OneCallService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@RunWith(JUnit4::class)
class WeatherRepositoryTest {

    private lateinit var repository: WeatherRepository

    private val service = mock<OneCallService>()

    @Before
    fun setup() {
        repository = WeatherRepository(service = service)
    }

    @Test
    fun `should call api with city latitude and longitude`(): Unit = runBlocking {
        val city = City.LONDON
        repository.fetchCityWeather(city).first()

        verify(service).fetchData(
            lat = city.lat,
            lon = city.lon
        )
    }
}
