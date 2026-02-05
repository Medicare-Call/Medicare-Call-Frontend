package com.konkuk.medicarecall.ui.feature.login.myinfo.viewmodel

import com.konkuk.medicarecall.domain.model.UserInfo
import com.konkuk.medicarecall.ui.model.NavigationDestination
import com.konkuk.medicarecall.ui.type.GenderType

data class LoginInfoUiState(
    val phoneNumber: String = "", // userInfo
    val verificationCode: String = "",
    val name: String = "", // userInfo
    val dateOfBirth: String = "", // userInfo
    val gender: GenderType = GenderType.MALE, // userInfo
    val showBottomSheet: Boolean = false,
    val checkedStates: List<Boolean> = listOf(false, false),
    val allAgreeCheckState: Boolean = false,
    val navigationDestination: NavigationDestination? = null,

    val userInfo: UserInfo = UserInfo(),
)
