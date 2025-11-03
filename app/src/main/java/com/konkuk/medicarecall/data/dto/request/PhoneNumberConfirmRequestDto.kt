package com.konkuk.medicarecall.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class PhoneNumberConfirmRequestDto(
    val phone: String,
    val certificationCode: String,
)
