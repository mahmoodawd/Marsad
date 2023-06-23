package com.example.marsad.ui.home.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marsad.data.database.LocationLocalDataSource
import com.example.marsad.data.network.ApiState
import com.example.marsad.data.network.LocationRemoteDataSource
import com.example.marsad.data.repositories.LocationRepository
import com.example.marsad.databinding.FragmentHomeBinding
import com.example.marsad.ui.home.view.adapters.HourAdapter
import com.example.marsad.ui.home.view.adapters.DayAdapter
import com.example.marsad.ui.home.view.adapters.DayItem
import com.example.marsad.ui.home.view.adapters.HourItem
import com.example.marsad.ui.home.viewmodel.HomeViewModel
import com.example.marsad.ui.home.viewmodel.LocationViewModelFactory
import com.google.android.gms.location.*
import kotlinx.coroutines.launch


private const val My_LOCATION_PERMISSION_ID = 5005

class HomeFragment : Fragment() {
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var fragmentHomeBinding: FragmentHomeBinding
    lateinit var homeViewModel: HomeViewModel
    lateinit var hourAdapter: HourAdapter
    lateinit var dayAdapter: DayAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapters()
        setupViewModel()
        collectWeatherData()
    }

    private fun setupAdapters() {
        hourAdapter = HourAdapter(listOf())
        fragmentHomeBinding.hoursRv.apply {
            adapter = hourAdapter
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = RecyclerView.HORIZONTAL
            }
        }

        dayAdapter = DayAdapter(listOf())
        fragmentHomeBinding.daysRv.apply {
            adapter = dayAdapter
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = RecyclerView.VERTICAL
            }
        }
    }

    private fun setupViewModel() {
        val homeViewModelFactory = LocationViewModelFactory(
            LocationRepository.getInstance(
                LocationRemoteDataSource,
                LocationLocalDataSource()
            )
        )
        homeViewModel = ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]
    }

    private fun collectWeatherData() {
        lifecycleScope.launch {
            homeViewModel.postStateFlow.collect { result ->
                when (result) {
                    is ApiState.Loading -> {
                        fragmentHomeBinding.loadingBar.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), "Fetching......", Toast.LENGTH_LONG).show()
                    }
                    is ApiState.Success -> {
                        fragmentHomeBinding.loadingBar.visibility = View.GONE
                        buildViews(result)
                    }
                    else -> {
                        fragmentHomeBinding.loadingBar.visibility = View.GONE
                        Toast.makeText(requireContext(), "Failed To Fetch Data", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun buildViews(result: ApiState.Success) {
        val currentDayWeather = result.weatherStatus.list?.get(0)
        val currentDayView = fragmentHomeBinding.currentDayCardView
        val propertiesCard = fragmentHomeBinding.propertiesCard
        currentDayView.apply {
            currentDayWeather?.let {
                cityTv.text =
                    "${result.weatherStatus.city?.country}, ${result.weatherStatus.city?.name}"
                dataTimeTv.text = it.dtTxt.toString()
                tempTv.text =
                    "${it.main?.temp}°C"
                weatherConditionTv.text = it.weather?.get(0)?.description
                minMaxTv.text = "${it.main?.tempMin} / ${it.main?.tempMax}"
                feelsLikeTv.text = "Feels Like ${it.main?.feelsLike}"

                val hourItemList = listOf(
                    HourItem("12:00 PM", "icon1", "70°F"),
                    HourItem("1:00 PM", "icon2", "72°F"),
                    HourItem("2:00 PM", "icon3", "74°F"),
                    HourItem("3:00 PM", "icon4", "76°F"),
                    HourItem("4:00 PM", "icon5", "78°F"),
                    HourItem("5:00 PM", "icon6", "80°F"),
                    HourItem("6:00 PM", "icon7", "82°F"),
                    HourItem("7:00 PM", "icon8", "84°F"),
                    HourItem("8:00 PM", "icon9", "82°F"),
                    HourItem("9:00 PM", "icon10", "80°F"),
                    HourItem("10:00 PM", "icon11", "78°F"),
                    HourItem("11:00 PM", "icon12", "76°F")
                )

                val dayItemList = listOf(
                    DayItem("Monday", "icon1", "72°F"),
                    DayItem("Tuesday", "icon2", "74°F"),
                    DayItem("Wednesday", "icon3", "76°F"),
                    DayItem("Thursday", "icon4", "78°F"),
                    DayItem("Friday", "icon5", "80°F"),
                    DayItem("Saturday", "icon6", "82°F"),
                    DayItem("Sunday", "icon7", "84°F")
                )
                hourAdapter.hours = hourItemList
                dayAdapter.days = dayItemList




                propertiesCard.humidity.probName.text = "Humidity"
                propertiesCard.humidity.probValue.text = it.main?.humidity.toString()

                propertiesCard.pressure.probName.text = "Pressure"
                propertiesCard.pressure.probValue.text = it.main?.pressure.toString()

                propertiesCard.clouds.probName.text = "Clouds"
                propertiesCard.clouds.probValue.text = it.clouds?.all.toString()

                propertiesCard.windSpeed.probName.text = "Wind Speed"
                propertiesCard.windSpeed.probValue.text = it.wind?.speed.toString()

                propertiesCard.sunrise.probName.text = "sunrise"
                propertiesCard.sunrise.probValue.text =
                    result.weatherStatus.city?.sunrise.toString()

                propertiesCard.sunset.probName.text = "sunset"
                propertiesCard.sunset.probValue.text =
                    result.weatherStatus.city?.sunset.toString()

            }
        }

    }

    override fun onResume() {
        super.onResume()
        getLastLocation()

    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == My_LOCATION_PERMISSION_ID) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) getLastLocation()

        }
    }

    private fun getLastLocation() {
        if (!isLocationPermissionsGranted()) {
            requestPermissions()
        } else {
            if (!isLocationEnabled()) {
                askUserToEnableLocation()
            } else {
                requestNewLocationData()
            }
        }
    }

    private fun isLocationPermissionsGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            My_LOCATION_PERMISSION_ID
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun askUserToEnableLocation() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }


    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val locationRequest = LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        if (locationRequest != null) {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                mLocationCallback,
                Looper.myLooper()
            )
        }

    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {

            val lat = locationResult.lastLocation?.latitude.toString()
            val lon = locationResult.lastLocation?.longitude.toString()
            homeViewModel.getWeatherStatus(lat, lon)
        }
    }
}