package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.data.dto.response.StatisticsResponseDto

interface StatisticsRepository {
    suspend fun getStatistics(elderId: Int, startDate: String): StatisticsResponseDto
}
