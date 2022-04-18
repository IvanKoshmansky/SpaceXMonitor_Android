package com.example.android.spacexmonitor.di

import com.example.android.spacexmonitor.webservice.*
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

// в случае зависимости на интерфейс используются модули
// @Provides - во время исполнения вызвать функцию, которая возвращает объект, реализующий интерфейс
// @Binds - подключить нужную реализацию интерфейса на этапе компиляции (более оптимально)

@Module
abstract class DataSourcesModuleBinds {

    @Binds
    @Singleton
    abstract fun bindsRemoteDataSource(remoteDataSource: RemoteDataSource): IRemoteDataSource
}
