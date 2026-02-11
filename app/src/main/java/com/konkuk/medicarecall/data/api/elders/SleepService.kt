package com.konkuk.medicarecall.data.api.elders

import com.konkuk.medicarecall.data.dto.response.SleepResponseDto
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

interface SleepService {
    @GET("elders/{elderId}/sleep")
    suspend fun getDailySleep(
        @Path("elderId") elderId: Long,
        @Query("date") date: String,
    ): Response<SleepResponseDto>
}
