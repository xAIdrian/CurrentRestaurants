package com.amohnacs.currentrestaurants.main.places

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amohnacs.currentrestaurants.R
import com.amohnacs.currentrestaurants.databinding.FragmentPlacesItemBinding
import com.amohnacs.currentrestaurants.model.Business

class PlacesAdapter(
    private val viewModel: PlacesViewModel
): PagingDataAdapter<Business, RecyclerView.ViewHolder>(COMPARATOR_DIFF) {

    private var binding: FragmentPlacesItemBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = FragmentPlacesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding!!)
    }

    inner class ViewHolder(
        private val binding: FragmentPlacesItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(business: Business) {
            binding.name.text = business.name
            binding.address.text = business.location?.address
            binding.category.text = binding.root.context.getString(
                R.string.formatted_price,
                business.price,
                business.category.title
            )
            binding.cardView.setOnClickListener {
                viewModel.businessSelected(business)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let { (holder as ViewHolder).bind(it) }
    }

    companion object {
        val COMPARATOR_DIFF = object : DiffUtil.ItemCallback<Business>() {
            override fun areItemsTheSame(oldItem: Business, newItem: Business): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Business, newItem: Business): Boolean {
                return oldItem == newItem
            }
        }
    }
}