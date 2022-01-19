package com.wojciechosak.openweatherapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.wojciechosak.openweatherapp.R
import com.wojciechosak.openweatherapp.data.dto.response.OpenApiResponse
import com.wojciechosak.openweatherapp.databinding.MainFragmentBinding
import com.wojciechosak.openweatherapp.di.CoroutineDispatchers
import com.wojciechosak.openweatherapp.ui.bottomsheet.BottomSheetView
import com.wojciechosak.openweatherapp.ui.location.LocationPickerDialogFragment
import com.wojciechosak.openweatherapp.utils.setGone
import com.wojciechosak.openweatherapp.utils.setVisible
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val LOCATION_PICKER_TAG = "LocationPicker"

class MainFragment : Fragment(R.layout.main_fragment) {

    private var _binding: MainFragmentBinding? = null

    private val coroutineDispatchers: CoroutineDispatchers by inject()

    @VisibleForTesting
    internal val binding: MainFragmentBinding by lazy { _binding!! }

    private val viewModel: MainFragmentViewModel by viewModel()

    private lateinit var bottomSheetView: BottomSheetView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MainFragmentBinding.inflate(layoutInflater)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomSheet()

        setupSwipeRefreshLayout()
        observeDataChanges()
        observeErrorsChanges()
        setupCityPicker()
    }

    private fun setupCityPicker() {
        binding.city.setOnClickListener {
            LocationPickerDialogFragment.newInstance {
                viewModel.changeCurrentCity(it)
            }.show(requireActivity().supportFragmentManager, LOCATION_PICKER_TAG)
        }
    }

    private fun setupBottomSheet() {
        val bottomSheetRoot = binding.bottomSheet.root
        bottomSheetView = BottomSheetView(bottomSheetRoot)
        val behavior = BottomSheetBehavior.from(bottomSheetRoot)
        behavior.state = BottomSheetBehavior.STATE_SETTLING
    }

    private fun setupSwipeRefreshLayout() {
        binding.swipeRefreshLayout.apply {
            isRefreshing = true
            setOnRefreshListener { viewModel.loadData() }
        }
    }

    private fun observeDataChanges() {
        viewModel.cityFlow
            .onEach { city ->
                binding.city.text = getString(city.nameResource).replaceFirstChar { it.uppercase() }
            }
            .flowOn(coroutineDispatchers.main)
            .launchIn(lifecycleScope)

        viewModel.data
            .filterNotNull()
            .onEach {
                onDataLoad(it)
            }
            .flowOn(coroutineDispatchers.main)
            .launchIn(lifecycleScope)
    }

    private fun onDataLoad(data: OpenApiResponse) {
        bottomSheetView.loadData(data)
        binding.apply {
            city.setVisible()
            cityIcon.setVisible()
            generalError.setGone()
            currentTemperature.setVisible()
            swipeRefreshLayout.isRefreshing = false
            currentTemperature.text = getString(R.string.temperature_celsius, data.current.temp)
        }
    }

    private fun observeErrorsChanges() {
        viewModel.errorFlow
            .onEach {
                binding.apply {
                    swipeRefreshLayout.isRefreshing = false
                    city.setGone()
                    cityIcon.setGone()
                    currentTemperature.setGone()
                    generalError.setVisible()
                }
            }
            .flowOn(coroutineDispatchers.main)
            .launchIn(lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
