package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.elders.GlucoseService
import com.konkuk.medicarecall.data.mapper.toDomain
import com.konkuk.medicarecall.data.repository.GlucoseRepository
import com.konkuk.medicarecall.data.util.handleResponse
import com.konkuk.medicarecall.domain.model.Glucose
import org.koin.core.annotation.Single

@Single
class GlucoseRepositoryImpl(
    private val glucoseService: GlucoseService,
) : GlucoseRepository {
    override suspend fun getGlucoseGraph(
        elderId: Long,
        counter: Int,
        type: String,
    ): Result<Glucose> = runCatching {
        glucoseService
            .getGlucoseGraph(elderId, counter, type)
            .handleResponse()
            .toDomain()
    }
}
