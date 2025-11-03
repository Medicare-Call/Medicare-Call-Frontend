package com.konkuk.medicarecall.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val status: Int,
    val error: String? = null,
    val message: String? = null,
    val data: T? = null,
)
