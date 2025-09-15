package com.konkuk.medicarecall.ui.homedetail.meal.data


import com.konkuk.medicarecall.ui.homedetail.meal.model.MealResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MealService {
    @GET("elders/{elderId}/meals")
    suspend fun getDailyMeal(
        @Path("elderId") elderId: Int,
        @Query("date") date: String
    ): MealResponseDto
}