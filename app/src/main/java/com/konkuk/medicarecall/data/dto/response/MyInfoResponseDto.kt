package com.konkuk.medicarecall.data.dto.response

import com.konkuk.medicarecall.domain.model.type.GenderType
import kotlinx.serialization.Serializable

@Serializable
data class MyInfoResponseDto(
    val name: String = "",
    val birthDate: String = "",
    val gender: GenderType = GenderType.MALE,
    val phone: String = "",
    val pushNotification: PushNotificationDto = PushNotificationDto("", "", "", ""),
)

@Serializable
data class PushNotificationDto(
    val all: String,
    val carecallCompleted: String,
    val healthAlert: String,
    val carecallMissed: String,
)
