package com.example.marsad.ui.weatheralerts.view

import com.example.marsad.domain.models.Alert

sealed class AlertsUiState {
    data class Success(val alerts: List<Alert>) : AlertsUiState()
    data object Loading : AlertsUiState()
    data object Empty : AlertsUiState()
    data object Error : AlertsUiState()
}