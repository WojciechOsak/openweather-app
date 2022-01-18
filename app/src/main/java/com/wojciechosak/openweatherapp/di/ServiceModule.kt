package com.wojciechosak.openweatherapp.di

import com.wojciechosak.openweatherapp.data.service.OneCallService
import org.koin.dsl.module
import retrofit2.Retrofit

val servicesModule = module {
    single { get<Retrofit>().create(OneCallService::class.java) }
}
