package com.example.marsad.ui.weatherdetails.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marsad.databinding.PropertyItemBinding


class PropertyAdapter(private val properties: List<Property>, val context: Context) :
    RecyclerView.Adapter<PropertyAdapter.ViewHolder>() {


    fun updateValues(values: List<String>) {
        properties.forEachIndexed { index, property ->
            property.value = values[index]
            notifyItemChanged(index)
        }
    }

    private lateinit var propertyBinding: PropertyItemBinding

    inner class ViewHolder(var binding: PropertyItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        propertyBinding = PropertyItemBinding.inflate(inflater, parent, false)
        return ViewHolder(propertyBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentProperty = properties[position]
        holder.binding.probName.text = context.getString(currentProperty.title)
        holder.binding.probIcon.setImageResource(currentProperty.icon)
        holder.binding.probValue.text = currentProperty.value
        holder.binding.probSymbol.text = context.getString(currentProperty.symbol)
    }

    override fun getItemCount() = properties.size

}