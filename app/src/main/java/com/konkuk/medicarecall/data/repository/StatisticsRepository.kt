package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.domain.model.StatisticsData

interface StatisticsRepository {
    suspend fun getStatistics(elderId: Long, startDate: String): Result<StatisticsData>
}
