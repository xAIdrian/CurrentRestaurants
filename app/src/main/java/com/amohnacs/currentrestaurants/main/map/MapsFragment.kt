package com.amohnacs.currentrestaurants.main.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class MapsFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelFactory<MapsViewModel>
    private lateinit var viewModel: MapsViewModel

    private var binding: FragmentMapsBinding? = null
    private var mapFragment: SupportMapFragment? = null

    private var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>? = null

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
        binding?.bottomSheetLayout?.bottomSheet?.let {
            bottomSheetBehavior = BottomSheetBehavior.from(it)
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                /**
                 * Callback for handling the [OnBackPressedDispatcher.onBackPressed] event.
                 */
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_mapsFragment_to_placesFragment)
                }
            }
        )
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
            setBottomSheet(it)
            addMarkerToMap(it)
        })
        viewModel.errorEvent.observe(viewLifecycleOwner, Observer {
            binding?.root?.let { it1 -> Snackbar.make(it1, it, Snackbar.LENGTH_LONG).show() }
        })
    }

    private fun setBottomSheet(dialogBusiness: Business?) {
        val bottomSheet = binding?.bottomSheetLayout
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheet?.name?.text = dialogBusiness?.name
        bottomSheet?.address?.text = dialogBusiness?.location?.address
        bottomSheet?.phone?.text = dialogBusiness?.displayPhone
        bottomSheet?.category?.text = activity?.getString(
            R.string.formatted_price,
            dialogBusiness?.price,
            dialogBusiness?.category?.title
        )
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