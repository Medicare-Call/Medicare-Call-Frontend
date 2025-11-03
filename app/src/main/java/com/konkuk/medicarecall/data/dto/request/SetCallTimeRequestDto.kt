package com.konkuk.medicarecall.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class SetCallTimeRequestDto(
    val firstCallTime: String,
    val secondCallTime: String,
    val thirdCallTime: String,
)
