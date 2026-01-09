package com.konkuk.medicarecall.data.api.elders

import com.konkuk.medicarecall.data.dto.response.EldersSubscriptionResponseDto
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.GET

interface SubscribeService {
    // elders 구독 정보 조회
    @GET("elders/subscriptions")
    suspend fun getElderSubscriptions(): Response<List<EldersSubscriptionResponseDto>>
}
