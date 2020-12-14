package com.amohnacs.currentrestaurants.common

import android.content.Context
import androidx.annotation.StringRes
import javax.inject.Inject

class ResourceProvider @Inject constructor(
    private val context: Context
) {
    fun getString(@StringRes stringId: Int) = context.getString(stringId)
}