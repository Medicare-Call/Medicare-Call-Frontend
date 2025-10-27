package com.konkuk.medicarecall.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class CertificationCodeRequestDto(
    val phone: String,
)
