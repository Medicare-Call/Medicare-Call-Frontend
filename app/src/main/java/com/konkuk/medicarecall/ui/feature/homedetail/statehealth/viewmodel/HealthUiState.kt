package com.konkuk.medicarecall.ui.feature.homedetail.statehealth.viewmodel

data class HealthUiState(
    val symptoms: List<String> = emptyList(),
    val symptomAnalysis: String = "",
    val isRecorded: Boolean = false,
) {
    companion object {
        val EMPTY = HealthUiState()
    }
}

