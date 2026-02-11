package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.domain.model.StatisticsData

interface StatisticsRepository {
    suspend fun getStatistics(elderId: Int, startDate: String): Result<StatisticsData>
}
