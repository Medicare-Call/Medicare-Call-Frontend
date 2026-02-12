package com.konkuk.medicarecall.ui.feature.settings.elderinfo.viewmodel

import com.konkuk.medicarecall.domain.model.ElderInfo

data class SettingsEldersInfoUiState(
    val eldersInfoList: List<ElderInfo> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) {
    val hasElders: Boolean get() = eldersInfoList.isNotEmpty()
    val hasError: Boolean get() = errorMessage != null
}
