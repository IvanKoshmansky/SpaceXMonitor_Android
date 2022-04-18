package com.example.android.spacexmonitor.main

import com.example.android.spacexmonitor.di.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface MainComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: OverviewFragment)
    fun inject(fragment: DetailFragment)
}
