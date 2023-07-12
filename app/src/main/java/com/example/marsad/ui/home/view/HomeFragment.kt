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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marsad.R
import com.example.marsad.data.database.localdatasources.WeatherDetailsLocalDataSource
import com.example.marsad.data.network.*
import com.example.marsad.data.repositories.WeatherDetailsRepository
import com.example.marsad.databinding.FragmentHomeBinding
import com.example.marsad.ui.home.view.adapters.DayAdapter
import com.example.marsad.ui.home.view.adapters.HourAdapter
import com.example.marsad.ui.home.viewmodel.HomeViewModel
import com.example.marsad.utils.MyViewModelFactory
import com.example.marsad.utils.UnitsUtils
import com.example.marsad.utils.getFullDateAndTime
import com.example.marsad.utils.getHour
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.util.*


private const val My_LOCATION_PERMISSION_ID = 5005
private const val DEFAULT_LATITUDE = 26.8206
private const val DEFAULT_LONGITUDE = 30.8025

class HomeFragment : Fragment(), OnMapReadyCallback {
    private lateinit var weatherResponse: WeatherDetailsResponse
    private var isHaveLocation = false
    private var isFirstUse = true
    var lat: Double = DEFAULT_LATITUDE
    var lon: Double = DEFAULT_LONGITUDE
    private lateinit var requestLocationView: View
    private lateinit var mapView: View
    private lateinit var homeContent: View
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel by lazy {
        val homeViewModelFactory = MyViewModelFactory(
            WeatherDetailsRepository.getInstance(
                WeatherRemoteDataSource, WeatherDetailsLocalDataSource(requireContext())
            )
        )
        ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]
    }
    lateinit var hourAdapter: HourAdapter
    lateinit var dayAdapter: DayAdapter
    lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestLocationView = view.findViewById(R.id.request_location_view)
        mapView = view.findViewById(R.id.map_view)
        homeContent = view.findViewById(R.id.home_content)

        binding.requestLocationView.grantBtn.setOnClickListener {
            requestLocationView.visibility = View.GONE
            requestLocationPermissions()
        }
        binding.mapView.confirmFab.setOnClickListener {
            mapView.visibility = View.GONE
            requestLocationView.visibility = View.GONE
            homeContent.visibility = View.VISIBLE
            homeViewModel.getWeatherStatus(lat, lon)
            isFirstUse = false
            isHaveLocation = true
            val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
            prefs.edit().putString(getString(R.string.pref_location_method_key), "map").apply()
            updateSharedPrefs()
        }
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.googleMapView) as SupportMapFragment


        mapFragment.getMapAsync(this)
        readDataFromSharedPrefs()
        setupAdapters()
        getWeatherDetails()
        collectWeatherData()
    }


    override fun onDestroy() {
        super.onDestroy()
        updateSharedPrefs()

    }

    private fun updateSharedPrefs() {
        val sharedPreferences = activity?.getSharedPreferences(
            getString(R.string.preferences_file_key), Context.MODE_PRIVATE
        ) ?: return
        with(sharedPreferences.edit()) {
            putString(getString(R.string.latitude_key), lat.toString())
            putString(getString(R.string.longitude_key), lon.toString())
            putBoolean(getString(R.string.first_use_key), isFirstUse)
            putBoolean(getString(R.string.is_having_location_key), isHaveLocation)
            apply()
        }
    }

    private fun readDataFromSharedPrefs() {
        val sharedPreferences = activity?.getSharedPreferences(
            getString(R.string.preferences_file_key), Context.MODE_PRIVATE
        ) ?: return
        lat =
            sharedPreferences.getString(
                getString(R.string.latitude_key),
                null
            )
                ?.toDouble() ?: 0.0

        lon =
            sharedPreferences.getString(
                getString(R.string.longitude_key),
                null
            )
                ?.toDouble() ?: 0.0

        isFirstUse = sharedPreferences.getBoolean(getString(R.string.first_use_key), true)

        isHaveLocation =
            sharedPreferences.getBoolean(getString(R.string.is_having_location_key), false)
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

    private fun getWeatherDetails() {
        if (lat != 0.0 && lon != 0.0) {
            homeViewModel.getWeatherStatus(lat, lon, false)
            homeViewModel.getWeatherStatus(lat, lon)
        }
    }

    private fun collectWeatherData() {
        lifecycleScope.launch {
            homeViewModel.weatherDataStateFlow.collect { result ->
                when (result) {
                    is ApiState.Loading -> {
                        binding.loadingBar.visibility = View.VISIBLE
                    }
                    is ApiState.Success<*> -> {
                        binding.loadingBar.visibility = View.GONE
                        homeContent.visibility = View.VISIBLE
                        weatherResponse = result.weatherStatus as WeatherDetailsResponse
                        buildViews(weatherResponse)
                    }
                    else -> {
                        binding.loadingBar.visibility = View.GONE
                        /* Toast.makeText(requireContext(), "Failed To Fetch Data", Toast.LENGTH_SHORT)
                             .show()*/
                    }
                }
            }
        }
    }

    private fun buildViews(weatherStatus: WeatherDetailsResponse) {
        val currentDayWeather = weatherStatus.current
        val currentDayView = binding.homeContent.currentDayCardView
        val propertiesCard = binding.homeContent.propertiesCard

        hourAdapter.hours = weatherStatus.hourly.take(24)
        dayAdapter.days = weatherStatus.daily.take(7)

        currentDayWeather?.let {
            currentDayView.apply {

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

                feelsLikeTv.text = StringBuilder().append(
                    getString(R.string.feels_like), " ",
                    UnitsUtils.getTempRepresentation(requireContext(), it.feels_like)
                )
            }

            propertiesCard.apply {

                humidity.probName.text = getString(R.string.humidity)
                humidity.probIcon.setImageResource(R.drawable.ic_water_drop_24)
                humidity.probValue.text = StringBuilder().append(
                    UnitsUtils.localizeNumber(it.humidity),
                    " ",
                    getString(R.string.percentageSymbol)
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
                    UnitsUtils.localizeNumber(it.clouds),
                    " ",
                    getString(R.string.percentageSymbol)
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

    override fun onResume() {
        super.onResume()
        Log.i("HomeFragment", "onResume: Get Called")
        getLastLocation()
    }

    private fun getLastLocation() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val prefLocationMethod = prefs.getString("locationMethod", null)
        when (prefLocationMethod) {
            "map" -> {
                if (isFirstUse) displayMap()
                else return
            }
            "gps" -> {
                if (isLocationPermissionsGranted()) {
                    getLocationUsingGPS()
                } else {
                    requestLocationPermissions()
                }
            }
            else -> showDialog()
        }
        /*if (isFirstUse) {
            showDialog()
        } else {
            if (isLocationPermissionsGranted()) {
                getLocationUsingGPS()
            } else {
                when (isHaveLocation) {
                    true -> return //In case using previously stored map location
                    false -> {
                        requestLocationView.visibility = View.VISIBLE
                        mapView.visibility = View.GONE
                        homeContent.visibility = View.GONE
                    }
                }
            }
        }*/
    }

    private fun showDialog() {
        MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.location_method))
            .setIcon(R.drawable.ic_launcher_foreground)
            .setMessage(getString(R.string.dialog_msg))
            .setNeutralButton(getString(R.string.map)) { dialog, which ->
                displayMap()
            }
            .setNeutralButtonIcon(
                getDrawable(requireContext(), R.drawable.ic_map_24)
            )
            .setPositiveButton(getString(R.string.gps)) { dialog, which ->
                requestLocationPermissions()
            }
            .setPositiveButtonIcon(
                getDrawable(requireContext(), R.drawable.ic_map_24)
            )
            .show()
    }

    private fun displayMap() {
        mapView.visibility = View.VISIBLE
        homeContent.visibility = View.GONE
    }

    private fun getLocationUsingGPS() {
        if (!isLocationEnabled()) {
            askUserToEnableLocation()
        } else {
            requestNewLocationData()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapClickListener {
            MarkerOptions().apply {
                position(it)
                mMap.clear()
                mMap.addMarker(this)
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 10.0F))
            lat = it.latitude
            lon = it.longitude
            binding.mapView.confirmFab.apply {
                text =
                    UnitsUtils.getCity(requireContext(), lat, lon)
                icon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_check)

                setBackgroundColor(resources.getColor(R.color.md_theme_light_primary))
                setTextColor(resources.getColor(R.color.md_theme_dark_onBackground))

            }

        }

    }


    private fun isLocationPermissionsGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            ), My_LOCATION_PERMISSION_ID
        )
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        prefs.edit().putString(getString(R.string.pref_location_method_key), "gps").apply()
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
                locationRequest, mLocationCallback, Looper.myLooper()
            )
        }

    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let {
                lat = it.latitude
                lon = it.longitude
            }
            isHaveLocation = true
            homeViewModel.getWeatherStatus(lat, lon)
        }
    }

}