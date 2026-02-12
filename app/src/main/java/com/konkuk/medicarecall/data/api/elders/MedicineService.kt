package com.konkuk.medicarecall.data.api.elders

import com.konkuk.medicarecall.data.dto.response.MedicineResponseDto
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

interface MedicineService {
    @GET("elders/{elderId}/medication")
    suspend fun getDailyMedication(
        @Path("elderId") elderId: Long,
        @Query("date") date: String,
    ): Response<MedicineResponseDto>
}
