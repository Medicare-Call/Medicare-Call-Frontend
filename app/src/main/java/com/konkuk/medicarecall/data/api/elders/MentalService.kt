package com.konkuk.medicarecall.data.api.elders

import com.konkuk.medicarecall.data.dto.response.MentalResponseDto
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query


interface MentalService {
    @GET("elders/{elderId}/mental-analysis")
    suspend fun getDailyMental(
        @Path("elderId") elderId: Int,
        @Query("date") date: String,
    ): MentalResponseDto
}
