package com.konkuk.medicarecall.data.mapper

import com.konkuk.medicarecall.data.dto.response.SleepResponseDto
import com.konkuk.medicarecall.domain.model.Sleep
import kotlinx.datetime.LocalTime

fun SleepResponseDto.toModel() = Sleep(
    date = this.date,
    totalSleepHours = this.totalSleep?.hours,
    totalSleepMinutes = this.totalSleep?.minutes,
    // 확장 함수 호출
    bedTime = this.sleepTime.toKoreanAmPmFormat(),
    wakeUpTime = this.wakeTime.toKoreanAmPmFormat(),
)

private fun String?.toKoreanAmPmFormat(): String {
    // 1. 유효성 검사
    if (this.isNullOrBlank() || !this.contains(":")) return ""

    return try {
        val parsedTime = LocalTime.parse(this)
        val isAm = parsedTime.hour < 12
        val amPm = if (isAm) "오전" else "오후"
        val hour = if (parsedTime.hour % 12 == 0) 12 else parsedTime.hour % 12

        val minute = parsedTime.minute.toString().padStart(2, '0')
        "$amPm $hour:$minute"
    } catch (e: IllegalArgumentException) {
        ""
    }
}
