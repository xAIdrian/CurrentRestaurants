package com.amohnacs.currentrestaurants.main.places

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.amohnacs.currentrestaurants.R
import com.amohnacs.currentrestaurants.main.MainViewModel

class PlacesFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: PlacesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_places, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel

    }

    companion object {
        fun newInstance() =
            PlacesFragment()
    }
}