package com.konkuk.medicarecall.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenRefreshRequestDto(
    @SerialName("Refresh-Token")
    val refreshToken: String,
)
