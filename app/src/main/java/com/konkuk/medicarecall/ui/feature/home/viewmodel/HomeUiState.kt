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
    val totalTaken: Int? = null,
    val totalGoal: Int? = null,
    val medicines: List<MedicineUiState> = emptyList(),
    val sleep: HomeResponseDto.SleepDto? = null,
    val healthStatus: String? = null,
    val mentalStatus: String? = null,
    val glucoseLevelAverageToday: Int? = null,
    val unreadNotification: Int? = null,
) {
    companion object {
        val EMPTY = HomeUiState()

        fun from(dto: HomeResponseDto): HomeUiState = HomeUiState(
            elderName = dto.elderName,
            balloonMessage = dto.aiSummary.orEmpty(),

            // 기록 존재 여부(세 끼 중 하나라도 null 아니면 true)
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

            totalTaken = dto.medicationStatus.totalTaken,
            totalGoal = dto.medicationStatus.totalGoal,

            medicines = dto.medicationStatus.medicationList
                .orEmpty()
                .map {
                    MedicineUiState(
                        medicineName = it.type.orEmpty(),
                        todayTakenCount = it.taken,
                        todayRequiredCount = it.goal,
                        nextDoseTime = when (it.nextTime) {
                            "MORNING" -> "아침"
                            "LUNCH" -> "점심"
                            "DINNER" -> "저녁"
                            else -> it.nextTime
                        },
                        doseStatusList = it.doseStatusList?.map { dtoDose ->
                            DoseStatusUiState(
                                time = dtoDose.time.orEmpty(),
                                taken = dtoDose.taken,
                            )
                        },
                    )
                },
            sleep = dto.sleep,
            healthStatus = dto.healthStatus,
            mentalStatus = dto.mentalStatus,
            glucoseLevelAverageToday = dto.bloodSugar?.meanValue,
            unreadNotification = dto.unreadNotification,
        )
    }
}

data class MedicineUiState(
    val medicineName: String,
    val todayTakenCount: Int?,
    val todayRequiredCount: Int?,
    val nextDoseTime: String?,
    val doseStatusList: List<DoseStatusUiState>? = null,
)

// 아이콘 상태를 위한 UI State
data class DoseStatusUiState(
    val time: String, // "아침", "점심", "저녁"
    val taken: Boolean?, // true: 먹음, false: 안 먹음, null: 미기록
)

/**
 * HomeScreen 전체의 통합 UiState
 */
data class HomeScreenUiState(
    val homeData: HomeUiState, // 홈 화면 메인 데이터
    val elderInfoList: List<ElderInfo>, // 어르신 전체 목록
    val selectedElderId: Int?, // 현재 선택된 어르신 ID
) {
    companion object {
        val EMPTY = HomeScreenUiState(
            homeData = HomeUiState.EMPTY,
            elderInfoList = emptyList(),
            selectedElderId = null,
        )
    }
}

data class ElderInfo(val id: Int, val name: String, val phone: String?)
