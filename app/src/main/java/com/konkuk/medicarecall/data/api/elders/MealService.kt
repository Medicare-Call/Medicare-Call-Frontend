package com.konkuk.medicarecall.data.api.elders

import com.konkuk.medicarecall.data.dto.response.MealResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MealService {
    @GET("elders/{elderId}/meals")
    suspend fun getDailyMeal(
        @Path("elderId") elderId: Int,
        @Query("date") date: String,
    ): MealResponseDto
}
