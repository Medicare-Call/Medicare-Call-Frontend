package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.data.dto.response.EldersSubscriptionResponseDto

interface SubscribeRepository {
    suspend fun getSubscriptions(): Result<List<EldersSubscriptionResponseDto>>
}
