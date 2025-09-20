package com.konkuk.medicarecall.ui.feature.statistics.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StatisticsService {


    @GET("elders/{elderId}/weekly-stats")
    suspend fun getStatistics(
        @Path("elderId") elderId: Int,
        @Query("startDate") startDate: String,
    ): com.konkuk.medicarecall.ui.feature.statistics.model.StatisticsResponseDto
}
