package com.konkuk.medicarecall.ui.feature.homedetail.sleep.data

import com.konkuk.medicarecall.ui.feature.homedetail.sleep.model.SleepUiState
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class SleepRepositoryImpl @Inject constructor(
    private val sleepService: SleepService
) : SleepRepository {


    private fun formatTime(timeStr: String?): String {
        // 서버에서 받은 시간이 "HH:mm" 형식이 아닐 경우를 대비한 방어 코드
        if (timeStr.isNullOrBlank() || !timeStr.contains(":")) return ""
        return try {
            val parsedTime = LocalTime.parse(timeStr)
            parsedTime.format(DateTimeFormatter.ofPattern("a hh:mm", Locale.KOREAN))
        } catch (e: Exception) {
            ""
        }
    }

    override suspend fun getSleepUiState(
        elderId: Int,
        date: LocalDate
    ): SleepUiState {
        return try {
            val response = sleepService.getDailySleep(elderId, date.toString())

            // 서버 응답의 모든 값이 유효한지 확인
            if (response.totalSleep?.hours != null && response.totalSleep.minutes != null && !response.sleepTime.isNullOrBlank() && !response.wakeTime.isNullOrBlank()) {
                SleepUiState(
                    date = response.date,
                    totalSleepHours = response.totalSleep.hours,
                    totalSleepMinutes = response.totalSleep.minutes,
                    bedTime = formatTime(response.sleepTime),
                    wakeUpTime = formatTime(response.wakeTime),
                    isRecorded = true
                )
            } else {
                // 데이터 미기록 상태
                SleepUiState.Companion.EMPTY.copy(date = response.date)
            }
        } catch (e: Exception) {
            // API 호출 실패 시의 미기록 상태
            SleepUiState.Companion.EMPTY.copy(date = date.toString())
        }
    }
}
