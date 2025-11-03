package com.konkuk.medicarecall.data.api.elders

import com.konkuk.medicarecall.data.dto.response.MedicineResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MedicineService {
    @GET("elders/{elderId}/medication")
    suspend fun getDailyMedication(
        @Path("elderId") elderId: Int,
        @Query("date") date: String,
    ): Response<MedicineResponseDto>
}
