package com.example.android.spacexmonitor.di

import android.app.Application
import com.example.android.spacexmonitor.localdb.*
import com.example.android.spacexmonitor.webservice.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

// в случае зависимости на интерфейс используются модули
// @Provides - во время исполнения вызвать функцию, которая возвращает объект, реализующий интерфейс
// @Binds - подключить нужную реализацию интерфейса на этапе компиляции (более оптимально)

@Module
class DataSourcesModuleProvides {

    @Provides
    @Singleton
    fun provideLocalDatabase(application: Application): LocalDatabase {
        return getDatabase(application.applicationContext)
    }

    @Provides
    @Singleton
    fun provideSpaceXApiService(): SpaceXApiService {
        return getSpaceXApiService()
    }
}
