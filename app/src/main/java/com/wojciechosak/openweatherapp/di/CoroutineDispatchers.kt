package com.wojciechosak.openweatherapp.di

import kotlin.coroutines.CoroutineContext

/**
 * Dispatchers interface for easier unit testing
 */
interface CoroutineDispatchers {

    val main: CoroutineContext

    val io: CoroutineContext

    val default: CoroutineContext
}
