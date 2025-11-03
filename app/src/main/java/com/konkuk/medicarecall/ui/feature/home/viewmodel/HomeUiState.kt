package com.konkuk.medicarecall.ui.feature.home.viewmodel

import com.konkuk.medicarecall.data.dto.response.HomeResponseDto

data class HomeUiState(
    val isLoading: Boolean = true,
    val elderName: String = "",
    val balloonMessage: String = "",
    val isRecorded: Boolean = false,
    val isEaten: Boolean = false,
    val breakfastEaten: Boolean? = null,
    val lunchEaten: Boolean? = null,
    val dinnerEaten: Boolean? = null,
    val medicines: List<MedicineUiState> = emptyList(),
    val sleep: HomeResponseDto.SleepDto = HomeResponseDto.SleepDto(0, 0),
    val healthStatus: String = "",
    val mentalStatus: String = "",
    val glucoseLevelAverageToday: Int = 0,
) {
    companion object {
        val EMPTY = HomeUiState()

        fun from(dto: HomeResponseDto): HomeUiState = HomeUiState(
            elderName = dto.elderName,
            balloonMessage = dto.aiSummary.orEmpty(),

            // 기록 존재 여부(세 끼 중 하나라도 null 아님)
            isRecorded = listOf(
                dto.mealStatus.breakfast,
                dto.mealStatus.lunch,
                dto.mealStatus.dinner,
            ).any { it != null },

            // 오늘 한 끼라도 먹었는지(선택적)
            isEaten = listOf(
                dto.mealStatus.breakfast,
                dto.mealStatus.lunch,
                dto.mealStatus.dinner,
            ).any { it == true },

            breakfastEaten = dto.mealStatus.breakfast,
            lunchEaten = dto.mealStatus.lunch,
            dinnerEaten = dto.mealStatus.dinner,

            medicines = dto.medicationStatus.medicationList
                .orEmpty()
                .map {
                    MedicineUiState(
                        medicineName = it.type,
                        todayTakenCount = it.taken,
                        todayRequiredCount = it.goal,
                        nextDoseTime = when (it.nextTime) {
                            "MORNING" -> "아침"
                            "LUNCH" -> "점심"
                            "DINNER" -> "저녁"
                            null, "" -> "-"
                            else -> it.nextTime
                        },
                    )
                },
            sleep = dto.sleep ?: HomeResponseDto.SleepDto(0, 0),
            healthStatus = dto.healthStatus ?: "",
            mentalStatus = dto.mentalStatus ?: "",
            glucoseLevelAverageToday = dto.bloodSugar?.meanValue ?: 0,
        )
    }
}

data class MedicineUiState(
    val medicineName: String,
    val todayTakenCount: Int,
    val todayRequiredCount: Int,
    val nextDoseTime: String?,
)
