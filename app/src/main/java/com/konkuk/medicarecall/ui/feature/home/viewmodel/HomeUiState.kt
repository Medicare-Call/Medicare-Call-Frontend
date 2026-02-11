package com.konkuk.medicarecall.ui.feature.home.viewmodel

import com.konkuk.medicarecall.domain.model.HomeSleep

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
    val sleep: HomeSleep? = null,
    val healthStatus: String? = null,
    val mentalStatus: String? = null,
    val glucoseLevelAverageToday: Int? = null,
    val unreadNotification: Int? = null,
) {
    companion object {
        val EMPTY = HomeUiState()
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
