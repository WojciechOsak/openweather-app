package com.wojciechosak.openweatherapp.ui.bottomsheet

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.wojciechosak.openweatherapp.data.dto.response.OpenApiResponse
import com.wojciechosak.openweatherapp.databinding.BottomSheetBinding
import java.util.TimeZone

internal const val MAX_FORECAST_ITEMS_ON_LIST = 6

class BottomSheetView(root: View) {

    private val binding: BottomSheetBinding = BottomSheetBinding.bind(root)

    fun loadData(data: OpenApiResponse) {
        binding.forecastRecyclerView.apply {
            adapter = ForecastAdapter(
                items = data.daily.take(MAX_FORECAST_ITEMS_ON_LIST),
                timeZone = TimeZone.getTimeZone(data.timezone),
                resources = binding.root.resources
            )
            layoutManager = LinearLayoutManager(context)
        }
    }
}
