package com.example.marsad.ui.home.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.marsad.NavGraphDirections
import com.example.marsad.R
import com.example.marsad.core.MapListener
import com.example.marsad.core.utils.GPS
import com.example.marsad.core.utils.MAP
import com.example.marsad.core.utils.UNDEFINED
import com.example.marsad.core.utils.hide
import com.example.marsad.core.utils.show
import com.example.marsad.core.utils.viewModelFactory
import com.example.marsad.data.repositories.UserPrefsRepository
import com.example.marsad.databinding.FragmentHomeBinding
import com.example.marsad.ui.home.LocationException
import com.example.marsad.ui.home.LocationManager
import com.example.marsad.ui.home.viewmodel.HomeViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    companion object {
        private const val TAG = "HomeFragment"
    }

    private val homeViewModel: HomeViewModel by viewModels(ownerProducer = { requireActivity() }) {
        viewModelFactory {
            HomeViewModel(
                UserPrefsRepository.getInstance(requireActivity())
            )
        }
    }

    private lateinit var locationManager: LocationManager

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        locationManager = LocationManager(
            context = requireContext(),
            client = LocationServices.getFusedLocationProviderClient(requireActivity().applicationContext),
            onLocationResult = this::navigateToDetails
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideContent()
        setUpMapView()

        binding.gpsBtn.setOnClickListener {
            homeViewModel.preferGPSMethod()
            getLastLocation()
        }
        binding.mapBtn.setOnClickListener {
            binding.mapLogo.hide()
            binding.progressBar.hide()
            binding.mapView.root.show()
        }
    }

    private fun navigateToDetails(latitude: Double, longitude: Double) {
        val action = NavGraphDirections.actionsHomeToDetails(
            lat = latitude.toFloat(),
            lon = longitude.toFloat()
        )
        Log.d(TAG, "onViewCreated: onLocationResult: $latitude, $longitude")
        //This check insures that the fragment is at leased started so we can navigate safely
        if (lifecycle.currentState in listOf(Lifecycle.State.STARTED, Lifecycle.State.RESUMED)) {
            findNavController().navigate(action)
        }
    }

    private fun setUpMapView() {
        binding.mapView.run {

            val mapFragment =
                childFragmentManager.findFragmentById(R.id.googleMapView) as SupportMapFragment
            var lat = 0.0
            var lon = 0.0
            MapListener(
                requireContext(),
            ) {
                lat = it.latitude
                lon = it.longitude
                confirmFab.show()
            }.apply {
                mapFragment.getMapAsync(this)
                this@run.searchView.setOnQueryTextListener(this)
            }

            confirmFab.setOnClickListener {
                homeViewModel.preferMapMethod()
                homeViewModel.onLocationChanged(lat, lon)
                navigateToDetails(lat, lon)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: Preferred")
        getLastLocation()
    }

    override fun onStop() {
        super.onStop()
        homeViewModel.updatePrefs()
    }

    private fun getLastLocation() {
        lifecycleScope.launch {
            homeViewModel.userPrefsState.collect { prefs ->
                when (prefs.preferredLocationMethod) {
                    MAP -> if (prefs.isFirstUse) binding.mapBtn.performClick()
                    else navigateToDetails(
                        prefs.lat,
                        prefs.lon
                    )

                    GPS -> try {
                        locationManager.getLastLocation()
                    } catch (e: LocationException) {
                        when (e) {
                            is LocationException.LocationDisabledException -> locationManager.askUserToEnableLocation()
                            is LocationException.LocationPermissionException -> locationManager.requestLocationPermissions(
                                requireActivity()
                            )
                        }
                    }

                    UNDEFINED -> {
                        showContent()
                        binding.progressBar.hide()
                    }
                }
            }
        }
    }

    private fun hideContent() {
        binding.run {
            mapLogo.hide()
            gpsBtn.hide()
            mapBtn.hide()
        }
    }

    private fun showContent() {
        binding.run {
            mapLogo.show()
            gpsBtn.show()
            mapBtn.show()
        }
    }
}