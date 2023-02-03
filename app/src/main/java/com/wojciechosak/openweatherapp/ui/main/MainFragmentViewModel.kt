package com.wojciechosak.openweatherapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wojciechosak.openweatherapp.data.ResultWrapper
import com.wojciechosak.openweatherapp.data.dto.location.City
import com.wojciechosak.openweatherapp.data.dto.response.OpenApiResponse
import com.wojciechosak.openweatherapp.data.repository.WeatherRepository
import com.wojciechosak.openweatherapp.di.CoroutineDispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainFragmentViewModel(
    private val repository: WeatherRepository,
    private val coroutineDispatchers: CoroutineDispatchers
) : ViewModel() {

    data class ViewState(
        val city: City = City.LONDON,
        val weatherData: OpenApiResponse? = null,
        val error: Error? = null,
    )

    private val _state: MutableStateFlow<ViewState> = MutableStateFlow(ViewState())
    val state: StateFlow<ViewState> = _state

    private var fetchJob: Job? = null

    fun loadData() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch(coroutineDispatchers.io) {
            when (val data = repository.fetchCityWeather(_state.value.city)) {
                is ResultWrapper.Success -> {
                    mutateState { currentState ->
                        currentState.copy(
                            weatherData = data.value,
                            error = null
                        )
                    }
                }
                is ResultWrapper.Error -> {
                    mutateState { currentState ->
                        currentState.copy(
                            weatherData = null,
                            error = Error.General
                        )
                    }
                }
            }
        }
    }

    fun changeCurrentCity(city: City) {
        viewModelScope.launch {
            mutateState { currentState -> currentState.copy(city = city) }
            loadData()
        }
    }

    private suspend fun mutateState(newState: (ViewState) -> ViewState) {
        _state.emit(newState(_state.value))
    }

    sealed class Error {
        object General : Error()
    }
}
