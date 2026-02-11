package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.elders.GlucoseService
import com.konkuk.medicarecall.data.dto.response.GlucoseResponseDto
import com.konkuk.medicarecall.data.repository.GlucoseRepository
import com.konkuk.medicarecall.data.util.handleResponse
import org.koin.core.annotation.Single

@Single
class GlucoseRepositoryImpl(
    private val glucoseService: GlucoseService,
) : GlucoseRepository {
    override suspend fun getGlucoseGraph(
        elderId: Long,
        counter: Int,
        type: String,
    ): Result<GlucoseResponseDto> = runCatching {
        glucoseService.getGlucoseGraph(elderId, counter, type).handleResponse()
    }
}
