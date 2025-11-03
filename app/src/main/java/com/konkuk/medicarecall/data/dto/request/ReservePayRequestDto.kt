package com.konkuk.medicarecall.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class ReservePayRequestDto(
    val productName: String,
    val productCount: Int,
    val totalPayAmount: Int,
    val taxScopeAmount: Int,
    val taxExScopeAmount: Int,
    val elderIds: List<Int>,
)
