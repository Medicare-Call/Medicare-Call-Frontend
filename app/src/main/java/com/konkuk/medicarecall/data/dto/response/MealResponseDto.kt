package com.konkuk.medicarecall.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class MealResponseDto(
    val date: String,
    val meals: MealData,
)

@Serializable
data class MealData(
    val breakfast: String?, // null 허용
    val lunch: String?,
    val dinner: String?,
)
