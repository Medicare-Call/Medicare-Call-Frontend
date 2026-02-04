package com.konkuk.medicarecall.ui.feature.homedetail.medicine.viewmodel

data class MedicineUiState(
    val medicineName: String = "", // 약 이름
    val todayRequiredCount: Int, // 목표 복약 횟수
    val doseStatusList: List<DoseStatusItem> = emptyList(), // 복약 시간대 + 상태
)

data class DoseStatusItem(
    val time: String, // "MORNING", "LUNCH", "DINNER"
    val doseStatus: DoseStatus, // TAKEN, SKIPPED, NOT_RECORDED
)

enum class DoseStatus {
    TAKEN,
    SKIPPED,
    NOT_RECORDED,
}
