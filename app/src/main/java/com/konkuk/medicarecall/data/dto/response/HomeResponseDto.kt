package com.konkuk.medicarecall.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class HomeResponseDto(

    val elderName: String = "",
    val aiSummary: String?,

    val mealStatus: MealStatusDto,
    val medicationStatus: MedicationStatusDto,

    val sleep: SleepDto?,
    val healthStatus: String?,
    val mentalStatus: String?,
    val bloodSugar: BloodSugarDto?,
    val unreadNotification: Int?,
) {
    @Serializable
    data class MealStatusDto(
        val breakfast: Boolean?,
        val lunch: Boolean?,
        val dinner: Boolean?,
    )

    @Serializable
    data class MedicationStatusDto(
        val totalTaken: Int?,
        val totalGoal: Int?,
        val medicationList: List<MedicationDto>?,
    )

    @Serializable
    data class MedicationDto(
        val type: String?,
        val taken: Int?,
        val goal: Int?,
        val nextTime: String?,
        val doseStatusList: List<DoseStatusDto>?,
    )

    @Serializable
    data class DoseStatusDto(
        val time: String?,
        val taken: Boolean?,
    )

    @Serializable
    data class SleepDto(
        val meanHours: Int?,
        val meanMinutes: Int?,
    )

    @Serializable
    data class BloodSugarDto(
        val meanValue: Int?,
    )
}
