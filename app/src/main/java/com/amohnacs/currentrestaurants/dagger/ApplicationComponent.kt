package com.amohnacs.currentrestaurants.dagger

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, SubcomponentsModule::class])
interface ApplicationComponent {
    fun mainComponent(): MainComponent.Factory
}