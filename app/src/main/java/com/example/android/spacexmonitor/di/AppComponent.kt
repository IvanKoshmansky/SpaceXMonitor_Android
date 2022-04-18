package com.example.android.spacexmonitor.di

import android.app.Application
import com.example.android.spacexmonitor.main.MainComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

// Use @BindsInstance for objects that are constructed outside of the graph (e.g. instances of Context).

@Singleton
@Component(modules = [DataSourcesModuleProvides::class, DataSourcesModuleBinds::class, AppSubcomponents::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }

    fun mainComponent(): MainComponent.Factory
}
