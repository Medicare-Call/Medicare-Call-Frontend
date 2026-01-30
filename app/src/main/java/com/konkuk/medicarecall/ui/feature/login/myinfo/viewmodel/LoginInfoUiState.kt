package com.konkuk.medicarecall.ui.feature.login.myinfo.viewmodel

import com.konkuk.medicarecall.ui.model.NavigationDestination

data class LoginInfoUiState(
    val phoneNumber: String = "",
    val verificationCode: String = "",
    val name: String = "",
    val dateOfBirth: String = "",
    val isMale: Boolean = true,
    val showBottomSheet: Boolean = false,
    val checkedStates: List<Boolean> = listOf(false, false),
    val allAgreeCheckState: Boolean = false,
    val navigationDestination: NavigationDestination? = null,
)
