package com.amohnacs.currentrestaurants.main.places

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.amohnacs.currentrestaurants.common.ViewModelFactory
import com.amohnacs.currentrestaurants.databinding.FragmentPlacesBinding
import com.amohnacs.currentrestaurants.main.MainActivity
import com.amohnacs.currentrestaurants.main.MainViewModel
import javax.inject.Inject

class PlacesFragment : Fragment() {

    @Inject lateinit var factory: ViewModelFactory<PlacesViewModel>
    private lateinit var viewModel: PlacesViewModel

    private val mainViewModel: MainViewModel by activityViewModels()
//    private var binding: FragmentPlacesBinding? = null

    /**
     * Called to do initial creation of a fragment.  This is called after
     * [.onAttach] and before
     * [.onCreateView].
     *
     *
     * Note that this can be called while the fragment's activity is
     * still in the process of being created.  As such, you can not rely
     * on things like the activity's content view hierarchy being initialized
     * at this point.  If you want to do work once the activity itself is
     * created, see [.onActivityCreated].
     *
     *
     * Any restored child fragments will be created before the base
     * `Fragment.onCreate` method returns.
     *
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        (activity as MainActivity).mainComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentPlacesBinding.inflate(inflater).root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(PlacesViewModel::class.java)
        viewModel.getBurritoPlaces()
        viewModel.businesses.observe(viewLifecycleOwner, Observer {
            Log.e("Tester", it.size.toString())
        })
    }
}