package com.amohnacs.currentrestaurants.main.places

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amohnacs.currentrestaurants.common.LocationManager
import com.amohnacs.currentrestaurants.domain.YelpRepository
import com.amohnacs.currentrestaurants.main.MainViewModel
import com.amohnacs.currentrestaurants.model.Business
import com.amohnacs.currentrestaurants.model.YelpCoordinates
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PlacesViewModel @Inject constructor(
    private val locationManager: LocationManager,
    private val mainViewModel: MainViewModel,
    private val yelpRepository: YelpRepository
) : ViewModel() {

    val businesses = MutableLiveData<List<Business>>()

    @SuppressLint("CheckResult")
    fun getBurritoPlaces() {
        yelpRepository.getBurritoSearchResults(
            locationManager.lastFetchedLocation?.latitude ?: 20.0,
            locationManager.lastFetchedLocation?.longitude ?: 40.0
        ).observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.e("tester", it.toString())
            }
//        locationManager.getUsersLastLocation()
//            .flatMapObservable {
//                yelpRepository.getBurritoSearchResults(
//                    it.latitude,
//                    it.longitude
//                )
//            }.doOnError{
//                Log.e("error", it.message.toString())
//            }
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(
//                { response ->
//                    if (!response.hasErrors()) {
//                        // TODO: 12/12/20
//                        Log.e("error", "response has errors")
//                    } else {
//                        val businessList = ArrayList<Business>()
//                        response.data?.search?.business?.forEach {
//                            businessList.add(
//                                Business(
//                                    id = it?.id ?: "",
//                                    name = it?.name ?: "",
//                                    price = it?.price ?: "",
//                                    rating = it?.rating ?: 0.0,
//                                    coordinates = YelpCoordinates(
//                                        it?.coordinates?.latitude ?: 0.0,
//                                        it?.coordinates?.longitude ?: 0.0
//                                    ),
//                                    photos = it?.photos ?: emptyList<String>()
//                                )
//                            )
//                        }
//                        businesses.postValue(businessList)
//                    }
//                },
//                {
//                    // TODO: 12/12/20
//                    Log.e("error", it.message.toString())
//                }
//            )

    }
}