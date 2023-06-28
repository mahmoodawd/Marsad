package com.example.marsad.ui.home.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marsad.data.network.DailyWeather
import com.example.marsad.databinding.DayLayoutBinding
import com.example.marsad.ui.utils.UnitsUtils
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.*


class DayAdapter(dayItems: List<DailyWeather>, val context: Context) :
    RecyclerView.Adapter<DayAdapter.ViewHolder>() {
    var days = dayItems
        set(value) {
            notifyDataSetChanged()
            field = value
        }

    lateinit var dayLayoutBinding: DayLayoutBinding

    inner class ViewHolder(var binding: DayLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        dayLayoutBinding = DayLayoutBinding.inflate(inflater, parent, false)
        return ViewHolder(dayLayoutBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentDayItem = days[position]
        val dayOfWeek =
            SimpleDateFormat("EEEE", Locale.getDefault()).format(currentDayItem.dt * 1000)
        val iconUrl = "https://openweathermap.org/img/wn/${currentDayItem.weather[0].icon}@2x.png"
        val minTemp = UnitsUtils.getTempRepresentation(
            context,
            currentDayItem.temp.day, false
        )
        val maxTemp = UnitsUtils.getTempRepresentation(
            context,
            currentDayItem.temp.night
        )
        holder.binding.dayTv.text = dayOfWeek
        holder.binding.tempTv.text = StringBuilder().append(minTemp, "/", maxTemp)
        Picasso.get().load(iconUrl).into(holder.binding.weatherIcon)
    }

    override fun getItemCount() = days.size

}