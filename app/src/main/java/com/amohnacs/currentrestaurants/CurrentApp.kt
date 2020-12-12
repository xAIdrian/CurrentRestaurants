package com.amohnacs.currentrestaurants

import android.app.Application
import com.amohnacs.currentrestaurants.dagger.ApplicationModule
import com.amohnacs.currentrestaurants.dagger.DaggerApplicationComponent

class CurrentApp: Application() {
    lateinit var appComponent: DaggerApplicationComponent

    override fun onCreate() {
        super.onCreate()
        this.appComponent = this.initDagger() as DaggerApplicationComponent
    }

    private fun initDagger() = DaggerApplicationComponent.builder()
        .applicationModule(ApplicationModule(this))
        .build()
}