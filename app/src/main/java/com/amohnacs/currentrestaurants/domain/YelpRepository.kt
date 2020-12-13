package com.amohnacs.currentrestaurants.domain

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.rx2.Rx2Apollo
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import yelpQL.SearchQuery
import javax.inject.Inject

class YelpRepository @Inject constructor(
    private val yelpService: YelpApolloService
) {

    /**
     * Static query to get all the restaurants that serve burritos in a 12 mile radius of
     * where the user is located
     */
    fun getBurritoSearchResults(
        latitude: Double,
        longitude: Double
    ): Observable<Response<SearchQuery.Data>> {

        val query = yelpService.search(
            BURRITO_RADIUS,
            latitude,
            longitude,
            0,
            BURRITO_QUERY_TERM
        ) as ApolloCall<SearchQuery.Data>

        return Rx2Apollo.from(query).subscribeOn(Schedulers.io())
    }

    companion object {
        const val BURRITO_RADIUS = 12.0
        const val BURRITO_QUERY_TERM = "burrito"
    }
}