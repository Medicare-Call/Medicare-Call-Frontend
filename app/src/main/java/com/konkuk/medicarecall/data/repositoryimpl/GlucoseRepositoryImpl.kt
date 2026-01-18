package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.elders.GlucoseService
import com.konkuk.medicarecall.data.dto.response.GlucoseResponseDto
import com.konkuk.medicarecall.data.repository.GlucoseRepository
import org.koin.core.annotation.Single

@Single
class GlucoseRepositoryImpl(
    private val glucoseService: GlucoseService,
) : GlucoseRepository {
    override suspend fun getGlucoseGraph(
        elderId: Int,
        counter: Int,
        type: String,
    ): Result<GlucoseResponseDto> =
        runCatching {
            val response = glucoseService.getGlucoseGraph(elderId, counter, type)
            if (response.isSuccessful) {
                response.body() ?: error("Response body is null")
            } else {
//                val errorBody = response.errorBody()?.toString() ?: "Unknown error"
                error("Failed to fetch glucose graph: ${response.code}")
            }
        }
}
