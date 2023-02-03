package com.wojciechosak.openweatherapp.ui.bottomsheet

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.wojciechosak.openweatherapp.databinding.BottomSheetBinding
import com.wojciechosak.openweatherapp.ui.main.MainFragmentViewModel
import timber.log.Timber
import java.util.TimeZone

internal const val MAX_FORECAST_ITEMS_ON_LIST = 6

class BottomSheetView(root: View) {

    private val binding: BottomSheetBinding = BottomSheetBinding.bind(root)

    fun loadData(data: MainFragmentViewModel.ViewState) {
        data.weatherData?.let { weatherData ->
            binding.forecastRecyclerView.apply {
                adapter = ForecastAdapter(
                    items = weatherData.daily.take(MAX_FORECAST_ITEMS_ON_LIST),
                    timeZone = TimeZone.getTimeZone(weatherData.timezone),
                    resources = binding.root.resources
                )
                layoutManager = LinearLayoutManager(context)
            }
        } ?: run { Timber.e("Weather data is null, can not update!") }
    }
}
