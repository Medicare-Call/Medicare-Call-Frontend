package com.konkuk.medicarecall.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class SleepResponseDto(
    val date: String,
    val totalSleep: TotalSleepDto?, // null이면 미기록으로 간주
    val sleepTime: String?, // 취침 시간 (예: "22:12")
    val wakeTime: String?, // 기상 시간 (예: "06:00")
)

@Serializable
data class TotalSleepDto(
    val hours: Int?,
    val minutes: Int?,
)
