package com.example.marsad.ui.home.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marsad.databinding.DayLayoutBinding

data class DayItem(val title: String, val icon: String, val temperature: String)

class DayAdapter(dayItems: List<DayItem>) : RecyclerView.Adapter<DayAdapter.ViewHolder>() {
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
        holder.binding.dayTv.text = currentDayItem.title
        holder.binding.tempTv.text = currentDayItem.temperature
    }

    override fun getItemCount() = days.size

}