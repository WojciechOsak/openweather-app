package com.wojciechosak.openweatherapp.di

import com.wojciechosak.openweatherapp.data.repository.WeatherRepository
import org.koin.dsl.module

val repositoriesModule = module {
    single { WeatherRepository(get()) }
}
