package com.konkuk.medicarecall.ui.feature.settings.elderhealth.viewmodel

import com.konkuk.medicarecall.data.dto.response.EldersHealthResponseDto

data class SettingsEldersHealthUiState(
    val eldersInfoList: List<EldersHealthResponseDto> = emptyList(),
    val errorMessage: String? = null,
)
