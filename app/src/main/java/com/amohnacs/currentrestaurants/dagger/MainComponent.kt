package com.amohnacs.currentrestaurants.dagger

import com.amohnacs.currentrestaurants.main.MainActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface MainComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }
    fun inject(mainActivity: MainActivity)
}