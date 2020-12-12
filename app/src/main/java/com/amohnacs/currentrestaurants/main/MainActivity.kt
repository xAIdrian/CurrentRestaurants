package com.amohnacs.currentrestaurants.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amohnacs.currentrestaurants.CurrentApp
import com.amohnacs.currentrestaurants.R
import com.amohnacs.currentrestaurants.dagger.MainComponent

class MainActivity : AppCompatActivity() {

    lateinit var mainComponent: MainComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        initDaggerGraph()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun initDaggerGraph() {
        mainComponent = (applicationContext as CurrentApp).appComponent.mainComponent().create()
        mainComponent.inject(this)
    }
}