package com.amohnacs.currentrestaurants.main

import androidx.lifecycle.ViewModel
import com.amohnacs.currentrestaurants.common.LocationManager
import com.amohnacs.currentrestaurants.dagger.scopes.ActivityScope
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

class MainViewModel @Inject constructor(
    private val locationManager: LocationManager
): ViewModel() {

    /**
     * Fetching to get the user's location that we will use throughout the applicaiton
     */
    fun getUserLocation(): Disposable =
        locationManager.getUsersLastLocation(true)
            .subscribeOn(Schedulers.io())
            .subscribe()
}