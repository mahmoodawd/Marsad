package com.example.marsad.ui.home.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marsad.databinding.HourItemBinding
import com.squareup.picasso.Picasso

data class HourItem(val time: String, val icon: String, val temperature: String)

class HourAdapter(hourList: List<HourItem>) : RecyclerView.Adapter<HourAdapter.ViewHolder>() {
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
        holder.binding.hourTv.text = currentHourItem.time
        holder.binding.hourTempTv.text = currentHourItem.temperature
//        Picasso.get().load(currentHourItem.icon).into(hourItemBinding.hourTempIcon)
    }

    override fun getItemCount(): Int = hours.size

}