package com.example.marsad.ui.weatheralerts.view

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.marsad.R
import com.example.marsad.data.database.localdatasources.AlertLocalDataSource
import com.example.marsad.data.model.AlertItem
import com.example.marsad.data.model.AlertType
import com.example.marsad.data.network.WeatherRemoteDataSource
import com.example.marsad.data.repositories.AlertsRepository
import com.example.marsad.databinding.BottomSheetLayoutBinding
import com.example.marsad.ui.utils.getDateAndTime
import com.example.marsad.ui.weatheralerts.viewmodel.AlertViewModelFactory
import com.example.marsad.ui.weatheralerts.viewmodel.WeatherAlertsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class ModalBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetLayoutBinding
    private var startDate: Long = 0
    private var endDate: Long = 0
    lateinit var viewModel: WeatherAlertsViewModel
    private var calendar = Calendar.getInstance()

    companion object {
        const val TAG = "ModalBottomSheet"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.radioButtonAlarm.setOnClickListener {
            if (!Settings.canDrawOverlays(requireContext())) {
                startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION))
            }
        }
        setButtonsClickListeners()
        setUpViewModel()
    }

    private fun setButtonsClickListeners() {
        binding.pickStartDateButton.setOnClickListener {
            showDatePicker("start")
        }
        binding.pickEndDateButton.setOnClickListener {
            showDatePicker("end")
        }
        binding.saveButton.setOnClickListener {
            addNewAlert()
        }
    }

    private fun showDatePicker(target: String) {
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val myCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        myCalendar.timeInMillis = today
        val thisMonth = myCalendar.timeInMillis
        val calendarConstraints = CalendarConstraints.Builder()
            .setStart(thisMonth)
            .setValidator(DateValidatorPointForward.now())

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setCalendarConstraints(calendarConstraints.build())
                .setSelection(calendar.timeInMillis)
                .setTitleText(
                    when (target) {
                        "start" -> "Select Start Date"
                        else -> "Select End Date"
                    }
                )
                .build()
        datePicker.addOnPositiveButtonClickListener {

            calendar.timeInMillis = it
            showTimePicker(target)

        }

        datePicker.show(requireActivity().supportFragmentManager, "tag")
    }

    private fun showTimePicker(target: String) {
        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(calendar[Calendar.HOUR_OF_DAY])
                .setMinute(calendar[Calendar.MINUTE])
                .setTitleText("Select Start Time ")
                .build()
        picker.addOnPositiveButtonClickListener {
            calendar[Calendar.HOUR_OF_DAY] = picker.hour
            calendar[Calendar.MINUTE] = picker.minute
            when (target) {
                "start" -> {
                    startDate = calendar.timeInMillis
                    binding.pickStartDateButton.text = getDateAndTime(startDate / 1000)
                }
                "end" -> {
                    endDate = calendar.timeInMillis
                    binding.pickEndDateButton.text = getDateAndTime(endDate / 1000)
                }
            }
        }
        picker.show(requireActivity().supportFragmentManager, "tag")
    }

    private fun addNewAlert() {

        if (startDate.toInt() == 0 || endDate.toInt() == 0) {
            Toast.makeText(requireContext(), "Please Choose Start & End Dates", Toast.LENGTH_SHORT)
                .show()
        } else if (startDate > endDate) {
            Toast.makeText(requireContext(), "Start Must be before End", Toast.LENGTH_SHORT)
                .show()
        } else {
            val currentAlert =
                AlertItem(System.currentTimeMillis(), getAlertType(), startDate, endDate)
            viewModel.addAlert(currentAlert)
            getWeatherAlerts()

            this@ModalBottomSheet.dismiss()
        }
    }

    private fun registerForOverlayPermission(currentAlert: AlertItem): ActivityResultLauncher<String> {
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                viewModel.addAlert(currentAlert)
                getWeatherAlerts()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Overlay permission denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return requestPermissionLauncher
    }

    private fun getWeatherAlerts() {
        val currentLocation = getCurrentStoredLocation()
        viewModel.getWeatherAlerts(currentLocation.latitude, currentLocation.longitude)
    }

    private fun getCurrentStoredLocation(): Location {
        val sharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.preferences_file_key), Context.MODE_PRIVATE
        )
        return Location("").apply {
            latitude =
                sharedPreferences.getString(
                    getString(R.string.latitude_key),
                    null
                )
                    ?.toDouble() ?: 0.0

            longitude =
                sharedPreferences.getString(
                    getString(R.string.longitude_key),
                    null
                )
                    ?.toDouble() ?: 0.0
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

    private fun getAlertType() = when (binding.radioGroup.checkedRadioButtonId) {
        binding.radioButtonNotification.id -> AlertType.NOTIFICATION
        binding.radioButtonAlarm.id -> AlertType.ALARM
        else -> "Non Determined"
    }
}