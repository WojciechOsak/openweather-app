package com.wojciechosak.openweatherapp.ui.main

import com.wojciechosak.openweatherapp.data.ResultWrapper
import com.wojciechosak.openweatherapp.data.dto.location.City
import com.wojciechosak.openweatherapp.data.dto.response.OpenApiResponse
import com.wojciechosak.openweatherapp.data.repository.WeatherRepository
import com.wojciechosak.openweatherapp.utils.TestCoroutineDispatchers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
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

    private lateinit var viewModel: MainFragmentViewModel

    private val repository = mock<WeatherRepository>()

    private val coroutineDispatchers = TestCoroutineDispatchers()

    private fun prepareViewModel() {
        viewModel = MainFragmentViewModel(
            repository = repository,
            coroutineDispatchers = coroutineDispatchers
        )
    }

    @Before
    fun setup() {
        Dispatchers.setMain(coroutineDispatchers.main)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should emit data on success`() = runBlocking {
        val response = mock<OpenApiResponse>()
        val responseFlow =
            MutableStateFlow<ResultWrapper<OpenApiResponse>>(ResultWrapper.Success(response))
        whenever(repository.fetchCityWeather(City.LONDON)).doReturn(responseFlow)

        prepareViewModel()

        assertEquals(response, viewModel.data.filterNotNull().first())
    }

    @Test
    fun `should emit general error when network fail`() = runBlocking {
        val responseFlow =
            MutableStateFlow<ResultWrapper<OpenApiResponse>>(ResultWrapper.Error.Network)
        whenever(repository.fetchCityWeather(City.LONDON)).doReturn(responseFlow)

        prepareViewModel()
        assertTrue {
            viewModel.errorFlow.filterNotNull().first() is MainFragmentViewModel.Error.General
        }
    }

    @Test
    fun `should emit general error when generic error thrown`() = runBlocking {
        val responseFlow =
            MutableStateFlow<ResultWrapper<OpenApiResponse>>(ResultWrapper.Error.Generic())
        whenever(repository.fetchCityWeather(City.LONDON)).doReturn(responseFlow)

        prepareViewModel()
        assertTrue {
            viewModel.errorFlow.filterNotNull().first() is MainFragmentViewModel.Error.General
        }
    }
}
