package com.konkuk.medicarecall.ui.homedetail.medicine.data

import com.konkuk.medicarecall.ui.homedetail.medicine.model.MedicineResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MedicineService {
    @GET("elders/{elderId}/medication")
    suspend fun getDailyMedication(
        @Path("elderId") elderId: Int,
        @Query("date") date: String
    ): Response<MedicineResponseDto>
}