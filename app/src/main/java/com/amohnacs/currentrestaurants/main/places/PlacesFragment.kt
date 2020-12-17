package com.amohnacs.currentrestaurants.main.places

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.amohnacs.currentrestaurants.R
import com.amohnacs.currentrestaurants.common.ViewModelFactory
import com.amohnacs.currentrestaurants.databinding.FragmentPlacesBinding
import com.amohnacs.currentrestaurants.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class PlacesFragment : Fragment() {

    @Inject lateinit var factory: ViewModelFactory<PlacesViewModel>
    private lateinit var viewModel: PlacesViewModel

    private var binding: FragmentPlacesBinding? = null
    private var yelpPagingAdapter: YelpPagingAdapter? = null
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
        binding?.fab?.setOnClickListener { view ->
            viewModel.loadFromDataSource()
        }
        return binding?.root
    }

    @SuppressLint("CheckResult")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(PlacesViewModel::class.java)

        yelpPagingAdapter = YelpPagingAdapter(viewModel)
        placesAdapter = PlacesAdapter(viewModel)

        viewModel.navigateEvent.observe(viewLifecycleOwner, Observer {
            findNavController().navigate(it)
        })
        viewModel.errorEvent.observe(viewLifecycleOwner, Observer {
            binding?.root?.let { it1 -> Snackbar.make(it1, it, Snackbar.LENGTH_LONG).show() }
        })
        viewModel.emptyEvent.observe(viewLifecycleOwner, Observer {
            binding?.root?.let { it1 -> Snackbar.make(it1, it, Snackbar.LENGTH_INDEFINITE).show() }
        })
        viewModel.placesBurritoLiveData.observe(viewLifecycleOwner, Observer {
            placesAdapter?.updateBusinesses(it)
        })
        viewModel.updatePagingDataLiveData.observe(viewLifecycleOwner, Observer {
            yelpPagingAdapter!!.submitData(lifecycle, it)
        })
        viewModel.isShowingYelpQlLiveDataSource.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding?.list?.adapter = yelpPagingAdapter
                binding?.fab?.text = requireActivity().getString(R.string.load_places_api)
            } else {
                binding?.list?.adapter = placesAdapter
                binding?.fab?.text = requireActivity().getString(R.string.load_yelp_api)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadFromDataSource(false)
    }
}