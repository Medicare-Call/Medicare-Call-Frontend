package com.konkuk.medicarecall.domain.model

import com.konkuk.medicarecall.domain.model.type.GenderType

data class UserInfo(
    val name: String = "",
    val birthDate: String = "",
    val gender: GenderType = GenderType.MALE,
    val phoneNumber: String = "",
    val pushNotification: PushNotification = PushNotification(),
)
