package com.konkuk.medicarecall.ui.feature.settings.elderhealth.viewmodel

import com.konkuk.medicarecall.data.dto.response.EldersHealthResponseDto

data class SettingsElderHealthDetailUiState(
    val healthData: EldersHealthResponseDto? = null,
    val isLoading: Boolean = false,
    val isUpdateSuccess: Boolean = false,
    val errorMessage: String? = null,
)
