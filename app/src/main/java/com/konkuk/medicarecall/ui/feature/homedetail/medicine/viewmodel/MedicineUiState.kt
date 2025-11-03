package com.konkuk.medicarecall.ui.feature.homedetail.medicine.viewmodel

data class MedicineUiState(
    val medicineName: String, // 약 이름
    val todayRequiredCount: Int, // 목표 복약 횟수
    // val nextDoseTime: String?, // 다음 복약 예정 시간대 (MORNING 등)
    val doseStatusList: List<DoseStatusItem>, // 복약 시간대 + 상태
) {
    companion object {
        val EMPTY = MedicineUiState(
            medicineName = "",
            todayRequiredCount = 0,
            // nextDoseTime = null,
            doseStatusList = emptyList(),
        )
    }
}

data class DoseStatusItem(
    val time: String, // "MORNING", "LUNCH", "DINNER"
    val doseStatus: DoseStatus, // TAKEN, SKIPPED, NOT_RECORDED
)

enum class DoseStatus {
    TAKEN,
    SKIPPED,
    NOT_RECORDED,
}
