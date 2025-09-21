package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.ui.feature.statistics.model.StatisticsResponseDto

interface StatisticsRepository {
    suspend fun getStatistics(elderId: Int, startDate: String): StatisticsResponseDto
}
