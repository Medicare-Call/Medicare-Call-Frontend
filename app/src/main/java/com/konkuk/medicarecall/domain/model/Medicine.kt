package com.konkuk.medicarecall.domain.model

data class Medicine(
    val medicineName: String = "", // 약 이름
    val todayTakenCount: Int? = null, // 오늘 복약한 횟수
    val todayRequiredCount: Int? = null, // 목표 복약 횟수
    val nextDoseTime: String? = null, // 다음 복약 시간
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
