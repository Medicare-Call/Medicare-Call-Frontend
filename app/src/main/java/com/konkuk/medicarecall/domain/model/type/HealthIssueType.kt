package com.konkuk.medicarecall.domain.model.type

enum class HealthIssueType(val displayName: String) {
    INSOMNIA("불면증 / 수면장애"),
    FORGETS_MEDICATION("약 자주 잊음"),
    MOBILITY_ISSUE("보행 불편"),
    HEARING_LOSS("청력 저하"),
    COGNITIVE_DECLINE("인지저하 의심"),
    MOOD_SWINGS("감정기복"),
    RECENT_SPOUSE_DEATH("최근 배우자 사망"),
    SMOKING("흡연"),
    DRINKING("음주"),
    ALCOHOLISM("알콜중독"),
    VISUAL_IMPAIRMENT("시각장애"),
}
