package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.elders.StatisticsService
import com.konkuk.medicarecall.data.dto.response.AverageSleepDto
import com.konkuk.medicarecall.data.dto.response.BloodSugarDetailDto
import com.konkuk.medicarecall.data.dto.response.BloodSugarDto
import com.konkuk.medicarecall.data.dto.response.MealStatsDto
import com.konkuk.medicarecall.data.dto.response.MedicationStatDto
import com.konkuk.medicarecall.data.dto.response.PsychSummaryDto
import com.konkuk.medicarecall.data.dto.response.StatisticsResponseDto
import com.konkuk.medicarecall.data.dto.response.SummaryStatsDto
import com.konkuk.medicarecall.data.repository.EldersHealthInfoRepository
import com.konkuk.medicarecall.data.repository.StatisticsRepository
import retrofit2.HttpException
import java.time.LocalDate
import javax.inject.Inject

class StatisticsRepositoryImpl @Inject constructor(
    private val statisticsService: StatisticsService,
    private val eldersHealthInfoRepository: EldersHealthInfoRepository,
) : StatisticsRepository {

    override suspend fun getStatistics(elderId: Int, startDate: String): StatisticsResponseDto {
        return try {
            val response = statisticsService.getStatistics(elderId = elderId, startDate = startDate)
            response
        } catch (e: HttpException) {
            if (e.code() == 404) {
                createUnrecordedStatisticsDto(elderId)
            } else {
                throw e
            }
        }
    }

    private suspend fun createUnrecordedStatisticsDto(elderId: Int): StatisticsResponseDto {
        val healthInfo = eldersHealthInfoRepository.getEldersHealthInfo()
            .getOrNull()
            ?.firstOrNull { it.elderId == elderId }

        val elderName = healthInfo?.name ?: ""

        val medicationStats = healthInfo?.medications?.values
            ?.flatten()
            ?.distinct()
            ?.associateWith {
                MedicationStatDto(
                    takenCount = -1,
                    totalCount = 0,
                )
            }
            ?: emptyMap()

        return StatisticsResponseDto(
            elderName = elderName,
            summaryStats = SummaryStatsDto(
                mealRate = -1,
                medicationRate = -1,
                healthSignals = -1,
                missedCalls = -1,
            ),
            mealStats = MealStatsDto(
                breakfast = -1,
                lunch = -1,
                dinner = -1,
            ),
            medicationStats = medicationStats,
            healthSummary = "아직 충분한 기록이 쌓이지 않았어요.",
            averageSleep = AverageSleepDto(
                hours = null,
                minutes = null,
            ),
            psychSummary = PsychSummaryDto(
                good = -1,
                normal = -1,
                bad = -1,
            ),
            bloodSugar = BloodSugarDto(
                beforeMeal = BloodSugarDetailDto(
                    normal = 0,
                    high = 0,
                    low = 0,
                ),
                afterMeal = BloodSugarDetailDto(
                    normal = 0,
                    high = 0,
                    low = 0,
                ),
            ),
            subscriptionStartDate = LocalDate.now().toString(),
        )
    }
}
