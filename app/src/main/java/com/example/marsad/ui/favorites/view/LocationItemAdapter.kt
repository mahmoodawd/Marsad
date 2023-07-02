package com.example.marsad.ui.favorites.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marsad.R
import com.example.marsad.data.model.SavedLocation
import com.example.marsad.databinding.LocationItemBinding
import com.example.marsad.ui.utils.UnitsUtils
import com.example.marsad.ui.utils.getLocalTime
import com.squareup.picasso.Picasso

class LocationItemAdapter(
    locationsItems: List<SavedLocation>,
    private val onItemClick: (SavedLocation) -> Unit,
    val context: Context
) :
    RecyclerView.Adapter<LocationItemAdapter.ViewHolder>() {
    var locations = locationsItems
        set(value) {
            notifyDataSetChanged()
            field = value
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
        val iconUrl = "https://openweathermap.org/img/wn/${currentLocation.icon}@2x.png"

        holder.binding.cityTv.text = currentLocation.city
        holder.binding.tempTv.text =
            UnitsUtils.getTempRepresentation(context, currentLocation.lastTemp.toDouble(), true)
        holder.binding.dataTimeTv.text =
            getLocalTime(currentLocation.lat, currentLocation.lon)
        holder.binding.weatherConditionTv.text = currentLocation.description
        Picasso.get().load(iconUrl).into(holder.binding.weatherIcon)

        holder.binding.locationCard.setOnClickListener {
            onItemClick(currentLocation)
        }
    }

    override fun getItemCount() = locations.size
}