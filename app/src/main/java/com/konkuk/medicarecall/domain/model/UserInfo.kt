package com.konkuk.medicarecall.domain.model

import com.konkuk.medicarecall.ui.type.GenderType

data class UserInfo(
    val name: String = "",
    val birthDate: String = "",
    val gender: GenderType = GenderType.MALE,
    val phoneNumber: String = "",
)
