package com.konkuk.medicarecall.data.api.elders

import com.konkuk.medicarecall.data.dto.response.HealthResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HealthService {
    @GET("elders/{elderId}/health-analysis")
    suspend fun getDailyHealth(
        @Path("elderId") elderId: Int,
        @Query("date") date: String,
    ): HealthResponseDto
}
