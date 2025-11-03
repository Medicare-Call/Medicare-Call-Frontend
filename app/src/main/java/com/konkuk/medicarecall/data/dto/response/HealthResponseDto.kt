package com.konkuk.medicarecall.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class HealthResponseDto(
    val date: String,
    val symptomList: List<String>? = null,
    val analysisComment: String? = null,
)
