package com.wojciechosak.openweatherapp.ui.location

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wojciechosak.openweatherapp.R
import com.wojciechosak.openweatherapp.data.dto.location.City
import com.wojciechosak.openweatherapp.databinding.CityRowBinding

class CitiesAdapter(
    private val resources: Resources,
    private val onItemClick: (City) -> Unit
) : RecyclerView.Adapter<CitiesAdapter.ViewHolder>() {

    private val items = City.values()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            root = LayoutInflater.from(parent.context).inflate(
                R.layout.city_row,
                parent,
                false
            ),
            resources = resources
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], onItemClick)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.unbind()
    }

    override fun getItemCount() = items.size

    class ViewHolder(
        root: View,
        private val resources: Resources
    ) : RecyclerView.ViewHolder(root) {

        private val binding = CityRowBinding.bind(root)

        fun bind(city: City, onItemClick: (City) -> Unit) {
            binding.root.setOnClickListener { onItemClick.invoke(city) }
            binding.value.text = resources.getString(city.nameResource)
        }

        fun unbind() {
            binding.root.setOnClickListener(null)
        }
    }
}
