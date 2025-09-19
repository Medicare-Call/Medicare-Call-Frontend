package com.konkuk.medicarecall.ui.feature.homedetail.glucoselevel.data


import com.konkuk.medicarecall.ui.feature.homedetail.glucoselevel.model.GlucoseResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GlucoseService {


    @GET("elders/{elderId}/blood-sugar/weekly")
    suspend fun getGlucoseGraph(
        @Path("elderId") elderId: Int,
        @Query("counter") counter: Int,
        @Query("type") type: String // BEFORE_MEAL or AFTER_MEAL
    ): Response<GlucoseResponseDto>
}
