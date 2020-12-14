package com.amohnacs.currentrestaurants.main.places

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.amohnacs.currentrestaurants.common.ViewModelFactory
import com.amohnacs.currentrestaurants.databinding.FragmentPlacesBinding
import com.amohnacs.currentrestaurants.main.MainActivity
import javax.inject.Inject

class PlacesFragment : Fragment() {

    @Inject lateinit var factory: ViewModelFactory<PlacesViewModel>
    private lateinit var viewModel: PlacesViewModel

    private var binding: FragmentPlacesBinding? = null
    private var placesAdapter: PlacesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity as MainActivity).mainComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlacesBinding.inflate(inflater)
        binding?.list?.layoutManager = LinearLayoutManager(context)
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(PlacesViewModel::class.java)

        placesAdapter = PlacesAdapter(viewModel)
        binding?.list?.adapter = placesAdapter

        viewModel.getBurritoPlaces()
        viewModel.businesses.observe(viewLifecycleOwner, Observer {
            placesAdapter?.updateBusinesses(it)
        })
        viewModel.navigateEvent.observe(viewLifecycleOwner, Observer {
            findNavController().navigate(it)
        })
    }
}