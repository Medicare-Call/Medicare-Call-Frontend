package com.konkuk.medicarecall.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class ElderRegisterResponseDto(
    val id: Int,
    val name: String,
)
