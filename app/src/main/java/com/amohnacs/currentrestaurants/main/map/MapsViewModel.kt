package com.amohnacs.currentrestaurants.main.map

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amohnacs.currentrestaurants.common.MappingHelper
import com.amohnacs.currentrestaurants.domain.BusinessSearchRepository
import com.amohnacs.currentrestaurants.main.MainViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import com.amohnacs.currentrestaurants.main.MainViewModel.MapDataSource.ClickedDataSource.*
import com.amohnacs.currentrestaurants.model.*

class MapsViewModel @Inject constructor(
    private val mainViewModel: MainViewModel,
    private val businessSearchRepository: BusinessSearchRepository
): ViewModel() {

    val business = MutableLiveData<Business>()
    val errorEvent = MutableLiveData<String>()

    fun getBusinessDetails() {
        // TODO: 12/17/20 additional logic to check if it's a places or yelpId
        val selectedId = mainViewModel.currentDataSource.selectedBusinessId
        val selectedDataSource = mainViewModel.currentDataSource.datasource
        if (selectedId != null) {
            if (selectedDataSource == YELP_QL) {
                getYelpBusiness(selectedId)
            } else {
                getPlacesBusiness(selectedId)
            }
        } else {
            errorEvent.value = "business ID is missing for some reason...go back!"
        }
    }

    @SuppressLint("CheckResult")
    private fun getPlacesBusiness(id: String) {
        businessSearchRepository.getBusinessDetailsWithPlaceId(id)
            .map { placeSearchResponse ->
                MappingHelper.mapBusinessResultToBusiness(placeSearchResponse.result)
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                business.value = it
            },
            {
                errorEvent.value = it.message
            })
    }

    @SuppressLint("CheckResult")
    private fun getYelpBusiness(id: String) {
        businessSearchRepository.getBusinessDetailsWithId(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    if (response.hasErrors()) {
                        errorEvent.value = "response has errors"
                    } else {
                        val businessResponse = response.data?.business
                        business.value = Business(
                            id = businessResponse?.id ?: "",
                            name = businessResponse?.name ?: "",
                            displayPhone = businessResponse?.display_phone,
                            reviewCount = businessResponse?.review_count,
                            price = businessResponse?.price ?: "",
                            rating = businessResponse?.rating ?: 0.0,
                            coordinates = YelpCoordinates(
                                businessResponse?.coordinates?.latitude ?: 0.0,
                                businessResponse?.coordinates?.longitude ?: 0.0
                            ),
                            photos = businessResponse?.photos ?: emptyList<String>(),
                            category = YelpCategory(
                                businessResponse?.categories?.get(0)?.title ?: "No Category"
                            ),
                            location = YelpLocation(businessResponse?.location?.formatted_address.toString())
                        )
                    }
                },
                {
                    errorEvent.value = it.message
                })
    }
}