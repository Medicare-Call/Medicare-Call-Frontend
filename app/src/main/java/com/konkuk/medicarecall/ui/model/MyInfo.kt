package com.konkuk.medicarecall.ui.model

import com.konkuk.medicarecall.domain.model.type.GenderType

data class MyInfo(
    val name: String = "",
    val birthDate: String = "",
    val gender: GenderType = GenderType.MALE,
    val phone: String = "",
    val pushNotification: PushNotification = PushNotification(),
)
