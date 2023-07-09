package com.example.marsad.ui.favorites.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marsad.R
import com.example.marsad.data.database.localdatasources.LocationLocalDataSource
import com.example.marsad.data.network.ApiState
import com.example.marsad.data.network.WeatherRemoteDataSource
import com.example.marsad.data.network.OneCallResponse
import com.example.marsad.data.repositories.LocationRepository
import com.example.marsad.databinding.FragmentLocationDetailsBinding
import com.example.marsad.ui.favorites.viewmodel.SharedViewModel
import com.example.marsad.ui.home.view.adapters.DayAdapter
import com.example.marsad.ui.home.view.adapters.HourAdapter
import com.example.marsad.ui.home.viewmodel.HomeViewModel
import com.example.marsad.ui.utils.MyViewModelFactory
import com.example.marsad.ui.utils.UnitsUtils
import com.example.marsad.ui.utils.getFullDateAndTime
import com.example.marsad.ui.utils.getHour
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch


class LocationDetailsFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var homeContent: View
    lateinit var binding: FragmentLocationDetailsBinding
    lateinit var homeViewModel: HomeViewModel
    lateinit var hourAdapter: HourAdapter
    lateinit var dayAdapter: DayAdapter
    var lat: Double = 0.0
    var lon: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeContent = view.findViewById(R.id.home_content)

        setLatAndLon()
        setupAdapters()
        setupViewModel()
        collectWeatherData()
    }

    private fun setLatAndLon() {
        sharedViewModel.savedLocationLiveData.observe(requireActivity()) {
            lat = it.lat
            lon = it.lon
        }
    }


    private fun setupAdapters() {
        hourAdapter = HourAdapter(listOf(), requireContext())
        binding.homeContent.hoursRv.apply {
            adapter = hourAdapter
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = RecyclerView.HORIZONTAL
            }
        }

        dayAdapter = DayAdapter(listOf(), requireContext())
        binding.homeContent.daysRv.apply {
            adapter = dayAdapter
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = RecyclerView.VERTICAL
            }
        }
    }

    private fun setupViewModel() {
        val homeViewModelFactory = MyViewModelFactory(
            LocationRepository.getInstance(
                WeatherRemoteDataSource, LocationLocalDataSource(requireContext())
            )
        )
        homeViewModel = ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]
        if (lat != 0.0 && lon != 0.0) {
            homeViewModel.getWeatherStatus(lat, lon)
        }
    }

    private fun collectWeatherData() {
        lifecycleScope.launch {
            homeViewModel.weatherDataStateFlow.collect { result ->
                when (result) {
                    is ApiState.Loading -> {
                        binding.loadingBar.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), "Fetching......", Toast.LENGTH_LONG).show()
                    }
                    is ApiState.Success<*> -> {
                        binding.loadingBar.visibility = View.GONE
                        homeContent.visibility = View.VISIBLE
                        buildViews(result.weatherStatus as OneCallResponse)
                    }
                    else -> {
                        binding.loadingBar.visibility = View.GONE
                        Toast.makeText(requireContext(), "Failed To Fetch Data", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun buildViews(weatherStatus: OneCallResponse) {
        val currentDayWeather = weatherStatus.current
        val currentDayView = binding.homeContent.currentDayCardView
        val propertiesCard = binding.homeContent.propertiesCard


        currentDayView.apply {
            currentDayWeather?.let {

                cityTv.text =
                    UnitsUtils.getCity(requireContext(), weatherStatus.lat, weatherStatus.lon)

                val iconUrl =
                    "https://openweathermap.org/img/wn/${currentDayWeather.weather[0].icon}@4x.png"
                Picasso.get().load(iconUrl).into(weatherIcon)
                dataTimeTv.text = getFullDateAndTime(weatherStatus.timezone_offset.toInt(), it.dt)
                tempTv.text = java.lang.StringBuilder().append(
                    UnitsUtils.getTempRepresentation(requireContext(), it.temp)
                )
                weatherConditionTv.text = it.weather?.get(0)?.description

                val minTemp = UnitsUtils.getTempRepresentation(
                    requireContext(),
                    weatherStatus.daily[0].temp.min, false
                )
                val maxTemp = UnitsUtils.getTempRepresentation(
                    requireContext(),
                    weatherStatus.daily[0].temp.max
                )

                minMaxTv.text = StringBuilder().append(minTemp, "/", maxTemp)

                feelsLikeTv.text =
                    UnitsUtils.getTempRepresentation(requireContext(), it.feels_like)

                hourAdapter.hours = weatherStatus.hourly.take(24)
                dayAdapter.days = weatherStatus.daily.take(7)




                propertiesCard.apply {

                    humidity.probName.text = getString(R.string.humidity)
                    humidity.probIcon.setImageResource(R.drawable.ic_water_drop_24)
                    humidity.probValue.text = StringBuilder().append(
                        UnitsUtils.localizeNumber(it.humidity), getString(R.string.percentageSymbol)
                    )

                    pressure.probName.text = getString(R.string.pressure)
                    pressure.probIcon.setImageResource(R.drawable.ic_compress_24)
                    pressure.probValue.text = StringBuilder().append(
                        UnitsUtils.localizeNumber(it.pressure),
                        " ",
                        getString(R.string.weather_unit)
                    )

                    clouds.probName.text = getString(R.string.clouds)
                    clouds.probIcon.setImageResource(R.drawable.ic_cloud_24)
                    clouds.probValue.text = StringBuilder().append(
                        UnitsUtils.localizeNumber(it.clouds), getString(R.string.percentageSymbol)
                    )

                    windSpeed.probName.text = getString(R.string.wind_speed)
                    windSpeed.probIcon.setImageResource(R.drawable.ic_air_24)
                    windSpeed.probValue.text =
                        UnitsUtils.getSpeedRepresentation(requireContext(), it.wind_speed)

                    sunrise.probName.text = getString(R.string.sunrise)
                    sunrise.probIcon.setImageResource(R.drawable.ic_sunrise_24)
                    sunrise.probValue.text = getHour(it.sunrise)

                    sunset.probName.text = getString(R.string.sunset)
                    sunset.probIcon.setImageResource(R.drawable.ic_sunset_24)
                    sunset.probValue.text = getHour(it.sunset)
                }


            }
        }

    }


}