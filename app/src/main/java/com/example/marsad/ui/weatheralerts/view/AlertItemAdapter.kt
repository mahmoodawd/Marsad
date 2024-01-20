package com.example.marsad.ui.weatheralerts.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marsad.R
import com.example.marsad.core.utils.getDateAndTime
import com.example.marsad.databinding.AlertItemBinding
import com.example.marsad.domain.models.Alert
import com.example.marsad.domain.models.AlertType

class AlertItemAdapter(
    alertItems: List<Alert>,
) : RecyclerView.Adapter<AlertItemAdapter.ViewHolder>() {
    var alertItemList = alertItems
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    lateinit var binding: AlertItemBinding

    inner class ViewHolder(var binding: AlertItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = AlertItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentAlert = alertItemList[position]
        val alertIcon =
            if (currentAlert.type == AlertType.ALARM) R.drawable.ic_alarm_24
            else R.drawable.ic_alert_24

        holder.binding.startTimeTv.text = getDateAndTime(currentAlert.startTime.toLong() / 1000)
        holder.binding.endTimeTv.text = getDateAndTime(currentAlert.endTime.toLong() / 1000)
        holder.binding.alarmTypeIcon.setImageResource(alertIcon)
    }

    override fun getItemCount() = alertItemList.size

}