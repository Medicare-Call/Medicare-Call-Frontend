package com.konkuk.medicarecall.ui.statistics.data

import com.konkuk.medicarecall.ui.statistics.model.StatisticsResponseDto

interface StatisticsRepository {
    suspend fun getStatistics(elderId: Int, startDate: String): StatisticsResponseDto
}