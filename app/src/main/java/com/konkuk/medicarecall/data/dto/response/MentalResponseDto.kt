package com.konkuk.medicarecall.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class MentalResponseDto(
    val date: String,
    val commentList: List<String>? = null,
)
