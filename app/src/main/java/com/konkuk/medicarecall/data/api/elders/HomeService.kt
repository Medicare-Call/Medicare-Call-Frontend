package com.konkuk.medicarecall.data.api.elders

import com.konkuk.medicarecall.data.dto.request.ImmediateCallRequestDto
import com.konkuk.medicarecall.data.dto.response.HomeResponseDto
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path

interface HomeService {
    @GET("elders/{elderId}/home")
    suspend fun getHomeSummary(
        @Path("elderId") elderId: Long,
    ): Response<HomeResponseDto>

    @POST("care-call/immediate")
    suspend fun requestImmediateCareCall(
        @Body request: ImmediateCallRequestDto,
    ): Response<Unit>
}
