package com.konkuk.medicarecall.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class CallTimeResponseDto(
    val firstCallTime: String,
    val secondCallTime: String,
    val thirdCallTime: String,
)
