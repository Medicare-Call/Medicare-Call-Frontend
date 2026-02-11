package com.konkuk.medicarecall.data.api.elders

import com.konkuk.medicarecall.data.dto.response.StatisticsResponseDto
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

interface StatisticsService {

    @GET("elders/{elderId}/weekly-stats")
    suspend fun getStatistics(
        @Path("elderId") elderId: Long,
        @Query("startDate") startDate: String,
    ): Response<StatisticsResponseDto>
}
