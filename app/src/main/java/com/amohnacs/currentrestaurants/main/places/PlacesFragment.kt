package com.amohnacs.currentrestaurants.main.places

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amohnacs.currentrestaurants.R

class PlacesFragment : Fragment() {

    companion object {
        fun newInstance() =
            PlacesFragment()
    }

    private lateinit var viewModel: PlacesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.places_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PlacesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}