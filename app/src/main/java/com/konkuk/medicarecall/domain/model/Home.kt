package com.konkuk.medicarecall.domain.model

data class Home(
    val elderName: String = "",
    val balloonMessage: String = "",
    val isEaten: Boolean = false,
    val breakfastEaten: Boolean? = null,
    val lunchEaten: Boolean? = null,
    val dinnerEaten: Boolean? = null,
    val totalTaken: Int? = null,
    val totalGoal: Int? = null,
    val medicines: List<Medicines> = emptyList(),
    val sleep: HomeSleep? = null,
    val healthStatus: String? = null,
    val mentalStatus: String? = null,
    val glucoseLevelAverageToday: Int? = null,
    val unreadNotification: Int? = null,
)

data class HomeSleep(
    val totalSleepHours: Int?,
    val totalSleepMinutes: Int?,
    val isRecorded: Boolean, // 홈 카드 판단용
)

data class Medicines(
    val medicineName: String,
    val todayTakenCount: Int?,
    val todayRequiredCount: Int?,
    val nextDoseTime: String?,
    val doseStatusList: List<HomeDoseStatusList>? = null,
)

data class HomeDoseStatusList(
    val time: String, // "아침", "점심", "저녁"
    val taken: Boolean?, // true: 먹음, false: 안 먹음, null: 미기록
)
