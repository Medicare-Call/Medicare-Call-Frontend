package com.konkuk.medicarecall.data.api.elders

import com.konkuk.medicarecall.data.dto.response.StatisticsResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StatisticsService {

    @GET("elders/{elderId}/weekly-stats")
    suspend fun getStatistics(
        @Path("elderId") elderId: Int,
        @Query("startDate") startDate: String,
    ): StatisticsResponseDto
}
