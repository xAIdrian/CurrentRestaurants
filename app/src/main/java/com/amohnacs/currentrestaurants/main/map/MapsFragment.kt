package com.amohnacs.currentrestaurants.main.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.amohnacs.currentrestaurants.R
import com.amohnacs.currentrestaurants.common.ViewModelFactory
import com.amohnacs.currentrestaurants.databinding.FragmentMapsBinding
import com.amohnacs.currentrestaurants.main.MainActivity
import com.amohnacs.currentrestaurants.model.Business
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject

class MapsFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelFactory<MapsViewModel>
    private lateinit var viewModel: MapsViewModel

    private var binding: FragmentMapsBinding? = null
    private var mapFragment: SupportMapFragment? = null

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity as MainActivity).mainComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this, factory).get(MapsViewModel::class.java)
        viewModel.getBusinessDetails()
        viewModel.business.observe(viewLifecycleOwner, Observer {
            addMarkerToMap(it)
        })
    }

    private fun addMarkerToMap(business: Business?) {
        val businessLatLng = LatLng(
            business?.coordinates?.latitude ?: 0.0,
            business?.coordinates?.longitude ?: 0.0
        )
        mapFragment?.getMapAsync {
            it.addMarker(
                MarkerOptions()
                    .position(businessLatLng)
                    .title(business?.name ?: "")
            )
            it.moveCamera(CameraUpdateFactory.newLatLng(businessLatLng))
            it.animateCamera(CameraUpdateFactory.zoomTo(15.0f))
        }
    }
}