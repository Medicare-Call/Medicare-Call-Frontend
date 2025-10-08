package com.konkuk.medicarecall.ui.feature.login.senior.viewmodel

import com.konkuk.medicarecall.ui.model.ElderData

data class LoginElderUiState(
    val eldersList: List<ElderData> = listOf(ElderData()),
    val selectedIndex: Int = 0,
)
