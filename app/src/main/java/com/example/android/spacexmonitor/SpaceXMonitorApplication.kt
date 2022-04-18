package com.example.android.spacexmonitor

import android.app.Application
import com.example.android.spacexmonitor.di.AppComponent
import com.example.android.spacexmonitor.di.*
import timber.log.Timber

open class SpaceXMonitorApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
