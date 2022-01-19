package com.wojciechosak.openweatherapp.ui.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.wojciechosak.openweatherapp.R
import com.wojciechosak.openweatherapp.data.dto.location.City
import com.wojciechosak.openweatherapp.databinding.LocationPickerBinding
import timber.log.Timber

class LocationPickerDialogFragment(
    private val onSelectionListener: (City) -> Unit
) : DialogFragment(R.layout.location_picker) {

    private var binding: LocationPickerBinding? = null

    companion object {
        fun newInstance(
            onSelectionListener: (City) -> Unit
        ) = LocationPickerDialogFragment(onSelectionListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LocationPickerBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.recyclerView?.apply {
            adapter = CitiesAdapter(resources = resources) {
                Timber.d("Clicked city: $it")
                onSelectionListener.invoke(it)
                dismiss()
            }
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
