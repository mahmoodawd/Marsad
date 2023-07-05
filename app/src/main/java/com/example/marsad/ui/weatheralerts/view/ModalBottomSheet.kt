package com.example.marsad.ui.weatheralerts.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.marsad.data.database.localdatasources.AlertLocalDataSource
import com.example.marsad.data.model.AlertItem
import com.example.marsad.data.model.AlertType
import com.example.marsad.data.repositories.AlertRepository
import com.example.marsad.databinding.BottomSheetLayoutBinding
import com.example.marsad.ui.utils.getDate
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
    private lateinit var alertType: String
    lateinit var viewModel: WeatherAlertsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewModel()
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

    private fun addNewAlert() {
        alertType = when (binding.radioGroup.checkedRadioButtonId) {
            binding.radioButtonNotification.id -> AlertType.NOTIFICATION
            binding.radioButtonAlarm.id -> AlertType.ALARM
            else -> "Non Determined"
        }
        val currentAlert = AlertItem(startDate, endDate, alertType)
        if (startDate.toInt() == 0 || endDate.toInt() == 0) {
            Toast.makeText(requireContext(), "Please Choose Start & End Dates", Toast.LENGTH_SHORT)
                .show()
        } else if (startDate > endDate) {
            Toast.makeText(requireContext(), "Start Must be before End", Toast.LENGTH_SHORT)
                .show()
        } else {
            viewModel.addAlert(currentAlert)
            this@ModalBottomSheet.dismiss()
        }
    }

    private fun setUpViewModel() {
        val factory = AlertViewModelFactory(
            AlertRepository.getInstance(AlertLocalDataSource(requireContext()))
        )
        viewModel =
            ViewModelProvider(requireActivity(), factory)[WeatherAlertsViewModel::class.java]
    }

    private fun showDatePicker(type: String) {
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
                .setTitleText(
                    when (type) {
                        "start" -> "Select Start Date"
                        else -> "Select End Date"
                    }
                )
                .build()
        datePicker.addOnPositiveButtonClickListener {
            when (type) {
                "start" -> {
                    binding.pickStartDateButton.text = getDate(it)
                    startDate = it
                }
                else -> {
                    binding.pickEndDateButton.text = getDate(it)
                    endDate = it
                }
            }
        }

        datePicker.show(requireActivity().supportFragmentManager, "tag")
    }

    private fun showTimePicker() {
        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Select Start Time ")
                .build()
        picker.addOnPositiveButtonClickListener {
            binding.pickStartDateButton.text = picker.hour.toString()
        }
        picker.show(requireActivity().supportFragmentManager, "tag")
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}