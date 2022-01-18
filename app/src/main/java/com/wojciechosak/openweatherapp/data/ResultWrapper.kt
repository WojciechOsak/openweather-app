package com.wojciechosak.openweatherapp.data

import com.wojciechosak.openweatherapp.utils.ErrorResponse

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()

    sealed class Error : ResultWrapper<Nothing>() {
        data class Generic(val code: Int? = null, val error: ErrorResponse? = null) : Error()
        object Network : Error()
    }
}
