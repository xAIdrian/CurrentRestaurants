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
) : ViewModel() {

    /**
     * singular object associated with this MainViewModel instance monitors the datasource being
     * currently viewed and the identifier of the selected business.
     * Working simultaneously with 2 datasources we need to be aware that using an ID with an
     * incorrect datasource will result in errors
     */
    val currentDataSource: MapDataSource = MapDataSource(datasource = MapDataSource.ClickedDataSource.YELP_QL)

    /**
     * Fetching to get the user's location that we will use throughout the applicaiton
     */
    fun getUserLocation(): Disposable =
        locationManager.getUsersLastLocation(true)
            .subscribeOn(Schedulers.io())
            .subscribe()

    data class MapDataSource(
        var selectedBusinessId: String? = null,
        var datasource: ClickedDataSource? = null
    ) {
        fun updateToNewCurrentDataSource(): MapDataSource =
            if (datasource == ClickedDataSource.YELP_QL) {
                datasource = ClickedDataSource.PLACES
                this
            } else {
                datasource = ClickedDataSource.YELP_QL
                this
            }

        enum class ClickedDataSource {
            YELP_QL,
            PLACES
        }
    }
}