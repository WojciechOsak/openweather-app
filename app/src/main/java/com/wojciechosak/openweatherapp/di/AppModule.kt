package com.wojciechosak.openweatherapp.di

import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val appModule = module {
    single { provideCoroutineDispatchers() }
}

private fun provideCoroutineDispatchers(): CoroutineDispatchers {
    return object : CoroutineDispatchers {
        override val main = Dispatchers.Main
        override val io = Dispatchers.IO
        override val default = Dispatchers.Default
    }
}
