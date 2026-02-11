package com.konkuk.medicarecall.ui.feature.settings.elderhealth.viewmodel

import com.konkuk.medicarecall.domain.model.ElderHealthInfo

data class SettingsElderHealthDetailUiState(
    val healthData: ElderHealthInfo? = null,
    val isLoading: Boolean = false,
    val isUpdateSuccess: Boolean = false,
    val errorMessage: String? = null,
) {
    val hasData: Boolean get() = healthData != null
    val hasError: Boolean get() = errorMessage != null
}
