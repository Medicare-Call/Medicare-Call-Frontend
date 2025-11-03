package com.konkuk.medicarecall.ui.feature.homedetail.sleep.viewmodel

data class SleepUiState(
    val date: String, // "2025-07-07"
    val totalSleepHours: Int, // 8          /총 수면 시간
    val totalSleepMinutes: Int, // 12         /총 수면 분
    val bedTime: String, // "2025-07-07T22:12"
    val wakeUpTime: String, // "2025-07-08T06:00"
    val isRecorded: Boolean, // 기록 여부
) {
    companion object {
        val EMPTY = SleepUiState(
            date = "",
            totalSleepHours = 0,
            totalSleepMinutes = 0,
            bedTime = "",
            wakeUpTime = "",
            isRecorded = false,
        )
    }
}
