package com.konkuk.medicarecall.ui.feature.settings.menu.viewmodel

import com.konkuk.medicarecall.data.dto.response.MyInfoResponseDto

data class SettingsMenuUiState(
    val myInfo: MyInfoResponseDto = MyInfoResponseDto(),
)
