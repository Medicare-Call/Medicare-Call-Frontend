package com.konkuk.medicarecall.domain.model

data class StatisticsData(
    val elderName: String = "",
    val summaryStats: SummaryStats? = null,
    val mealStats: MealStats? = null,
    val medicationStats: Map<String, MedicationStat>? = null,
    val healthSummary: String? = null,
    val averageSleep: AverageSleep? = null,
    val psychSummary: PsychSummary? = null,
    val bloodSugar: BloodSugar? = null,
    val subscriptionStartDate: String? = null,
    val unreadNotification: Int? = null,
)

data class SummaryStats(
    val mealRate: Int? = null,
    val medicationRate: Int? = null,
    val healthSignals: Int? = null,
    val missedCalls: Int? = null,
)

data class MealStats(
    val breakfast: Int? = null,
    val lunch: Int? = null,
    val dinner: Int? = null,
)

data class MedicationStat(
    val totalCount: Int? = null,
    val takenCount: Int? = null,
)

data class AverageSleep(
    val hours: Int? = null,
    val minutes: Int? = null,
)

data class PsychSummary(
    val good: Int? = null,
    val normal: Int? = null,
    val bad: Int? = null,
)

data class BloodSugar(
    val beforeMeal: BloodSugarDetail? = null,
    val afterMeal: BloodSugarDetail? = null,
)

data class BloodSugarDetail(
    val normal: Int? = null,
    val high: Int? = null,
    val low: Int? = null,
)
