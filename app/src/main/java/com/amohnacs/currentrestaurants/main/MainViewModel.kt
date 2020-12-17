package com.amohnacs.currentrestaurants.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amohnacs.currentrestaurants.common.LocationManager
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainViewModel @Inject constructor(
    private val locationManager: LocationManager
): ViewModel() {

    var selectedBusinessId: String? = null
    // used to track current data source for persistence across fragments for navigation
    var isShowingYelpQlDataSource = true

    /**
     * Fetching to get the user's location that we will use throughout the applicaiton
     */
    fun getUserLocation(): Disposable =
        locationManager.getUsersLastLocation(true)
            .subscribeOn(Schedulers.io())
            .subscribe()
}