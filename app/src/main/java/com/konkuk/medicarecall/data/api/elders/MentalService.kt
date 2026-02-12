package com.konkuk.medicarecall.data.api.elders

import com.konkuk.medicarecall.data.dto.response.MentalResponseDto
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

interface MentalService {
    @GET("elders/{elderId}/mental-analysis")
    suspend fun getDailyMental(
        @Path("elderId") elderId: Long,
        @Query("date") date: String,
    ): Response<MentalResponseDto>
}
