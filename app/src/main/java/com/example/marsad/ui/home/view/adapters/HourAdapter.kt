package com.example.marsad.ui.home.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marsad.data.network.HourlyWeather
import com.example.marsad.databinding.HourItemBinding
import com.example.marsad.ui.utils.UnitsUtils
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*


class HourAdapter(hourList: List<HourlyWeather>, val context: Context) :
    RecyclerView.Adapter<HourAdapter.ViewHolder>() {
    var hours = hourList
        set(value) {
            notifyDataSetChanged()
            field = value
        }

    lateinit var hourItemBinding: HourItemBinding

    inner class ViewHolder(var binding: HourItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        hourItemBinding = HourItemBinding.inflate(inflater, parent, false)
        return ViewHolder(hourItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentHourItem = hours[position]
        val hour =
            SimpleDateFormat("h a", Locale.getDefault()).format(currentHourItem.dt * 1000)
        val iconUrl = "https://openweathermap.org/img/wn/${currentHourItem.weather[0].icon}@2x.png"
        holder.binding.hourTv.text = hour
        holder.binding.hourTempTv.text =
            UnitsUtils.getTempRepresentation(context, currentHourItem.temp, false)
        Picasso.get().load(iconUrl).into(hourItemBinding.hourTempIcon)
    }

    override fun getItemCount(): Int = hours.size

}