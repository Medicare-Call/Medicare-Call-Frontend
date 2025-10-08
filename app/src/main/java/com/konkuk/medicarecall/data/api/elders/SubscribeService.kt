package com.konkuk.medicarecall.data.api.elders

import com.konkuk.medicarecall.data.dto.response.EldersSubscriptionResponseDto
import retrofit2.Response
import retrofit2.http.GET

interface SubscribeService {
    // elders 구독 정보 조회
    @GET("elders/subscriptions")
    suspend fun getElderSubscriptions(): Response<List<EldersSubscriptionResponseDto>>
}
