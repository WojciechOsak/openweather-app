package com.wojciechosak.openweatherapp.ui.main

import app.cash.turbine.test
import com.wojciechosak.openweatherapp.data.ResultWrapper
import com.wojciechosak.openweatherapp.data.dto.location.City
import com.wojciechosak.openweatherapp.data.dto.response.OpenApiResponse
import com.wojciechosak.openweatherapp.data.repository.WeatherRepository
import com.wojciechosak.openweatherapp.utils.TestCoroutineDispatchers
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(JUnit4::class)
class MainFragmentViewModelTest {

    private val coroutineDispatchers = TestCoroutineDispatchers()

    private val repository = mock<WeatherRepository>()

    @Test
    fun `should emit data on success`() = runBlocking {
        val response = mock<OpenApiResponse>()
        whenever(repository.fetchCityWeather(City.LONDON)).doReturn(ResultWrapper.Success(response))

        val viewModel = prepareViewModel()
        viewModel.state
            .drop(1)
            .test {
                viewModel.loadData()
                assertEquals(response, awaitItem().weatherData)
            }
    }

    @Test
    fun `should emit general error when network fail`() = runBlocking {
        whenever(repository.fetchCityWeather(City.LONDON)).doReturn(ResultWrapper.Error.Network)

        val viewModel = prepareViewModel()
        viewModel.state
            .drop(1)
            .test {
                viewModel.loadData()
                assertTrue(awaitItem().error is MainFragmentViewModel.Error.General)
            }
    }

    @Test
    fun `should emit general error when generic error thrown`() = runBlocking {
        whenever(repository.fetchCityWeather(City.LONDON)).doReturn(ResultWrapper.Error.Generic())

        val viewModel = prepareViewModel()
        viewModel.state
            .drop(1)
            .test {
                viewModel.loadData()
                assertTrue(awaitItem().error is MainFragmentViewModel.Error.General)
            }
    }

    private fun prepareViewModel(): MainFragmentViewModel {
        return MainFragmentViewModel(
            repository = repository,
            coroutineDispatchers = coroutineDispatchers
        )
    }
}
