package com.konkuk.medicarecall.ui.home.data

import com.konkuk.medicarecall.ui.home.model.HomeResponseDto
import com.konkuk.medicarecall.ui.home.model.ImmediateCallRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface HomeService {
    @GET("elders/{elderId}/home")
    suspend fun getHomeSummary(
        @Path("elderId") elderId: Int
    ): HomeResponseDto


    @POST("care-call/immediate")
    suspend fun requestImmediateCareCall(
        @Body request: ImmediateCallRequestDto
    ): Response<Unit>

}
