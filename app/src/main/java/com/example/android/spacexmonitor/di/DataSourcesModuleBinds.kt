package com.example.android.spacexmonitor.di

import com.example.android.spacexmonitor.webservice.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// модуль Hilt для проведения зависимости на интерфейс
// в отличие от Dagger в Hilt нужно указывать из какого класса (компонента) android будет
// использоваться данный модуль
// SingletonComponent::class соответствует уровню приложения

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourcesModuleBinds {
    @Singleton
    @Binds
    abstract fun bindRemoteDataSource(
        remoteDataSource: RemoteDataSource
    ): IRemoteDataSource
}

//@ViewModelScoped - можно использовать вместо @Singleton для того, чтобы в каждый экземпляр
//ViewModel передавался свой набор ссылок (scoped to ViewModel)
