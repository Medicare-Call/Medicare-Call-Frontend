package com.konkuk.medicarecall.data.repositoryimpl

import android.util.Log
import com.konkuk.medicarecall.data.api.elders.SleepService
import com.konkuk.medicarecall.data.repository.SleepRepository
import com.konkuk.medicarecall.ui.feature.homedetail.sleep.viewmodel.SleepUiState
import org.koin.core.annotation.Single
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

@Single
class SleepRepositoryImpl(
    private val sleepService: SleepService,
) : SleepRepository {

    private fun formatTime(timeStr: String?): String {
        // 서버에서 받은 시간이 "HH:mm" 형식이 아닐 경우를 대비한 방어 코드
        if (timeStr.isNullOrBlank() || !timeStr.contains(":")) return ""
        return try {
            val parsedTime = LocalTime.parse(timeStr)
            parsedTime.format(DateTimeFormatter.ofPattern("a hh:mm", Locale.KOREAN))
        } catch (e: DateTimeParseException) {
            Log.w("SleepRepository", "Failed to parse time: $timeStr", e)
            ""
        }
    }

    override suspend fun getSleepUiState(
        elderId: Int,
        date: LocalDate,
    ): SleepUiState {
        val response = sleepService.getDailySleep(
            elderId,
            date.toString(),
        )

        return if (response.isSuccessful) {
            val body = response.body()
                ?: error("Sleep response body is null")

            // 서버 응답의 모든 값이 유효한지 확인
            if (
                body.totalSleep?.hours != null &&
                body.totalSleep.minutes != null &&
                !body.sleepTime.isNullOrBlank() &&
                !body.wakeTime.isNullOrBlank()
            ) {
                SleepUiState(
                    date = body.date,
                    totalSleepHours = body.totalSleep.hours,
                    totalSleepMinutes = body.totalSleep.minutes,
                    bedTime = formatTime(body.sleepTime),
                    wakeUpTime = formatTime(body.wakeTime),
                    isRecorded = true,
                )
            } else {
                // 데이터 미기록 상태
                SleepUiState.EMPTY.copy(date = body.date)
            }
        } else {
            if (response.code == 404) {
                // 미기록
                SleepUiState.EMPTY.copy(date = date.toString())
            } else {
                error("Failed to fetch sleep data: ${response.code}")
            }
        }
    }
}
