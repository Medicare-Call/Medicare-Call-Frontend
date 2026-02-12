package com.konkuk.medicarecall.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class NoticesResponseDto(
    val id: Long,
    val title: String,
    val author: String,
    val contents: String,
    val publishedAt: String,
)
