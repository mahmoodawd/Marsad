package com.example.marsad.ui.weatherdetails.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.marsad.R
import com.example.marsad.core.utils.getCountryName
import com.example.marsad.core.utils.getShortTime
import com.example.marsad.core.utils.localized
import com.example.marsad.core.utils.setImageUrl
import com.example.marsad.core.utils.show
import com.example.marsad.core.utils.showSnackbar
import com.example.marsad.core.utils.viewModelFactory
import com.example.marsad.core.utils.visibleIf
import com.example.marsad.data.database.localdatasources.WeatherDetailsLocalDataSource
import com.example.marsad.data.network.remotesource.WeatherRemoteDataSource
import com.example.marsad.data.repositories.UserPrefsRepository
import com.example.marsad.data.repositories.WeatherDetailsRepository
import com.example.marsad.databinding.FragmentWeatherDetailsBinding
import com.example.marsad.domain.models.WeatherDetails
import com.example.marsad.ui.weatherdetails.adapters.DayAdapter
import com.example.marsad.ui.weatherdetails.adapters.HourAdapter
import com.example.marsad.ui.weatherdetails.adapters.Property
import com.example.marsad.ui.weatherdetails.adapters.PropertyAdapter
import com.example.marsad.ui.weatherdetails.viewmodel.DetailsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class WeatherDetailsFragment : Fragment() {
    lateinit var binding: FragmentWeatherDetailsBinding

    private val viewModel: DetailsViewModel by viewModels(ownerProducer = { requireActivity() }) {
        viewModelFactory {
            DetailsViewModel(
                weatherDetailsRepository = WeatherDetailsRepository.getInstance(
                    WeatherRemoteDataSource, WeatherDetailsLocalDataSource(requireContext())
                ),
                UserPrefsRepository.getInstance(requireActivity())
            )
        }
    }

    private lateinit var tempUnit: String
    private var tempFactor: Double = 1.0
    private lateinit var speedUnit: String
    private var speedFactor: Double = 1.0

    private val hourAdapter: HourAdapter by lazy {
        HourAdapter(tempFactor = tempFactor, listOf())
    }
    private val dayAdapter: DayAdapter by lazy {
        DayAdapter(listOf(), tempUnit = tempUnit, tempFactor = tempFactor)

    }
    private val propertyAdapter: PropertyAdapter by lazy {
        val properties = listOf(
            Property(
                title = R.string.humidity,
                icon = R.drawable.ic_water_drop_24,
                value = "",
                symbol = R.string.percentageSymbol
            ),
            Property(
                title = R.string.wind_speed,
                icon = R.drawable.ic_air_24,
                value = "",
                symbol = R.string.empty
            ),
            Property(
                title = R.string.clouds,
                icon = R.drawable.ic_cloud_24,
                value = "",
                symbol = R.string.percentageSymbol
            ),
            Property(
                title = R.string.pressure,
                icon = R.drawable.ic_compress_24,
                value = "",
                symbol = R.string.pressure_unit
            ),
            Property(
                title = R.string.sunrise,
                icon = R.drawable.ic_sunrise_24,
                value = "",
                symbol = R.string.empty
            ),
            Property(
                title = R.string.sunset,
                icon = R.drawable.ic_sunset_24,
                value = "",
                symbol = R.string.empty
            ),

            )
        PropertyAdapter(properties, requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        binding = FragmentWeatherDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.userPrefs.collectLatest { state ->
                Log.d(TAG, "onViewCreated: $state")
                tempFactor = state.tempUnit.factor
                tempUnit = getString(state.tempUnit.symbol)
                speedFactor = state.speedUnit.factor
                speedUnit = getString(state.speedUnit.symbol)
                hourAdapter.tempFactor = tempFactor
                dayAdapter.tempFactor = tempFactor
            }
        }

        setLanLon(onLatLonChanged = viewModel::onLatLonChanged)
        setupAdapters()
        collectWeatherData()

    }

    private fun setLanLon(onLatLonChanged: (lat: Double, lon: Double) -> Unit) {
        val args: WeatherDetailsFragmentArgs by navArgs()
        val lat = args.lat
        val lon = args.lon
        Log.d(TAG, "setLanLon: $lat, $lon")
        if (lat != 0.0f && lon != 0.0f) {
            onLatLonChanged(lat.toDouble(), lon.toDouble())
        }
    }


    private fun setupAdapters() {
        binding.detailsContent.run {
            hoursRv.adapter = hourAdapter
            daysRv.adapter = dayAdapter
            propertiesRv.adapter = propertyAdapter
        }
    }


    private fun collectWeatherData() {
        lifecycleScope.launch {
            viewModel.weatherDetails.collect { result ->
                binding.loadingBar visibleIf (result is DetailsUiState.Loading)
                when (result) {
                    is DetailsUiState.Success -> {
                        binding.detailsContent.root.show()
                        buildViews(result.details)
                    }

                    is DetailsUiState.Error -> {
                        showSnackbar(msg = R.string.error_getting_data)
                    }

                    else -> {}
                }
            }
        }
    }

    private fun buildViews(details: WeatherDetails) {

        val maxTemp =
            details.current.main.tempMax.times(tempFactor).localized
        val minTemp =
            details.current.main.tempMin.times(tempFactor).localized

        (requireActivity() as AppCompatActivity).supportActionBar?.title = details.city.name

        buildCurrentDayView(
            imageUrl = details.current.weather.icon,
            weatherDescription = details.current.weather.description,
            temperature = "${details.current.main.temperature.times(tempFactor).localized} $tempUnit",
            city = "${details.city.name}\n ${getCountryName(details.city.country)}",
            dateTime = details.dateTime,
            minMax = "$minTemp / $maxTemp $tempUnit",
            feelsLike = "${details.current.main.feelsLike.times(tempFactor).localized} $tempUnit"
        )
        val propertiesValues = listOf(
            details.current.main.humidity.localized,
            "${details.current.windSpeed.times(speedFactor).localized} $speedUnit",
            details.current.clouds.localized,
            details.current.main.pressure.localized,
            details.city.sunrise.getShortTime(),
            details.city.sunset.getShortTime()
        )
        hourAdapter.hours = details.hourly
        dayAdapter.days = details.daily
        propertyAdapter.updateValues(propertiesValues)


    }

    private fun buildCurrentDayView(
        city: String,
        imageUrl: String,
        dateTime: String,
        temperature: String,
        weatherDescription: String,
        minMax: String,
        feelsLike: String,
    ) {
        binding.detailsContent.currentDayCardView.apply {
            cityTv.text = city
            weatherIcon.setImageUrl(imageUrl)
            dataTimeTv.text = dateTime
            tempTv.text = temperature
            weatherConditionTv.text = weatherDescription
            minMaxTv.text = minMax
            feelsLikeValue.text = feelsLike
        }
    }

    companion object {
        private const val TAG = "LocationDetailsFragment"
    }

}