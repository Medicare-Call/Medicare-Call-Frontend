package com.konkuk.medicarecall.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class EldersSubscriptionResponseDto(
    val elderId: Long,
    val name: String,
    val plan: String,
    val price: Int,
    val nextBillingDate: String,
    val startDate: String,
)
