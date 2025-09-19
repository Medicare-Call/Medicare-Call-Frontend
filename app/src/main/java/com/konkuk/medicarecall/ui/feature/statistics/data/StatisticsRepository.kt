package com.konkuk.medicarecall.ui.feature.statistics.data

import com.konkuk.medicarecall.ui.statistics.model.StatisticsResponseDto

interface StatisticsRepository {
    suspend fun getStatistics(elderId: Int, startDate: String): StatisticsResponseDto
}
