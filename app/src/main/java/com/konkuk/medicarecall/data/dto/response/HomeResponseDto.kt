package com.konkuk.medicarecall.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeResponseDto(

    @SerialName("elderName")
    val elderName: String = "",

    @SerialName("aiSummary")
    val aiSummary: String = "",

    val mealStatus: MealStatusDto = MealStatusDto(),
    val medicationStatus: MedicationStatusDto = MedicationStatusDto(),

    val sleep: SleepDto? = null,
    val healthStatus: String? = null,
    val mentalStatus: String? = null,
    val bloodSugar: BloodSugarDto? = null,
) {
    @Serializable
    data class MealStatusDto(
        val breakfast: Boolean? = null,
        val lunch: Boolean? = null,
        val dinner: Boolean? = null,
    )

    @Serializable
    data class MedicationStatusDto(
        val totalTaken: Int = 0,
        val totalGoal: Int = 0,
        val nextMedicationTime: String? = null,
        val medicationList: List<MedicationDto> = emptyList(),
    )

    @Serializable
    data class MedicationDto(
        val type: String = "",
        val taken: Int = 0,
        val goal: Int = 0,
        val nextTime: String? = null,
    )

    @Serializable
    data class SleepDto(
        val meanHours: Int = 0,
        val meanMinutes: Int = 0,
    )

    @Serializable
    data class BloodSugarDto(
        @SerialName("meanValue")
        val meanValue: Int = 0,
    )
}
