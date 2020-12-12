package com.amohnacs.currentrestaurants.domain

import com.amohnacs.currentrestaurants.common.ResourceProvider
import com.apollographql.apollo.ApolloClient
import javax.inject.Inject

class YelpRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val resourceProvider: ResourceProvider
) {

}