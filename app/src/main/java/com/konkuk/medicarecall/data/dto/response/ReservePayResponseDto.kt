package com.konkuk.medicarecall.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class ReservePayResponseDto(
    val code: String,
    val message: String,
    val body: ReserveBody,
)

@Serializable
data class ReserveBody(
    val code: String, // 주문코드(or merchantPayKey)
)
