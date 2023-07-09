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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marsad.R
import com.example.marsad.data.database.localdatasources.LocationLocalDataSource
import com.example.marsad.data.model.SavedLocation
import com.example.marsad.data.network.ApiState
import com.example.marsad.data.network.WeatherRemoteDataSource
import com.example.marsad.data.network.OpenWeatherMapResponse
import com.example.marsad.data.repositories.LocationRepository
import com.example.marsad.databinding.FragmentFavoritesBinding
import com.example.marsad.ui.favorites.viewmodel.FavoritesViewModel
import com.example.marsad.ui.favorites.viewmodel.SharedViewModel
import com.example.marsad.ui.utils.MyViewModelFactory
import com.example.marsad.ui.utils.UnitsUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment(), OnMapReadyCallback {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var locationLat: Double = 0.0
    private var locationLon: Double = 0.0
    lateinit var binding: FragmentFavoritesBinding
    lateinit var viewModel: FavoritesViewModel
    lateinit var locationItemAdapter: LocationItemAdapter
    private lateinit var mMap: GoogleMap
    private lateinit var mapView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.map_view)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.googleMapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
        initUI(view)
        setUpViewModel()
        getLocations()
        binding.addToFavFab.setOnClickListener {
            mapView.visibility = View.VISIBLE
            binding.addToFavFab.visibility = View.GONE
            binding.savedLocationsRv.visibility = View.GONE
        }
        binding.mapView.confrimFab.setOnClickListener {
            addNewLocation()
        }

        val itemTouchHelperCallback = LocationItemTouchHelperCallback(
            requireContext(), locationItemAdapter
        ) { deletedLocation ->
            viewModel.removeLocation(deletedLocation)

            Snackbar.make(
                binding.savedLocationsRv, "Deleted " + deletedLocation.city, Snackbar.LENGTH_LONG
            ).setAction("Undo") {
                viewModel.addLocation(deletedLocation)
            }.show()
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.savedLocationsRv)
        }

    }

    private fun addNewLocation() {
        lifecycleScope.launch {

            viewModel.locationWeatherStateFlow.collect { result ->
                when (result) {
                    is ApiState.Loading -> {
                        Toast.makeText(requireContext(), "Fetching......", Toast.LENGTH_LONG).show()
                    }
                    is ApiState.Success<*> -> {
                        val weatherInfo = result.weatherStatus as OpenWeatherMapResponse
                        saveLocation(weatherInfo)
                        mapView.visibility = View.GONE
                        binding.addToFavFab.visibility = View.VISIBLE
                        binding.savedLocationsRv.visibility = View.VISIBLE
                    }
                    else -> {
                        Toast.makeText(
                            requireContext(), "Failed To Fetch Data", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        }
    }

    private fun saveLocation(weatherInfo: OpenWeatherMapResponse) {
        val myLocation = SavedLocation(
            city = UnitsUtils.getCity(
                requireContext(), locationLat, locationLon
            ),

            lat = locationLat,
            lon = locationLon,
            description = weatherInfo.weather[0].description,
            icon = weatherInfo.weather[0].icon,
            lastTemp = weatherInfo.main.temp.toInt()
        )
        viewModel.addLocation(myLocation)
    }

    private fun getLocations() {
        viewModel.getSavedLocations().observe(requireActivity()) {
            view?.findViewById<View>(R.id.no_locations_view)?.apply {
                if (it.isEmpty()) {
                    visibility = View.VISIBLE
                } else {
                    locationItemAdapter.locations = it
                    visibility = View.GONE
                }
            }
        }
    }

    private fun setUpViewModel() {
        val viewModelFactory = MyViewModelFactory(
            LocationRepository.getInstance(
                WeatherRemoteDataSource, LocationLocalDataSource(requireContext())
            )
        )
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[FavoritesViewModel::class.java]
    }

    private fun initUI(view: View) {
        locationItemAdapter = LocationItemAdapter(
            listOf(), {
                sharedViewModel.setLocation(it)
                Navigation.findNavController(view)
                    .navigate(R.id.locationDetailsFragment)
            }, requireContext()
        )
        binding.savedLocationsRv.apply {
            adapter = locationItemAdapter
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = RecyclerView.VERTICAL
            }
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
            locationLat = it.latitude
            locationLon = it.longitude
            viewModel.getLocationWeather(it.latitude, it.longitude)
        }
    }
}