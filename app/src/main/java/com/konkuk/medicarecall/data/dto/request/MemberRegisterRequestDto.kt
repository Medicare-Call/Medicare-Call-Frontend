package com.konkuk.medicarecall.data.dto.request

import com.konkuk.medicarecall.domain.model.type.GenderType
import kotlinx.serialization.Serializable

@Serializable
data class MemberRegisterRequestDto(
    val name: String,
    val birthDate: String,
    val gender: GenderType, // MALE, FEMALE
    val fcmToken: String,
)
