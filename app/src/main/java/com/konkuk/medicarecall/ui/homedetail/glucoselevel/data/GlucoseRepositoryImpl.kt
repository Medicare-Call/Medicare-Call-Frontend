package com.konkuk.medicarecall.ui.homedetail.glucoselevel.data

import com.konkuk.medicarecall.ui.homedetail.glucoselevel.model.GlucoseResponseDto
import retrofit2.HttpException
import javax.inject.Inject

class GlucoseRepositoryImpl @Inject constructor(
    private val glucoseApi: GlucoseApi
) : GlucoseRepository {
    override suspend fun getGlucoseGraph(
        elderId: Int,
        counter: Int,
        type: String
    ): Result<GlucoseResponseDto> =
        runCatching {
            val response = glucoseApi.getGlucoseGraph(elderId, counter, type)
            if (response.isSuccessful) {
                response.body() ?: throw IllegalStateException("Response body is null")
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                throw HttpException(response)
            }
        }


}