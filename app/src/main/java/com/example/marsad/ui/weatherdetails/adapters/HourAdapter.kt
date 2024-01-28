package com.example.marsad.ui.weatherdetails.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marsad.core.utils.getHour
import com.example.marsad.core.utils.localized
import com.example.marsad.core.utils.setImageUrl
import com.example.marsad.databinding.HourItemBinding
import com.example.marsad.domain.models.Hourly


class HourAdapter(var tempFactor: Double, hourList: List<Hourly>) :
    RecyclerView.Adapter<HourAdapter.ViewHolder>() {
    var hours = hourList
        set(value) {
            notifyDataSetChanged()
            field = value
        }

    private lateinit var hourItemBinding: HourItemBinding


    inner class ViewHolder(var binding: HourItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        hourItemBinding = HourItemBinding.inflate(inflater, parent, false)
        return ViewHolder(hourItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentHourWeather = hours[position]

        holder.binding.hourTv.text = currentHourWeather.hour.getHour()
        holder.binding.hourTempTv.text = currentHourWeather.temp.times(tempFactor).localized
        holder.binding.hourTempIcon.setImageUrl(currentHourWeather.icon)
    }

    override fun getItemCount(): Int = hours.size

}