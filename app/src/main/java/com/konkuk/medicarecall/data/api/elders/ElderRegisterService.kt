package com.konkuk.medicarecall.data.api.elders

import com.konkuk.medicarecall.data.dto.request.ElderBulkHealthInfoRequestDto
import com.konkuk.medicarecall.data.dto.request.ElderBulkRegisterRequestDto
import com.konkuk.medicarecall.data.dto.request.ElderHealthRegisterRequestDto
import com.konkuk.medicarecall.data.dto.request.ElderRegisterRequestDto
import com.konkuk.medicarecall.data.dto.response.ElderBulkRegisterResponseDto
import com.konkuk.medicarecall.data.dto.response.ElderRegisterResponseDto
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path

interface ElderRegisterService {
    @POST("elders")
    suspend fun postElder(
        @Body request: ElderRegisterRequestDto,
    ): Response<ElderRegisterResponseDto>

    @POST("elders/{elderId}/health-info")
    suspend fun postElderHealthInfo(
        @Path("elderId") elderId: Long,
        @Body request: ElderHealthRegisterRequestDto,
    ): Response<Unit>

    @POST("elders/bulk")
    suspend fun postElderBulk(
        @Body request: ElderBulkRegisterRequestDto,
    ): Response<ElderBulkRegisterResponseDto>

    @POST("elders/health-info/bulk")
    suspend fun postElderHealthInfoBulk(
        @Body request: ElderBulkHealthInfoRequestDto,
    ): Response<Unit>
}
