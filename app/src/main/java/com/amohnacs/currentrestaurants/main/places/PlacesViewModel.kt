package com.amohnacs.currentrestaurants.main.places

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.amohnacs.currentrestaurants.R
import com.amohnacs.currentrestaurants.common.LocationManager
import com.amohnacs.currentrestaurants.common.MappingHelper
import com.amohnacs.currentrestaurants.domain.BusinessSearchRepository
import com.amohnacs.currentrestaurants.main.MainViewModel
import com.amohnacs.currentrestaurants.model.Business
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import com.amohnacs.currentrestaurants.main.MainViewModel.MapDataSource.ClickedDataSource.*

class PlacesViewModel @Inject constructor(
    private val locationManager: LocationManager,
    private val mainViewModel: MainViewModel,
    private val businessSearchRepository: BusinessSearchRepository
) : ViewModel() {

    val navigateEvent = MutableLiveData<@androidx.annotation.NavigationRes Int>()
    val errorEvent = MutableLiveData<String>()
    val emptyEvent = MutableLiveData<String>()

    val placesBurritoLiveData = MutableLiveData<List<Business>>()
    val isShowingYelpQlLiveDataSource = MutableLiveData<Boolean>(true)
    val updatePagingDataLiveData = MutableLiveData<PagingData<Business>>()

    @SuppressLint("CheckResult") //for lint error of not using result of doOnError this is delegated
    fun getBurritoPlacesFromYelp() {
        locationManager.getUsersLastLocation()
            .flatMapObservable {
                businessSearchRepository.getBurritoSearch(
                    it.latitude,
                    it.longitude
                )
            }.doOnError {
                errorEvent.value = it.message
            }.subscribe(
                { pagingData ->
                    updatePagingDataLiveData.value = pagingData
                }, {
                    errorEvent.value = it.message
                })
    }

    fun businessSelected(id: String) {
        mainViewModel.currentDataSource.selectedBusinessId = id
        navigateEvent.value = R.id.mapsFragment
    }

    @SuppressLint("CheckResult")
    fun getBurritoPlacesFromGoogle() {
        locationManager.getUsersLastLocation()
            .flatMapObservable {
                businessSearchRepository.getBurritoSearchPlaces(
                    it.latitude,
                    it.longitude
                )
            }.toList()
            .map { businessResultsResponse ->
                val businessList = ArrayList<Business>()
                businessResultsResponse.forEach {
                    val businessResult = it.result
                    businessList.add(MappingHelper.mapBusinessResultToBusiness(businessResult))
                }
                businessList
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                placesBurritoLiveData.value = it
            }, {
                errorEvent.value = it.message
            })
    }

    fun loadFromDataSource(needNewCurrentDataSource: Boolean = true) {
        // update datasource in main for persistence
        val newCurrentDataSource = if (needNewCurrentDataSource) {
            mainViewModel.currentDataSource.updateToNewCurrentDataSource()
        } else {
            mainViewModel.currentDataSource
        }
        isShowingYelpQlLiveDataSource.value = if (newCurrentDataSource.datasource == YELP_QL) {
            getBurritoPlacesFromYelp()
            true
        } else {
            getBurritoPlacesFromGoogle()
            false
        }
    }
}