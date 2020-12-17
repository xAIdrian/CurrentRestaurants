package com.amohnacs.currentrestaurants.main.places

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amohnacs.currentrestaurants.R
import com.amohnacs.currentrestaurants.databinding.FragmentPlacesItemBinding
import com.amohnacs.currentrestaurants.model.Business
import com.amohnacs.currentrestaurants.model.BusinessResult

class PlacesAdapter(
    private val viewModel: PlacesViewModel
) : RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {

    private var binding: FragmentPlacesItemBinding? = null
    private var values: List<Business> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = FragmentPlacesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding!!)
    }

    @SuppressLint("DefaultLocale")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.binding.name.text = item.name
        holder.binding.address.text = item.location?.address
        holder.binding.category.text = holder.binding.root.context.getString(
            R.string.formatted_price,
            item.price ?: "?",
            item.category?.title?.capitalize()
        )
        holder.binding.cardView.setOnClickListener {
            viewModel.businessSelected(item)
        }
    }

    override fun getItemCount(): Int = values.size

    fun updateBusinesses(newBusinesses: List<Business>) {
        this.values = newBusinesses
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: FragmentPlacesItemBinding) : RecyclerView.ViewHolder(binding.root)
}