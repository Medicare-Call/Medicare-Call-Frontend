package com.konkuk.medicarecall.ui.feature.login.elder.viewmodel

sealed interface LoginElderEvent {
    data object NavigateToCareCallSetting : LoginElderEvent
}
