package com.konkuk.medicarecall.data.api.elders

import com.konkuk.medicarecall.data.dto.response.GlucoseResponseDto
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

interface GlucoseService {

    @GET("elders/{elderId}/blood-sugar/weekly")
    suspend fun getGlucoseGraph(
        @Path("elderId") elderId: Long,
        @Query("counter") counter: Int,
        @Query("type") type: String, // BEFORE_MEAL or AFTER_MEAL
    ): Response<GlucoseResponseDto>
}
