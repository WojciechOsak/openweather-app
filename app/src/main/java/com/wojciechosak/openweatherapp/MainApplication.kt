package com.wojciechosak.openweatherapp

import android.app.Application
import com.wojciechosak.openweatherapp.di.networkModule
import com.wojciechosak.openweatherapp.di.repositoriesModule
import com.wojciechosak.openweatherapp.di.servicesModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initTimber()
        initKoin()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@MainApplication)
            modules(networkModule, servicesModule, repositoriesModule)
        }
    }
}
