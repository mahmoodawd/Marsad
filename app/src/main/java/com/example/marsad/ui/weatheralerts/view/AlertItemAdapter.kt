package com.example.marsad.ui.weatheralerts.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marsad.R
import com.example.marsad.data.model.AlertItem
import com.example.marsad.data.model.AlertType
import com.example.marsad.databinding.AlertItemBinding
import com.example.marsad.ui.utils.getDate
import com.example.marsad.ui.utils.getDateAndTime

class AlertItemAdapter(
    alertItems: List<AlertItem>,
    val context: Context
) :
    RecyclerView.Adapter<AlertItemAdapter.ViewHolder>() {
    var alertItemList = alertItems
        set(value) {
            notifyDataSetChanged()
            field = value
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
            if (currentAlert.alertType == AlertType.ALARM) R.drawable.ic_alarm_24
            else R.drawable.ic_alert_24

        holder.binding.startTimeTv.text = getDate(currentAlert.start)
        holder.binding.endTimeTv.text = getDate(currentAlert.end)
        holder.binding.alarmTypeIcon.setImageResource(alertIcon)
    }

    override fun getItemCount() = alertItemList.size
}