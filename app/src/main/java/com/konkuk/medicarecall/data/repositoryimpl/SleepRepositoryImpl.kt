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

        return runCatching {
            sleepService.getDailySleep(
                elderId,
                date.toString(),
            )
        }.fold(
            onSuccess = { response ->

                // 서버 응답이 "기록됨" 상태인지 검증
                val isValid =
                    response.totalSleep?.hours != null &&
                        response.totalSleep.minutes != null &&
                        !response.sleepTime.isNullOrBlank() &&
                        !response.wakeTime.isNullOrBlank()

                if (isValid) {
                    SleepUiState(
                        date = response.date,
                        totalSleepHours = response.totalSleep.hours,
                        totalSleepMinutes = response.totalSleep.minutes,
                        bedTime = formatTime(response.sleepTime),
                        wakeUpTime = formatTime(response.wakeTime),
                        isRecorded = true,
                    )
                } else {
                    // 데이터 미기록 상태
                    SleepUiState.EMPTY.copy(date = response.date)
                }
            },
            onFailure = {
                Log.w(
                    "SleepRepository",
                    "Failed to fetch sleep data, fallback to EMPTY",
                    it,
                )
                SleepUiState.EMPTY.copy(date = date.toString())
            },
        )
    }
}
