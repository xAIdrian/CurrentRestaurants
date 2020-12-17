package com.amohnacs.currentrestaurants.domain.yelpQL

import android.annotation.SuppressLint
import androidx.paging.rxjava2.RxPagingSource
import com.amohnacs.currentrestaurants.model.Business
import com.amohnacs.currentrestaurants.model.YelpCategory
import com.amohnacs.currentrestaurants.model.YelpCoordinates
import com.amohnacs.currentrestaurants.model.YelpLocation
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.rx2.Rx2Apollo
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import yelpQL.SearchQuery

class SearchPagingSource(
    private val yelpService: YelpApolloService,
    private val searchLatitude: Double,
    private val searchLongitude: Double,
    private val radius: Double,
    private val term: String
) : RxPagingSource<Int, Business>() {
    /**
     * Loading API for [PagingSource].
     *
     * Implement this method to trigger your async load (e.g. from database or network).
     */
    @SuppressLint("CheckResult")
    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Business>> {
        val pagingOffsetKey = params.key ?: STARTING_OFFSET_VALUE
        return getBurritoSearchResults(searchLatitude, searchLongitude, radius, term)
            .subscribeOn(Schedulers.io())
            .map { response ->
                val businessesResponse = response.data?.search?.business
                val businessList = ArrayList<Business>()
                businessesResponse?.forEach {
                    businessList.add(
                        Business(
                            id = it?.id ?: "",
                            name = it?.name ?: "",
                            price = it?.price ?: "",
                            rating = it?.rating ?: 0.0,
                            coordinates = YelpCoordinates(
                                it?.coordinates?.latitude ?: 0.0,
                                it?.coordinates?.longitude ?: 0.0
                            ),
                            photos = it?.photos ?: emptyList<String>(),
                            category = YelpCategory(
                                it?.categories?.get(0)?.title ?: "No Category"
                            ),
                            location = YelpLocation(it?.location?.formatted_address.toString())
                        )
                    )
                }
                businessList
            }
            .map { businesses -> toLoadResult(businesses, pagingOffsetKey) }
            .onErrorReturn { LoadResult.Error(it) }
    }

    private fun getBurritoSearchResults(
        latitude: Double,
        longitude: Double,
        radius: Double,
        term: String
    ): Single<Response<SearchQuery.Data>> =
        Rx2Apollo.from(
            yelpService.search(
                radius,
                latitude,
                longitude,
                0,
                term
            )
        ).firstOrError()

    private fun toLoadResult(businesses: ArrayList<Business>, pagingOffsetKey: Int): LoadResult<Int, Business> {
        return LoadResult.Page(
            data = businesses,
            prevKey = if (pagingOffsetKey == STARTING_OFFSET_VALUE) null else pagingOffsetKey - PAGE_SIZE_OFFSET_VALUE,
            nextKey = if (businesses.isEmpty()) null else pagingOffsetKey + PAGE_SIZE_OFFSET_VALUE
        )
    }

    companion object {
        private const val STARTING_OFFSET_VALUE = 0
        internal const val PAGE_SIZE_OFFSET_VALUE = 20
    }
}