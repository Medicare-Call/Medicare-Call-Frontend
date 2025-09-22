package com.konkuk.medicarecall.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class ImmediateCallRequestDto(
    val elderId: Int,
    val careCallOption: String, // FIRST, SECOND, THIRD
)
