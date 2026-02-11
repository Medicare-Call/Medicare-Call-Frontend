package com.konkuk.medicarecall.data.mapper

import android.util.Log
import com.konkuk.medicarecall.data.dto.response.SleepResponseDto
import com.konkuk.medicarecall.domain.model.Sleep
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaLocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun SleepResponseDto.toModel() = Sleep(
    date = this.date,
    totalSleepHours = this.totalSleep?.hours,
    totalSleepMinutes = this.totalSleep?.minutes,
    bedTime = formatTime(this.sleepTime),
    wakeUpTime = formatTime(this.wakeTime),
)

private fun formatTime(timeStr: String?): String {
    // 서버에서 받은 시간이 "HH:mm" 형식이 아닐 경우를 대비한 방어 코드
    if (timeStr.isNullOrBlank() || !timeStr.contains(":")) return ""
    return try {
        val parsedTime = LocalTime.parse(timeStr)
        parsedTime.toJavaLocalTime().format(DateTimeFormatter.ofPattern("a hh:mm", Locale.KOREAN))
    } catch (e: IllegalArgumentException) {
        Log.w("SleepMapper", "Failed to parse time: $timeStr", e)
        ""
    }
}
