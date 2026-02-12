package com.konkuk.medicarecall.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class ImmediateCallRequestDto(
    val elderId: Long,
    val careCallOption: String, // FIRST, SECOND, THIRD
)
