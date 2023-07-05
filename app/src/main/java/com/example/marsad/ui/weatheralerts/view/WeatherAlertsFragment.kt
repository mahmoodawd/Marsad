package com.example.marsad.ui.weatheralerts.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marsad.R
import com.example.marsad.data.database.localdatasources.AlertLocalDataSource
import com.example.marsad.data.model.AlertItem
import com.example.marsad.data.repositories.AlertRepository
import com.example.marsad.databinding.FragmentWeatherAlertsBinding
import com.example.marsad.ui.favorites.view.LocationItemTouchHelperCallback
import com.example.marsad.ui.weatheralerts.viewmodel.AlertViewModelFactory
import com.example.marsad.ui.weatheralerts.viewmodel.WeatherAlertsViewModel
import com.google.android.material.snackbar.Snackbar

class WeatherAlertsFragment : Fragment() {
    lateinit var binding: FragmentWeatherAlertsBinding
    lateinit var alertItemAdapter: AlertItemAdapter
    lateinit var viewModel: WeatherAlertsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherAlertsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        setUpViewModel()
        getActiveAlerts()
        binding.addNewAlertFab.setOnClickListener { showBottomSheet(view) }

        val itemTouchHelperCallback = AlertItemTouchHelperCallback(
            requireContext(), alertItemAdapter
        ) { swipedAlert ->
            performSwiping(swipedAlert)
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.activeAlertsRv)
        }

    }

    private fun performSwiping(swipedAlert: AlertItem) {
        viewModel.removeAlert(swipedAlert)

        Snackbar.make(
            binding.activeAlertsRv, "Alert Deleted", Snackbar.LENGTH_LONG
        ).setAction("Undo") {
            viewModel.addAlert(swipedAlert)
        }.show()
    }


    private fun getActiveAlerts() {
        viewModel.getActiveAlerts().observe(requireActivity()) {
            view?.findViewById<View>(R.id.no_alerts_view)?.apply {
                if (it.isEmpty()) {
                    visibility = View.VISIBLE
                } else {
                    alertItemAdapter.alertItemList = it
                    visibility = View.GONE
                }
            }
        }
    }

    private fun setUpViewModel() {
        val factory = AlertViewModelFactory(
            AlertRepository.getInstance(AlertLocalDataSource(requireContext()))
        )
        viewModel =
            ViewModelProvider(requireActivity(), factory)[WeatherAlertsViewModel::class.java]
    }

    private fun setUpAdapter() {
        alertItemAdapter = AlertItemAdapter(listOf(), requireContext())
        binding.activeAlertsRv.apply {
            adapter = alertItemAdapter
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = RecyclerView.VERTICAL
            }

        }
    }


    private fun showBottomSheet(view: View) {
        /*val bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()*/

        val modalBottomSheet = ModalBottomSheet()
        modalBottomSheet.show(requireActivity().supportFragmentManager, ModalBottomSheet.TAG)

    }
}

