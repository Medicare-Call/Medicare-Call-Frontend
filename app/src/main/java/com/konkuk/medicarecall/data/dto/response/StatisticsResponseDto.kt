package com.konkuk.medicarecall.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StatisticsResponseDto(
    @SerialName("elderName")
    val elderName: String,
    @SerialName("summaryStats")
    val summaryStats: SummaryStatsDto,
    @SerialName("mealStats")
    val mealStats: MealStatsDto,
    @SerialName("medicationStats")
    val medicationStats: Map<String, MedicationStatDto>,
    @SerialName("healthSummary")
    val healthSummary: String,
    @SerialName("averageSleep")
    val averageSleep: AverageSleepDto,
    @SerialName("psychSummary")
    val psychSummary: PsychSummaryDto,
    @SerialName("bloodSugar")
    val bloodSugar: BloodSugarDto,
    val subscriptionStartDate: String,
)

@Serializable
data class SummaryStatsDto(
    @SerialName("mealRate")
    val mealRate: Int,
    @SerialName("medicationRate")
    val medicationRate: Int,
    @SerialName("healthSignals")
    val healthSignals: Int,
    @SerialName("missedCalls")
    val missedCalls: Int,
)

@Serializable
data class MealStatsDto(
    @SerialName("breakfast")
    val breakfast: Int?,
    @SerialName("lunch")
    val lunch: Int?,
    @SerialName("dinner")
    val dinner: Int?,
)

@Serializable
data class MedicationStatDto(
    @SerialName("totalCount")
    val totalCount: Int,
    @SerialName("takenCount")
    val takenCount: Int?,
)

@Serializable
data class AverageSleepDto(
    @SerialName("hours")
    val hours: Int? = null,
    @SerialName("minutes")
    val minutes: Int? = null,
)

@Serializable
data class PsychSummaryDto(
    @SerialName("good")
    val good: Int,
    @SerialName("normal")
    val normal: Int,
    @SerialName("bad")
    val bad: Int,
)

@Serializable
data class BloodSugarDto(
    @SerialName("beforeMeal")
    val beforeMeal: BloodSugarDetailDto,
    @SerialName("afterMeal")
    val afterMeal: BloodSugarDetailDto,
)

@Serializable
data class BloodSugarDetailDto(
    @SerialName("normal")
    val normal: Int,
    @SerialName("high")
    val high: Int,
    @SerialName("low")
    val low: Int,
)
