package com.example.marsad.ui.weatheralerts.view

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marsad.R
import com.example.marsad.data.database.localdatasources.AlertLocalDataSource
import com.example.marsad.data.model.AlertItem
import com.example.marsad.data.network.AlertResponse
import com.example.marsad.data.network.ApiState
import com.example.marsad.data.network.WeatherRemoteDataSource
import com.example.marsad.data.repositories.AlertsRepository
import com.example.marsad.databinding.FragmentWeatherAlertsBinding
import com.example.marsad.ui.utils.getDateAndTime
import com.example.marsad.ui.weatheralerts.AlertReceiver
import com.example.marsad.ui.weatheralerts.viewmodel.AlertViewModelFactory
import com.example.marsad.ui.weatheralerts.viewmodel.WeatherAlertsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class WeatherAlertsFragment : Fragment() {
    lateinit var binding: FragmentWeatherAlertsBinding
    lateinit var alertItemAdapter: AlertItemAdapter
    lateinit var viewModel: WeatherAlertsViewModel
    private var alarmManager: AlarmManager? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherAlertsBinding.inflate(inflater, container, false)
        alarmManager =
            requireContext().getSystemService(Context.ALARM_SERVICE) as? AlarmManager?
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        setUpViewModel()
        getActiveAlerts()
        collectAlertsResponse()

        binding.addNewAlertFab.setOnClickListener {
            requestBatteryOptimizationPermission()
            showBottomSheet()
        }

        setSwipeBehaviour()

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

    private fun setSwipeBehaviour() {
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
        swipedAlert.pendingIntent?.let {
            alarmManager?.cancel(it)
            Toast.makeText(requireContext(), "Alert Cancelled", Toast.LENGTH_SHORT).show()
        }
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
            AlertsRepository.getInstance(
                WeatherRemoteDataSource,
                AlertLocalDataSource(requireContext())
            )
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


    private fun showBottomSheet() {
        val modalBottomSheet = ModalBottomSheet()
        modalBottomSheet.show(requireActivity().supportFragmentManager, ModalBottomSheet.TAG)

    }

    private fun collectAlertsResponse() {
        lifecycleScope.launch {
            viewModel.weatherAlertsStateFlow.collect { result ->
                when (result) {
                    is ApiState.Success<*> -> {
                        val response = result.weatherStatus as AlertResponse
                        alertItemAdapter.alertItemList.forEach {
                            it.event = getString(R.string.no_alerts_msg)
                            it.description = getString(R.string.no_alerts_description)
                            response.alerts?.let { alerts ->
                                if (alerts.isNotEmpty()) {
                                    if (alertsExist(
                                            apiStart = alerts[0].start,
                                            apiEnd = alerts[0].end,
                                            localStart = it.start,
                                            localEnd = it.end
                                        )
                                    ) {
                                        it.event = alerts[0].event
                                        it.description = alerts[0].description
                                    }
                                }
                            }
                            scheduleAlarm(it)
                        }
                    }
                    is ApiState.Loading -> {}
                    else -> {
                        Toast.makeText(
                            requireContext(),
                            "Failed To Fetch Alerts $result",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }
    }

    private fun scheduleAlarm(alertItem: AlertItem) {
        val alarmIntent =
            Intent(requireActivity().applicationContext, AlertReceiver::class.java).apply {
                putExtra(getString(R.string.alert_id_key), alertItem.id)
                putExtra(getString(R.string.alert_title_key), alertItem.event)
                putExtra(getString(R.string.alert_description_key), alertItem.description)
                putExtra(getString(R.string.alert_type_key), alertItem.alertType)
            }
        val pendingIntent = PendingIntent.getBroadcast(
            requireActivity().applicationContext, 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        alertItem.pendingIntent = pendingIntent
        alarmManager?.set(
            AlarmManager.RTC_WAKEUP,
            alertItem.start,
            pendingIntent
        )
        Toast.makeText(
            requireContext(),
            "${getString(R.string.alert_scheduled)} \n${getDateAndTime(alertItem.start / 1000)}",
            Toast.LENGTH_LONG
        ).show()

    }

    private fun alertsExist(
        apiStart: Long,
        apiEnd: Long,
        localStart: Long,
        localEnd: Long
    ): Boolean {
        return (localStart in apiStart..apiEnd || localEnd in apiStart..apiEnd)
    }

}

