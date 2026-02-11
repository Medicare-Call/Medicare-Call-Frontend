package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.elders.StatisticsService
import com.konkuk.medicarecall.data.mapper.toModel
import com.konkuk.medicarecall.data.repository.StatisticsRepository
import com.konkuk.medicarecall.data.util.handleResponse
import com.konkuk.medicarecall.domain.model.StatisticsData
import org.koin.core.annotation.Single

@Single
class StatisticsRepositoryImpl(
    private val statisticsService: StatisticsService,
) : StatisticsRepository {

    override suspend fun getStatistics(elderId: Long, startDate: String): Result<StatisticsData> = runCatching {
        statisticsService.getStatistics(elderId = elderId, startDate = startDate).handleResponse().toModel()
    }
}
