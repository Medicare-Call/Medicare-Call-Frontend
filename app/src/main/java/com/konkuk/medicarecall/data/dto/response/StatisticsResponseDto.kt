package com.konkuk.medicarecall.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StatisticsResponseDto(
    @SerialName("elderName")
    val elderName: String = "",
    @SerialName("summaryStats")
    val summaryStats: SummaryStatsDto? = null,
    @SerialName("mealStats")
    val mealStats: MealStatsDto? = null,
    @SerialName("medicationStats")
    val medicationStats: Map<String, MedicationStatDto>? = null,
    @SerialName("healthSummary")
    val healthSummary: String? = null,
    @SerialName("averageSleep")
    val averageSleep: AverageSleepDto? = null,
    @SerialName("psychSummary")
    val psychSummary: PsychSummaryDto? = null,
    @SerialName("bloodSugar")
    val bloodSugar: BloodSugarDto? = null,
    @SerialName("subscriptionStartDate")
    val subscriptionStartDate: String? = null,
    @SerialName("unreadNotification")
    val unreadNotification: Int? = null,
)

@Serializable
data class SummaryStatsDto(
    @SerialName("mealRate")
    val mealRate: Int? = null,
    @SerialName("medicationRate")
    val medicationRate: Int? = null,
    @SerialName("healthSignals")
    val healthSignals: Int? = null,
    @SerialName("missedCalls")
    val missedCalls: Int? = null,
)

@Serializable
data class MealStatsDto(
    @SerialName("breakfast")
    val breakfast: Int? = null,
    @SerialName("lunch")
    val lunch: Int? = null,
    @SerialName("dinner")
    val dinner: Int? = null,
)

@Serializable
data class MedicationStatDto(
    @SerialName("totalCount")
    val totalCount: Int? = null,
    @SerialName("takenCount")
    val takenCount: Int? = null,
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
    val good: Int? = null,
    @SerialName("normal")
    val normal: Int? = null,
    @SerialName("bad")
    val bad: Int? = null,
)

@Serializable
data class BloodSugarDto(
    @SerialName("beforeMeal")
    val beforeMeal: BloodSugarDetailDto? = null,
    @SerialName("afterMeal")
    val afterMeal: BloodSugarDetailDto? = null,
)

@Serializable
data class BloodSugarDetailDto(
    @SerialName("normal")
    val normal: Int? = null,
    @SerialName("high")
    val high: Int? = null,
    @SerialName("low")
    val low: Int? = null,
)
