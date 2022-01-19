package com.wojciechosak.openweatherapp.ui.bottomsheet

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.wojciechosak.openweatherapp.R
import com.wojciechosak.openweatherapp.data.dto.weather.DailyForecast
import com.wojciechosak.openweatherapp.databinding.ForecastWeatherRowBinding
import java.time.Instant
import java.time.format.TextStyle
import java.util.Locale
import java.util.TimeZone

private const val CLOUDY_THRESHOLD = 50

class ForecastAdapter(
    private val items: List<DailyForecast>,
    private val timeZone: TimeZone,
    private val resources: Resources
) : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            root = LayoutInflater.from(parent.context).inflate(
                R.layout.forecast_weather_row,
                parent,
                false
            ),
            resources = resources
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            forecast = items[position],
            timeZone = timeZone,
            isToday = position == 0
        )
    }

    override fun getItemCount() = items.size

    class ViewHolder(
        private val root: View,
        private val resources: Resources
    ) : RecyclerView.ViewHolder(root) {

        private val binding = ForecastWeatherRowBinding.bind(root)

        fun bind(forecast: DailyForecast, timeZone: TimeZone, isToday: Boolean) {
            binding.day.text = if (isToday) {
                resources.getString(R.string.today)
            } else {
                Instant.ofEpochSecond(forecast.date)
                    .atZone(timeZone.toZoneId()).dayOfWeek
                    .getDisplayName(TextStyle.FULL, Locale.getDefault())
            }

            binding.conditionsIcon.setImageDrawable(getConditionsIcon(forecast))
            binding.conditionsLabel.text = resources.getString(
                if (forecast.clouds > CLOUDY_THRESHOLD) R.string.cloudly else R.string.sunny
            )

            bindPanel(forecast)
        }

        private fun getConditionsIcon(forecast: DailyForecast): Drawable? {
            return AppCompatResources.getDrawable(
                root.context,
                if (forecast.clouds > CLOUDY_THRESHOLD) R.drawable.icon_cloud else R.drawable.icon_sun
            )
        }

        private fun bindPanel(forecast: DailyForecast) {
            binding.conditionsPanel.dayTemperatureValue.text =
                resources.getString(R.string.temperature_celsius, forecast.temp.day)
            binding.conditionsPanel.morningTemperatureValue.text =
                resources.getString(R.string.temperature_celsius, forecast.temp.morn)
            binding.conditionsPanel.nightTemperatureValue.text =
                resources.getString(R.string.temperature_celsius, forecast.temp.night)
            binding.conditionsPanel.humidityValue.text =
                resources.getString(R.string.humidity_percentage, forecast.humidity)
        }
    }
}
