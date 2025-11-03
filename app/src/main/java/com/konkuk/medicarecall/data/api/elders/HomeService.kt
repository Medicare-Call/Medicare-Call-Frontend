package com.konkuk.medicarecall.data.api.elders

import com.konkuk.medicarecall.data.dto.request.ImmediateCallRequestDto
import com.konkuk.medicarecall.data.dto.response.HomeResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface HomeService {
    @GET("elders/{elderId}/home")
    suspend fun getHomeSummary(
        @Path("elderId") elderId: Int,
    ): HomeResponseDto

    @POST("care-call/immediate")
    suspend fun requestImmediateCareCall(
        @Body request: ImmediateCallRequestDto,
    ): Response<Unit>
}
