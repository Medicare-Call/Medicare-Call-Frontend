package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.elders.HealthService
import com.konkuk.medicarecall.data.mapper.toHealth
import com.konkuk.medicarecall.data.repository.HealthRepository
import com.konkuk.medicarecall.data.util.handleResponse
import com.konkuk.medicarecall.domain.model.Health
import kotlinx.datetime.LocalDate
import org.koin.core.annotation.Single

@Single
class HealthRepositoryImpl(
    private val healthService: HealthService,
) : HealthRepository {

    override suspend fun getHealth(
        elderId: Long,
        date: LocalDate,
    ): Result<Health> = runCatching {
        healthService.getDailyHealth(
            elderId = elderId,
            date = date.toString(),
        )
            .handleResponse()
            .toHealth()
    }
}
