package com.amohnacs.currentrestaurants.dagger

import android.app.Application
import android.content.Context
import com.amohnacs.currentrestaurants.common.LocationManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {

    @Provides
    @Singleton
    fun providesApplication(): Application = application

    @Provides
    @Singleton
    fun providesApplicationContext(): Context = application

    @Provides
    @Singleton
    fun provideLocationManager(context: Context) = LocationManager(context)
}