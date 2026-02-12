package com.konkuk.medicarecall.data.api.elders

import com.konkuk.medicarecall.data.dto.request.SetCallTimeRequestDto
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path

interface SetCallService {
    @POST("elders/{elderId}/care-call-setting")
    suspend fun saveCareCallTimes(
        @Path("elderId") elderId: Long,
        @Body body: SetCallTimeRequestDto,
    ): Response<Unit>
}
