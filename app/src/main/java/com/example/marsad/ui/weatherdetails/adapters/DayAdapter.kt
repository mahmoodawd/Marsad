package com.example.marsad.ui.weatherdetails.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marsad.core.utils.getWeekday
import com.example.marsad.core.utils.localized
import com.example.marsad.core.utils.setImageUrl
import com.example.marsad.databinding.DayLayoutBinding
import com.example.marsad.domain.models.Daily


class DayAdapter(
    dayItems: List<Daily>,
    val tempUnit: String,
    var tempFactor: Double,
) :
    RecyclerView.Adapter<DayAdapter.ViewHolder>() {
    var days = dayItems
        set(value) {
            notifyDataSetChanged()
            field = value
        }

    private lateinit var dayLayoutBinding: DayLayoutBinding

    inner class ViewHolder(var binding: DayLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        dayLayoutBinding = DayLayoutBinding.inflate(inflater, parent, false)
        return ViewHolder(dayLayoutBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentDayWeather = days[position]

        val minTemp =
            currentDayWeather.tempMin.times(tempFactor).localized
        val maxTemp =
            currentDayWeather.tempMax.times(tempFactor).localized

        holder.binding.dayTv.text = getWeekday(currentDayWeather.date.toLong())
        holder.binding.weatherDescTv.text = currentDayWeather.description
        holder.binding.tempTv.text =
            StringBuilder().append(minTemp, "/", maxTemp).append(tempUnit)
        holder.binding.weatherIcon.setImageUrl(currentDayWeather.icon)
    }

    override fun getItemCount() = days.size

}