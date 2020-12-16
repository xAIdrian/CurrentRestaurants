package com.amohnacs.currentrestaurants.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.observable
import com.amohnacs.currentrestaurants.domain.yelpQL.SearchPagingSource
import com.amohnacs.currentrestaurants.domain.yelpQL.YelpApolloService
import com.amohnacs.currentrestaurants.model.Business
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.rx2.Rx2Apollo
import io.reactivex.Observable
import yelpQL.BusinessDetailsQuery
import javax.inject.Inject

class YelpRepository @Inject constructor(
    private val yelpService: YelpApolloService
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
                    longitude
                )
            }
        ).observable
    }

    fun getBusinessDetailsWithId(id: String): Observable<Response<BusinessDetailsQuery.Data>> =
        Rx2Apollo.from(yelpService.businessDetails(id))
}