package com.konkuk.medicarecall.ui.feature.settings.menu.viewmodel

import com.konkuk.medicarecall.domain.model.UserInfo

data class SettingsMenuUiState(
    val userInfo: UserInfo = UserInfo(),
)
