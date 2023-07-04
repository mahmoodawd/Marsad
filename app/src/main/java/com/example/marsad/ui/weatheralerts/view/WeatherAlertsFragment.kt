package com.example.marsad.ui.weatheralerts.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marsad.R
import com.example.marsad.data.model.AlertItem
import com.example.marsad.data.model.AlertType
import com.example.marsad.databinding.FragmentWeatherAlertsBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog

class WeatherAlertsFragment : Fragment() {
    lateinit var binding: FragmentWeatherAlertsBinding
    lateinit var alertItemAdapter: AlertItemAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherAlertsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter(view)
        binding.addNewAlertFab.setOnClickListener { showBottomSheet(it) }
    }

    private fun setUpAdapter(view: View) {
        val alertItemList = listOf(
            AlertItem(1625428800, 1625439600, AlertType.ALARM),
            AlertItem(1625884800, 1625892000, AlertType.NOTIFICATION),
            AlertItem(1626278400, 1626285600, AlertType.ALARM),
            AlertItem(1626447600, 1626458400, AlertType.NOTIFICATION),
            AlertItem(1626980400, 1626984000, AlertType.ALARM),
            AlertItem(1627401600, 1627412400, AlertType.NOTIFICATION),
            AlertItem(1628827200, 1628838000, AlertType.ALARM),
            AlertItem(1629103200, 1629114000, AlertType.NOTIFICATION),
            AlertItem(1629518400, 1629525600, AlertType.ALARM),
            AlertItem(1629651600, 1629662400, AlertType.NOTIFICATION),
            AlertItem(1626980400, 1626984000, AlertType.ALARM),
            AlertItem(1627401600, 1627412400, AlertType.NOTIFICATION),
            AlertItem(1628827200, 1628838000, AlertType.ALARM),
            AlertItem(1629103200, 1629114000, AlertType.NOTIFICATION),
            AlertItem(1629518400, 1629525600, AlertType.ALARM),
            AlertItem(1629651600, 1629662400, AlertType.NOTIFICATION)
        )
        alertItemAdapter = AlertItemAdapter(alertItemList, requireContext())
        if (alertItemAdapter.alertItemList.isEmpty()) {
            view.findViewById<View>(R.id.no_alerts_view).visibility = View.VISIBLE
        } else {
            view.findViewById<View>(R.id.no_alerts_view).visibility = View.GONE
        }
        binding.activeAlertsRv.apply {
            adapter = alertItemAdapter
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = RecyclerView.VERTICAL
            }

        }
    }

    private fun showBottomSheet(view: View) {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()

    }
}
