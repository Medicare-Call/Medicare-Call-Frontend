package com.konkuk.medicarecall.ui.feature.settings.elderinfo.viewmodel

import com.konkuk.medicarecall.data.dto.response.EldersInfoResponseDto

data class SettingsEldersInfoUiState(
    val eldersInfoList: List<EldersInfoResponseDto> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
