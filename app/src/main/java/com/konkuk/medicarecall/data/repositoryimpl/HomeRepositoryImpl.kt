package com.konkuk.medicarecall.data.repositoryimpl

import android.util.Log
import com.konkuk.medicarecall.data.api.elders.HomeService
import com.konkuk.medicarecall.data.dto.request.ImmediateCallRequestDto
import com.konkuk.medicarecall.data.dto.response.HomeResponseDto
import com.konkuk.medicarecall.data.repository.HomeRepository
import com.konkuk.medicarecall.ui.feature.home.viewmodel.HomeUiState
import com.konkuk.medicarecall.ui.feature.home.viewmodel.MedicineUiState
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDate
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeService: HomeService,
) : HomeRepository {
    override suspend fun requestImmediateCareCall(
        elderId: Int, careCallOption: String,
    ): Result<Unit> = runCatching {
        val response = homeService.requestImmediateCareCall(
            ImmediateCallRequestDto(elderId, careCallOption),
        )
        if (response.isSuccessful) {
            Log.d(
                "httplog",
                "전화 걸림, 어르신: $Int, 시간: $careCallOption",
            )
        } else {
            val errorBody =
                response.errorBody()?.string() ?: "Unknown error(updating health info)"
            Log.e(
                "httplog",
                "전화 걸기 실패: ${response.code()} - $errorBody",
            )
            throw HttpException(response)
        }
    }

    private fun mapNextTimeToKor(nextTime: String?): String = when (nextTime) {
        "MORNING" -> "아침"
        "LUNCH" -> "점심"
        "DINNER" -> "저녁"
        else -> "-"
    }

    override suspend fun getHomeUiState(elderId: Int, date: LocalDate): HomeUiState {
        return try {
            Log.d("HomeRepo", "[REQ] elderId=$elderId")

            val res = homeService.getHomeSummary(elderId)

            val meds = res.medicationStatus.medicationList.orEmpty()
            Log.d(
                "HomeRepo",
                "[RES] elderName=${res.elderName}, medsCount=${meds.size}, " +
                    "totalTaken=${res.medicationStatus.totalTaken}, totalGoal=${res.medicationStatus.totalGoal}, " +
                    meds.joinToString(prefix = "items=[", postfix = "]") {
                        "type=${it.type}, taken=${it.taken}, goal=${it.goal}, next=${it.nextTime}"
                    },
            )

            HomeUiState(
                elderName = res.elderName,
                balloonMessage = res.aiSummary,
                breakfastEaten = res.mealStatus.breakfast,
                lunchEaten = res.mealStatus.lunch,
                dinnerEaten = res.mealStatus.dinner,

                medicines = res.medicationStatus.medicationList.orEmpty().map {
                    MedicineUiState(
                        medicineName = it.type,
                        todayTakenCount = it.taken,
                        todayRequiredCount = it.goal,
                        nextDoseTime = mapNextTimeToKor(it.nextTime),
                    )
                },

                sleep = res.sleep ?: HomeResponseDto.SleepDto(0, 0),
                healthStatus = res.healthStatus ?: "",
                mentalStatus = res.mentalStatus ?: "",
                glucoseLevelAverageToday = res.bloodSugar?.meanValue ?: 0,
            )
        } catch (e: HttpException) {
            Log.e("HomeRepo", "HTTP error fetching home data: ${e.code()}", e)
            HomeUiState.Companion.EMPTY
        } catch (e: IOException) {
            Log.e("HomeRepo", "Network error fetching home data", e)
            HomeUiState.Companion.EMPTY
        }
    }
}

private fun mapNextTimeToKor(nextTime: String?): String = when (nextTime) {
    "MORNING" -> "아침"
    "LUNCH" -> "점심"
    "DINNER" -> "저녁"
    else -> "-"
}
