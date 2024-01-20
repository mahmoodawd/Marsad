package com.example.marsad.ui.favorites.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marsad.databinding.LocationItemBinding
import com.example.marsad.domain.models.FavoriteLocation

class LocationItemAdapter(
    locationsItems: List<FavoriteLocation>,
    private val onItemClick: (FavoriteLocation) -> Unit,
) :
    RecyclerView.Adapter<LocationItemAdapter.ViewHolder>() {
    var locations = locationsItems
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    lateinit var binding: LocationItemBinding

    inner class ViewHolder(var binding: LocationItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = LocationItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentLocation = locations[position]

        holder.binding.cityTv.text = currentLocation.city
//        holder.binding.tempTv.text =
//            UnitsUtils.getTempRepresentation(context, currentLocation.lastTemp.toDouble(), true)
//        holder.binding.dataTimeTv.text = getLocalTime()
//        holder.binding.weatherConditionTv.text = currentLocation.description
//        holder.binding.weatherIcon.setImageUrl(currentLocation.icon)

        holder.binding.locationCard.setOnClickListener {
            onItemClick(currentLocation)
        }
    }

    override fun getItemCount() = locations.size
}