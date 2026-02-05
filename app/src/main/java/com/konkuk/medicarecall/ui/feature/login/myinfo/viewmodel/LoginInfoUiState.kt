package com.konkuk.medicarecall.ui.feature.login.myinfo.viewmodel

import com.konkuk.medicarecall.domain.model.UserInfo
import com.konkuk.medicarecall.ui.model.NavigationDestination

data class LoginInfoUiState(
    val verificationCode: String = "",
    val showBottomSheet: Boolean = false,
    val checkedStates: List<Boolean> = listOf(false, false),
    val allAgreeCheckState: Boolean = false,
    val navigationDestination: NavigationDestination? = null,
    val userInfo: UserInfo = UserInfo(),
)
