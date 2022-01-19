package com.wojciechosak.openweatherapp.utils

import com.wojciechosak.openweatherapp.di.CoroutineDispatchers
import kotlinx.coroutines.Dispatchers

class TestCoroutineDispatchers : CoroutineDispatchers {
    override val main = Dispatchers.Default
    override val io = Dispatchers.Default
    override val default = Dispatchers.Default
}
