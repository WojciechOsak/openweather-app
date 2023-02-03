package com.wojciechosak.openweatherapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.wojciechosak.openweatherapp.R
import com.wojciechosak.openweatherapp.databinding.MainFragmentBinding
import com.wojciechosak.openweatherapp.di.CoroutineDispatchers
import com.wojciechosak.openweatherapp.ui.bottomsheet.BottomSheetView
import com.wojciechosak.openweatherapp.ui.location.CityPickerDialogFragment
import com.wojciechosak.openweatherapp.utils.setGone
import com.wojciechosak.openweatherapp.utils.setVisible
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

private const val LOCATION_PICKER_TAG = "LocationPicker"
private const val LIGHT_ANIMATION_DURATION = 1500L
private const val MAX_DAY_HOUR = 19
private const val MIN_DAY_HOUR = 7

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
        setupCityPicker()
        viewModel.loadData()
    }

    private fun setupCityPicker() {
        binding.city.setOnClickListener {
            CityPickerDialogFragment.newInstance {
                viewModel.changeCurrentCity(it)
            }.show(requireActivity().supportFragmentManager, LOCATION_PICKER_TAG)
        }
    }

    private fun setupBottomSheet() {
        val bottomSheetRoot = binding.bottomSheet.root
        bottomSheetView = BottomSheetView(bottomSheetRoot)
        val behavior = BottomSheetBehavior.from(bottomSheetRoot)
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun setupSwipeRefreshLayout() {
        binding.swipeRefreshLayout.apply {
            isRefreshing = true
            setOnRefreshListener { viewModel.loadData() }
        }
    }

    private fun observeDataChanges() {
        viewModel.state
            .onEach { data ->
                onDataLoad(data)
            }
            .flowOn(coroutineDispatchers.main)
            .launchIn(lifecycleScope)
    }

    private fun onDataLoad(data: MainFragmentViewModel.ViewState) {
        binding.city.text = getString(data.city.nameResource).replaceFirstChar { it.uppercase() }
        bottomSheetView.loadData(data)
        binding.apply {
            city.setVisible()
            cityIcon.setVisible()
            generalError.setGone()
            currentTemperature.setVisible()
            swipeRefreshLayout.isRefreshing = false
            currentTemperature.text =
                getString(R.string.temperature_celsius, data.weatherData?.current?.temp)
            updateBackgroundImage(data.weatherData?.timezone_offset)
        }
        data.error?.let {
            binding.apply {
                swipeRefreshLayout.isRefreshing = false
                city.setGone()
                cityIcon.setGone()
                currentTemperature.setGone()
                generalError.setVisible()
            }
        }
    }

    private fun updateBackgroundImage(timezoneOffset: Int?) {
        val hourInTimeZone = Instant.now(Clock.systemUTC())
            .atOffset(ZoneOffset.ofTotalSeconds(timezoneOffset ?: 0))
            .hour

        val isNight = hourInTimeZone > MAX_DAY_HOUR || hourInTimeZone < MIN_DAY_HOUR

        binding.weatherImageView.setImageDrawable(
            AppCompatResources.getDrawable(
                requireContext(),
                if (isNight) R.drawable.night_base else R.drawable.day_base
            )
        )
        binding.weatherImageViewLight.clearAnimation()
        binding.weatherImageViewLight.setImageDrawable(
            AppCompatResources.getDrawable(
                requireContext(),
                if (isNight) R.drawable.night_volume_light else R.drawable.day_volume_light
            )
        )
        animateLightDown()
    }

    private fun animateLightDown() {
        binding.weatherImageViewLight
            .animate()
            .alpha(0f)
            .setDuration(LIGHT_ANIMATION_DURATION)
            .withEndAction { animateLightUp() }
            .start()
    }

    private fun animateLightUp() {
        binding.weatherImageViewLight
            .animate()
            .alpha(1f)
            .setDuration(LIGHT_ANIMATION_DURATION)
            .withEndAction { animateLightDown() }
            .start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
