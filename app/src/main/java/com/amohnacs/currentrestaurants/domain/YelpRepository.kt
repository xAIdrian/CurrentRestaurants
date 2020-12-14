package com.amohnacs.currentrestaurants.domain

import com.apollographql.apollo.api.Response
import com.apollographql.apollo.rx2.Rx2Apollo
import io.reactivex.Observable
import yelpQL.BusinessDetailsQuery
import yelpQL.SearchQuery
import javax.inject.Inject

class YelpRepository @Inject constructor(
    private val yelpService: YelpApolloService
) {
    
    fun getBurritoSearchResults(
        latitude: Double,
        longitude: Double
    ): Observable<Response<SearchQuery.Data>> =
        Rx2Apollo.from(
            yelpService.search(
                BURRITO_RADIUS,
                latitude,
                longitude,
                0,
                BURRITO_QUERY_TERM
            )
        )

    fun getBusinessDetailsWithId(id: String): Observable<Response<BusinessDetailsQuery.Data>> =
        Rx2Apollo.from(yelpService.businessDetails(id))

    companion object {
        const val BURRITO_RADIUS = 19200.0
        const val BURRITO_QUERY_TERM = "burrito"
    }
}