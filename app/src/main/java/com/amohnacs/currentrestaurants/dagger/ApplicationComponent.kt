package com.amohnacs.currentrestaurants.dagger

import com.amohnacs.currentrestaurants.main.MainComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, SubcomponentsModule::class])
interface ApplicationComponent {
    fun mainComponent(): MainComponent.Factory
}