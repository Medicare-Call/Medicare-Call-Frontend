package com.konkuk.medicarecall.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Token(
    val accessToken: String? = null,
    val refreshToken: String? = null,
)
