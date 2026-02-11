package com.konkuk.medicarecall.domain.model.type

// 아침, 점심, 저녁 enum
enum class MedicationTime(val displayName: String) {
    BREAKFAST("아침"),
    LUNCH("점심"),
    DINNER("저녁"),
}
