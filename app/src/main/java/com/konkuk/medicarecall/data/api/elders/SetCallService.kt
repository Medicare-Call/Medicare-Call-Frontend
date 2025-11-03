package com.konkuk.medicarecall.data.api.elders

import com.konkuk.medicarecall.data.dto.request.SetCallTimeRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface SetCallService {
    @POST("elders/{elderId}/care-call-setting")
    suspend fun saveCareCallTimes(
        @Path("elderId") elderId: Int,
        @Body body: SetCallTimeRequestDto,
    ): Response<Unit>
}
