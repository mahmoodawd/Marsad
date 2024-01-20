package com.example.marsad.ui.favorites.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.marsad.R
import com.example.marsad.core.MapListener
import com.example.marsad.core.utils.getCity
import com.example.marsad.core.utils.hide
import com.example.marsad.core.utils.show
import com.example.marsad.core.utils.showSnackbar
import com.example.marsad.core.utils.viewModelFactory
import com.example.marsad.core.utils.visibleIf
import com.example.marsad.data.database.localdatasources.LocationLocalDataSource
import com.example.marsad.data.repositories.LocationRepository
import com.example.marsad.databinding.FragmentFavoritesBinding
import com.example.marsad.domain.models.FavoriteLocation
import com.example.marsad.ui.favorites.viewmodel.FavoritesViewModel
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {
    private var locationLat: Double = 0.0
    private var locationLon: Double = 0.0
    private lateinit var binding: FragmentFavoritesBinding

    private val viewModel: FavoritesViewModel by viewModels(ownerProducer = { requireActivity() }) {
        viewModelFactory {
            FavoritesViewModel(
                repository = LocationRepository.getInstance(
                    LocationLocalDataSource(requireContext())
                )
            )
        }
    }
    private val locationItemAdapter: LocationItemAdapter by lazy {
        LocationItemAdapter(
            locationsItems = emptyList(),
            onItemClick = {
                val action = FavoritesFragmentDirections.actionFavoritesToDetails(
                    lat = it.lat.toFloat(),
                    lon = it.lon.toFloat()
                )
                findNavController().navigate(action)
            },
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.savedLocationsRv.adapter = locationItemAdapter
        binding.addToFavFab.setOnClickListener {
            binding.mapView.root.show()
            binding.addToFavFab.hide()
        }

        getLocations()
        setSwipeBehaviour(
            onSwipe = viewModel::removeLocation,
            onUndo = viewModel::addLocation
        )
        setUpMap()

    }

    private fun getLocations() {
        lifecycleScope.launch {
            viewModel.favLocationsUiState.collectLatest { favUiState ->

                binding.noLocationsView.root visibleIf (favUiState is FavUiState.Empty)
                //TODO Add loading spinner
                //binding.loading visibleIf (favUiState is FavUiState.Loading)
                when (favUiState) {
                    is FavUiState.Error -> showSnackbar(R.string.error_getting_data)

                    is FavUiState.Success -> locationItemAdapter.locations =
                        favUiState.favoriteLocations

                    else -> {}
                }

            }
        }
    }

    private fun setSwipeBehaviour(
        onSwipe: (FavoriteLocation) -> Unit,
        onUndo: (FavoriteLocation) -> Unit,
    ) {
        val itemTouchHelperCallback = LocationItemTouchHelperCallback(
            requireContext(), locationItemAdapter
        ) { location ->
            onSwipe(location)
            showSnackbar(
                msg = R.string.item_deleted,
                actionLabel = R.string.undo,
                action = { onUndo(location) })
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.savedLocationsRv)
        }
    }

    private fun setUpMap() {
        binding.mapView.run {

            val mapFragment =
                childFragmentManager.findFragmentById(R.id.googleMapView) as SupportMapFragment

            MapListener(
                requireContext(),
            ) {
                locationLat = it.latitude
                locationLon = it.longitude
                confirmFab.show()
            }.apply {
                mapFragment.getMapAsync(this)
                searchView.setOnQueryTextListener(this)
            }
            confirmFab.setOnClickListener {
                viewModel.addLocation(
                    FavoriteLocation(
                        city = getCity(
                            requireContext(), locationLat, locationLon
                        ),
                        country = "",
                        lat = locationLat,
                        lon = locationLon,
                    )
                )
                root.hide()
                binding.addToFavFab.show()
            }
        }
    }

}