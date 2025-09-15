package com.konkuk.medicarecall.ui.homedetail.sleep.data

import com.konkuk.medicarecall.ui.homedetail.sleep.model.SleepResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SleepService {
    @GET("elders/{elderId}/sleep")
    suspend fun getDailySleep(
        @Path("elderId") elderId: Int,
        @Query("date") date: String
    ): SleepResponseDto
}