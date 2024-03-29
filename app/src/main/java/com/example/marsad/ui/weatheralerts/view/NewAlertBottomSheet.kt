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
import com.example.marsad.R
import com.example.marsad.core.utils.getDateAndTime
import com.example.marsad.databinding.BottomSheetLayoutBinding
import com.example.marsad.domain.models.Alert
import com.example.marsad.domain.models.AlertType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar
import java.util.TimeZone

class NewAlertBottomSheet(val onSave: (Alert) -> Unit) : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetLayoutBinding
    private var startDate: Long = 0
    private var endDate: Long = 0
    private var calendar = Calendar.getInstance()

    companion object {
        const val TAG = "ModalBottomSheet"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
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
        val myCalendar = Calendar.getInstance(TimeZone.getTimeZone(TimeZone.getAvailableIDs()[0]))
        myCalendar.timeInMillis = today
        val thisMonth = myCalendar.timeInMillis
        val calendarConstraints = CalendarConstraints.Builder()
            .setStart(thisMonth).setFirstDayOfWeek(Calendar.SATURDAY)
            .setValidator(DateValidatorPointForward.now())

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setCalendarConstraints(calendarConstraints.build())
                .setSelection(calendar.timeInMillis)
                .setTitleText(
                    when (target) {
                        "start" -> R.string.select_start_date
                        else -> R.string.select_end_date
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
                .setTitleText(R.string.select_time)
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
            Toast.makeText(requireContext(), R.string.select_dates_warning, Toast.LENGTH_SHORT)
                .show()
        } else if (startDate > endDate) {
            Toast.makeText(requireContext(), R.string.invalid_date_range, Toast.LENGTH_SHORT)
                .show()
        } else {
            val currentAlert =
                Alert(
                    type = getAlertType(),
                    startTime = startDate.toInt(),
                    endTime = endDate.toInt(),
                    city = "",
                    lat = 0.0,
                    lon = 0.0
                )
            onSave(currentAlert)
            this@NewAlertBottomSheet.dismiss()
        }
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


    private fun getAlertType() = when (binding.radioGroup.checkedRadioButtonId) {
        binding.radioButtonNotification.id -> AlertType.NOTIFICATION
        binding.radioButtonAlarm.id -> AlertType.ALARM
        else -> "Non Determined"
    }
}