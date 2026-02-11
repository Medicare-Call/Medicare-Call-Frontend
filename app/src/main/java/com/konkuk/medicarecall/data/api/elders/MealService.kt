package com.konkuk.medicarecall.data.api.elders

import com.konkuk.medicarecall.data.dto.response.MealResponseDto
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

interface MealService {
    @GET("elders/{elderId}/meals")
    suspend fun getDailyMeal(
        @Path("elderId") elderId: Long,
        @Query("date") date: String,
    ): Response<MealResponseDto>
}
