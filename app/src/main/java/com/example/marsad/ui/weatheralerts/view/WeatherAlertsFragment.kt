package com.example.marsad.ui.weatheralerts.view

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.marsad.R
import com.example.marsad.core.notifications.AlarmHelper
import com.example.marsad.core.utils.showSnackbar
import com.example.marsad.core.utils.viewModelFactory
import com.example.marsad.core.utils.visibleIf
import com.example.marsad.data.database.localdatasources.AlertLocalDataSource
import com.example.marsad.data.network.remotesource.WeatherRemoteDataSource
import com.example.marsad.data.repositories.AlertsRepository
import com.example.marsad.databinding.FragmentWeatherAlertsBinding
import com.example.marsad.domain.models.Alert
import com.example.marsad.ui.weatheralerts.viewmodel.WeatherAlertsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WeatherAlertsFragment : Fragment() {
    lateinit var binding: FragmentWeatherAlertsBinding
    private val alarmHelper: AlarmHelper by lazy {
        AlarmHelper(
            requireContext(),
            requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        )
    }

    private val viewModel: WeatherAlertsViewModel by viewModels(ownerProducer = { requireActivity() }) {
        viewModelFactory {
            WeatherAlertsViewModel(
                repository = AlertsRepository.getInstance(
                    WeatherRemoteDataSource, AlertLocalDataSource(requireContext())
                )
            )
        }
    }
    private val alertItemAdapter: AlertItemAdapter by lazy {
        AlertItemAdapter(listOf())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentWeatherAlertsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.activeAlertsRv.adapter = alertItemAdapter
        binding.addNewAlertFab.setOnClickListener {
            requestBatteryOptimizationPermission()
            showBottomSheet()
        }

        getActiveAlerts()

        setSwipeBehaviour(
            onSwipe = {
                viewModel.removeAlert(it)
                alarmHelper.cancelAlert(it.id)
            },
            onUndo = viewModel::addAlert
        )

    }

    @SuppressLint("BatteryLife")
    private fun requestBatteryOptimizationPermission() {
        val packageName = requireActivity().packageName
        val powerManager =
            requireActivity().getSystemService(Context.POWER_SERVICE) as PowerManager
        if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
            Intent().apply {
                action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                data = Uri.parse("package:".plus(packageName))
                startActivity(this)
            }
        }
    }

    private fun setSwipeBehaviour(
        onSwipe: (Alert) -> Unit,
        onUndo: (Alert) -> Unit,
    ) {
        val itemTouchHelperCallback = AlertItemTouchHelperCallback(
            requireContext(), alertItemAdapter
        ) { swipedAlert ->
            onSwipe(swipedAlert)
            showSnackbar(
                msg = R.string.item_deleted,
                actionLabel = R.string.undo,
                action = { onUndo(swipedAlert) })
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.activeAlertsRv)
        }
    }


    private fun getActiveAlerts() {
        lifecycleScope.launch {
            viewModel.alertsUiState.collectLatest { alertsUiState ->
                binding.noAlertsView.root visibleIf (alertsUiState is AlertsUiState.Empty)
                //TODO Add loading spinner
                //binding.loading visibleIf (alertsUiState is AlertsUiState.Loading)
                when (alertsUiState) {

                    is AlertsUiState.Error -> showSnackbar(R.string.error_getting_data)

                    is AlertsUiState.Success -> {
                        alertItemAdapter.alertItemList =
                            alertsUiState.alerts
                    }

                    else -> {}
                }
            }
        }
    }


    private fun showBottomSheet() {
        val newAlertBottomSheet = NewAlertBottomSheet(onSave = viewModel::addAlert)
        newAlertBottomSheet.show(
            requireActivity().supportFragmentManager,
            NewAlertBottomSheet.TAG
        )
    }

    override fun onResume() {
        super.onResume()
        startObservingAlerts()
    }

    private fun startObservingAlerts() {
        lifecycleScope.launch {
            viewModel.weatherAlerts.collectLatest { alerts ->
                Log.i(TAG, "collectAlertsResponse: $alerts")
                alerts.forEach {
                    alarmHelper.scheduleAlert(it)
                }
            }
        }
    }

    companion object {
        private val TAG = WeatherAlertsFragment::class.java.simpleName
    }
}
