package com.konkuk.medicarecall.data.dto.request

import com.konkuk.medicarecall.ui.type.GenderType
import kotlinx.serialization.Serializable

@Serializable
data class MemberRegisterRequestDto(
    val name: String,
    val birthDate: String,
    val gender: GenderType, // MALE, FEMALE
)
