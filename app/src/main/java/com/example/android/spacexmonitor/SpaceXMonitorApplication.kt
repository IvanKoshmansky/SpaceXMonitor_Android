package com.example.android.spacexmonitor

import android.app.Application
import com.example.android.spacexmonitor.di.*
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

// @HiltAndroidApp - создает AppComponent (аналог AppComponent в Dagger)

@HiltAndroidApp
open class SpaceXMonitorApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
