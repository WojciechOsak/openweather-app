package com.wojciechosak.openweatherapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wojciechosak.openweatherapp.data.ResultWrapper
import com.wojciechosak.openweatherapp.data.dto.location.City
import com.wojciechosak.openweatherapp.data.dto.response.OpenApiResponse
import com.wojciechosak.openweatherapp.data.repository.WeatherRepository
import com.wojciechosak.openweatherapp.di.CoroutineDispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainFragmentViewModel(
    private val repository: WeatherRepository,
    private val coroutineDispatchers: CoroutineDispatchers
) : ViewModel() {
    private val _data: MutableStateFlow<OpenApiResponse?> = MutableStateFlow(null)
    val data = _data.asStateFlow()

    private val _city = MutableStateFlow(City.LONDON)
    val cityFlow = _city.asStateFlow()

    private val _error = MutableSharedFlow<Error>()
    val errorFlow = _error.asSharedFlow()

    private var fetchJob: Job? = null

    init {
        loadData()
    }

    fun loadData() {
        fetchJob?.cancel()
        fetchJob = repository.fetchCityWeather(cityFlow.value)
            .onEach {
                when (it) {
                    is ResultWrapper.Success -> {
                        _data.emit(it.value)
                    }
                    is ResultWrapper.Error -> {
                        _error.emit(Error.General)
                    }
                }
            }
            .flowOn(coroutineDispatchers.io)
            .launchIn(viewModelScope)
    }

    sealed class Error {
        object General : Error()
    }
}
