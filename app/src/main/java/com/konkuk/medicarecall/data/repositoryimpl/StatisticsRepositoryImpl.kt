package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.StatisticsService
import com.konkuk.medicarecall.data.repository.EldersHealthInfoRepository
import com.konkuk.medicarecall.data.repository.StatisticsRepository
import com.konkuk.medicarecall.ui.feature.statistics.model.MedicationStatDto
import com.konkuk.medicarecall.ui.feature.statistics.model.StatisticsResponseDto
import retrofit2.HttpException
import java.time.LocalDate
import javax.inject.Inject

class StatisticsRepositoryImpl @Inject constructor(
    private val statisticsService: StatisticsService,
    private val eldersHealthInfoRepository: EldersHealthInfoRepository
) : StatisticsRepository {

    override suspend fun getStatistics(elderId: Int, startDate: String): StatisticsResponseDto {
        return try {
            val response = statisticsService.getStatistics(elderId = elderId, startDate = startDate)
            response

        } catch (e: Exception) {

            if (e is HttpException && e.code() == 404) {
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
                    totalCount = 0
                )
            }
            ?: emptyMap()

        return _root_ide_package_.com.konkuk.medicarecall.ui.feature.statistics.model.StatisticsResponseDto(
            elderName = elderName,
            summaryStats = _root_ide_package_.com.konkuk.medicarecall.ui.feature.statistics.model.SummaryStatsDto(
                mealRate = -1,
                medicationRate = -1,
                healthSignals = -1,
                missedCalls = -1
            ),
            mealStats = _root_ide_package_.com.konkuk.medicarecall.ui.feature.statistics.model.MealStatsDto(
                breakfast = -1,
                lunch = -1,
                dinner = -1
            ),
            medicationStats = medicationStats,
            healthSummary = "아직 충분한 기록이 쌓이지 않았어요.",
            averageSleep = _root_ide_package_.com.konkuk.medicarecall.ui.feature.statistics.model.AverageSleepDto(
                hours = null,
                minutes = null
            ),
            psychSummary = _root_ide_package_.com.konkuk.medicarecall.ui.feature.statistics.model.PsychSummaryDto(
                good = -1,
                normal = -1,
                bad = -1
            ),
            bloodSugar = _root_ide_package_.com.konkuk.medicarecall.ui.feature.statistics.model.BloodSugarDto(
                beforeMeal = _root_ide_package_.com.konkuk.medicarecall.ui.feature.statistics.model.BloodSugarDetailDto(
                    normal = 0,
                    high = 0,
                    low = 0
                ),
                afterMeal = _root_ide_package_.com.konkuk.medicarecall.ui.feature.statistics.model.BloodSugarDetailDto(
                    normal = 0,
                    high = 0,
                    low = 0
                )
            ),
            subscriptionStartDate = LocalDate.now().toString()
        )
    }
}
