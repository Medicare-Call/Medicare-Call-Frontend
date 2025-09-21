package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.GlucoseService
import com.konkuk.medicarecall.data.repository.GlucoseRepository
import com.konkuk.medicarecall.data.dto.response.GlucoseResponseDto
import retrofit2.HttpException
import javax.inject.Inject

class GlucoseRepositoryImpl @Inject constructor(
    private val glucoseService: GlucoseService
) : GlucoseRepository {
    override suspend fun getGlucoseGraph(
        elderId: Int,
        counter: Int,
        type: String
    ): Result<GlucoseResponseDto> =
        runCatching {
            val response = glucoseService.getGlucoseGraph(elderId, counter, type)
            if (response.isSuccessful) {
                response.body() ?: throw IllegalStateException("Response body is null")
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                throw HttpException(response)
            }
        }


}
