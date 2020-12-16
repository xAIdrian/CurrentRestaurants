package com.amohnacs.currentrestaurants.domain.yelpQL

import com.apollographql.apollo.ApolloQueryCall
import yelpQL.BusinessDetailsQuery
import yelpQL.SearchQuery
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class YelpApolloService @Inject constructor(
    private val yelpApolloClient: YelpApolloClient
) {
    fun search(
        radius: Double,
        latitude: Double,
        longitude: Double,
        offset: Int,
        queryTerm: String
    ): ApolloQueryCall<SearchQuery.Data> =
        yelpApolloClient.apolloClient.query(
            SearchQuery(
                radius,
                latitude,
                longitude,
                offset,
                queryTerm
            )
        )

    fun businessDetails(businessId: String): ApolloQueryCall<BusinessDetailsQuery.Data> =
        yelpApolloClient.apolloClient.query(BusinessDetailsQuery(businessId))
}