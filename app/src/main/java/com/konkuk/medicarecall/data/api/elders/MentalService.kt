package com.konkuk.medicarecall.data.api.elders

import com.konkuk.medicarecall.data.dto.response.MentalResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MentalService {
    @GET("elders/{elderId}/mental-analysis")
    suspend fun getDailyMental(
        @Path("elderId") elderId: Int,
        @Query("date") date: String,
    ): MentalResponseDto
}
