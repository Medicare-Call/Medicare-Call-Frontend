package com.konkuk.medicarecall.data.api.elders

import com.konkuk.medicarecall.data.dto.response.HealthResponseDto
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

interface HealthService {
    @GET("elders/{elderId}/health-analysis")
    suspend fun getDailyHealth(
        @Path("elderId") elderId: Int,
        @Query("date") date: String,
    ): HealthResponseDto
}
