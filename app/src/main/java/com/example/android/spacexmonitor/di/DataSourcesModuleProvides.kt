package com.example.android.spacexmonitor.di

import android.app.Application
import android.content.Context
import com.example.android.spacexmonitor.localdb.*
import com.example.android.spacexmonitor.webservice.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// модуль Hilt для проведения зависимости на интерфейс, причем реализация получается через вызов билдера
// в отличие от Dagger в Hilt нужно указывать из какого класса (компонента) android будет
// использоваться данный модуль
// SingletonComponent::class соответствует уровню приложения

@Module
@InstallIn(SingletonComponent::class)
object DataSourcesModuleProvides {
    @Singleton
    @Provides
    fun provideLocalDatabase(@ApplicationContext appContext: Context): LocalDatabase {
        return getDatabase(appContext)
    }
    @Singleton
    @Provides
    fun provideSpaceXApiService(): SpaceXApiService {
        return getSpaceXApiService()
    }
}
