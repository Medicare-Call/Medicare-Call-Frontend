package com.konkuk.medicarecall.data.api.elders

import com.konkuk.medicarecall.data.dto.request.ElderRegisterRequestDto
import com.konkuk.medicarecall.data.dto.response.CallTimeResponseDto
import com.konkuk.medicarecall.data.dto.response.EldersHealthResponseDto
import com.konkuk.medicarecall.data.dto.response.EldersInfoResponseDto
import com.konkuk.medicarecall.data.dto.response.EldersSubscriptionResponseDto
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path


interface EldersInfoService {
    @GET("elders")
    suspend fun getElders(): Response<List<EldersInfoResponseDto>>

    @GET("elders/subscriptions")
    suspend fun getSubscriptions(): Response<List<EldersSubscriptionResponseDto>>

    @POST("elders/{elderId}")
    suspend fun updateElder(
        @Path("elderId") elderId: Int,
        @Body request: ElderRegisterRequestDto,
    ): Response<EldersInfoResponseDto>

    // 노인 개인정보 삭제
    @DELETE("elders/{elderId}")
    suspend fun deleteElderSettings(
        @Path("elderId") elderId: Int,
    ): Response<Unit>

    // 노인 건강정보 조회
    @GET("elders/health-info")
    suspend fun getElderHealthInfo(): Response<List<EldersHealthResponseDto>>

    @GET("elders/{elderId}/care-call-setting")
    suspend fun getCallTimes(
        @Path("elderId") elderId: Int,
    ): Response<CallTimeResponseDto>
}
