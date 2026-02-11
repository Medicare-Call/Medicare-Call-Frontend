package com.konkuk.medicarecall.data.mapper

import com.konkuk.medicarecall.data.dto.response.AverageSleepDto
import com.konkuk.medicarecall.data.dto.response.BloodSugarDetailDto
import com.konkuk.medicarecall.data.dto.response.BloodSugarDto
import com.konkuk.medicarecall.data.dto.response.MealStatsDto
import com.konkuk.medicarecall.data.dto.response.MedicationStatDto
import com.konkuk.medicarecall.data.dto.response.PsychSummaryDto
import com.konkuk.medicarecall.data.dto.response.StatisticsResponseDto
import com.konkuk.medicarecall.data.dto.response.SummaryStatsDto
import com.konkuk.medicarecall.domain.model.AverageSleep
import com.konkuk.medicarecall.domain.model.BloodSugar
import com.konkuk.medicarecall.domain.model.BloodSugarDetail
import com.konkuk.medicarecall.domain.model.MealStats
import com.konkuk.medicarecall.domain.model.MedicationStat
import com.konkuk.medicarecall.domain.model.PsychSummary
import com.konkuk.medicarecall.domain.model.StatisticsData
import com.konkuk.medicarecall.domain.model.SummaryStats

fun StatisticsResponseDto.toModel() = StatisticsData(
    elderName = this.elderName,
    summaryStats = this.summaryStats?.toModel(),
    mealStats = this.mealStats?.toModel(),
    medicationStats = this.medicationStats?.mapValues { it.value.toModel() },
    healthSummary = this.healthSummary,
    averageSleep = this.averageSleep?.toModel(),
    psychSummary = this.psychSummary?.toModel(),
    bloodSugar = this.bloodSugar?.toModel(),
    subscriptionStartDate = this.subscriptionStartDate,
    unreadNotification = this.unreadNotification,
)

fun SummaryStatsDto.toModel() = SummaryStats(
    mealRate = this.mealRate,
    medicationRate = this.medicationRate,
    healthSignals = this.healthSignals,
    missedCalls = this.missedCalls,
)

fun MealStatsDto.toModel() = MealStats(
    breakfast = this.breakfast,
    lunch = this.lunch,
    dinner = this.dinner,
)

fun MedicationStatDto.toModel() = MedicationStat(
    totalCount = this.totalCount,
    takenCount = this.takenCount,
)

fun AverageSleepDto.toModel() = AverageSleep(
    hours = this.hours,
    minutes = this.minutes,
)

fun PsychSummaryDto.toModel() = PsychSummary(
    good = this.good,
    normal = this.normal,
    bad = this.bad,
)

fun BloodSugarDto.toModel() = BloodSugar(
    beforeMeal = this.beforeMeal?.toModel(),
    afterMeal = this.afterMeal?.toModel(),
)

fun BloodSugarDetailDto.toModel() = BloodSugarDetail(
    normal = this.normal,
    high = this.high,
    low = this.low,
)
