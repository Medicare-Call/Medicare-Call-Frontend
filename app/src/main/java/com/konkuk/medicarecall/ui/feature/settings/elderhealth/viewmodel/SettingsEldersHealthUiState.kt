package com.konkuk.medicarecall.ui.feature.settings.elderhealth.viewmodel

import com.konkuk.medicarecall.domain.model.ElderHealthInfo

data class SettingsEldersHealthUiState(
    val eldersInfoList: List<ElderHealthInfo> = emptyList(),
    val errorMessage: String? = null,
) {
    val hasElders: Boolean get() = eldersInfoList.isNotEmpty()
    val hasError: Boolean get() = errorMessage != null
}
