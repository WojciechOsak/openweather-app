package com.wojciechosak.openweatherapp.di

import com.wojciechosak.openweatherapp.ui.main.MainFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { MainFragmentViewModel(repository = get(), coroutineDispatchers = get()) }
}
