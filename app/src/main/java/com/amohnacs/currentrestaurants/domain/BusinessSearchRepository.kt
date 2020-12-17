package com.amohnacs.currentrestaurants.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.observable
import com.amohnacs.currentrestaurants.R
import com.amohnacs.currentrestaurants.common.ResourceProvider
import com.amohnacs.currentrestaurants.domain.googleplaces.GooglePlacesService
import com.amohnacs.currentrestaurants.domain.yelpQL.SearchPagingSource
import com.amohnacs.currentrestaurants.domain.yelpQL.YelpApolloService
import com.amohnacs.currentrestaurants.model.*
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.rx2.Rx2Apollo
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import yelpQL.BusinessDetailsQuery
import javax.inject.Inject


class BusinessSearchRepository @Inject constructor(
    private val yelpService: YelpApolloService,
    private val placesClient: GooglePlacesService,
    private val resourceProvider: ResourceProvider
) {
    fun getBurritoSearch(
        latitude: Double,
        longitude: Double
    ): Observable<PagingData<Business>> {
        return Pager(
            config = PagingConfig(
                pageSize = SearchPagingSource.PAGE_SIZE_OFFSET_VALUE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                SearchPagingSource(
                    yelpService,
                    latitude,
                    longitude,
                    TWELVE_MILE_BURRITO_RADIUS,
                    BURRITO_QUERY_TERM
                )
            }
        ).observable
    }

    fun getBusinessDetailsWithId(id: String): Observable<Response<BusinessDetailsQuery.Data>> =
        Rx2Apollo.from(yelpService.businessDetails(id))

    fun getBurritoSearchPlaces(
        latitude: Double,
        longitude: Double
    ): Observable<PlacesSearchResponse>? = placesClient.getPlacesSearchResults(
        BURRITO_QUERY_TERM,
        "$latitude, $longitude",
        BURRITO_QUERY_TERM,
        resourceProvider.getString(R.string.places_api_key)
    ).flatMap { placesSearchResponse ->
        Observable.fromIterable(placesSearchResponse.results)
            .flatMap {
                it.placeId?.let { placesId ->
                    placesClient.getPlacesBusinessDetails(
                        placesId,
                        resourceProvider.getString(R.string.places_api_key)
                    )
                }
            }
    }.subscribeOn(Schedulers.io())

    fun getBusinessDetailsWithPlaceId(placesId: String) =
        placesClient.getPlacesBusinessDetails(
            placesId,
            resourceProvider.getString(R.string.places_api_key)
        )

    companion object {
        const val TWELVE_MILE_BURRITO_RADIUS = 19200.0
        const val BURRITO_QUERY_TERM = "burrito"
    }
}