package com.konkuk.medicarecall.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class VerificationResponseDto(
    val verified: Boolean,
    val message: String,
    val memberStatus: String,
    val token: String?,
    val accessToken: String?,
    val refreshToken: String?,
)
