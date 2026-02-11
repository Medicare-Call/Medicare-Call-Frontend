package com.konkuk.medicarecall.data.mapper

import com.konkuk.medicarecall.data.dto.response.HomeResponseDto
import com.konkuk.medicarecall.domain.model.ElderHealthInfo
import com.konkuk.medicarecall.domain.model.Home
import com.konkuk.medicarecall.domain.model.HomeDoseStatusList
import com.konkuk.medicarecall.domain.model.HomeSleep
import com.konkuk.medicarecall.domain.model.Medicines
import com.konkuk.medicarecall.ui.feature.home.viewmodel.DoseStatusUiState
import com.konkuk.medicarecall.ui.feature.home.viewmodel.HomeUiState
import com.konkuk.medicarecall.ui.feature.home.viewmodel.MedicineUiState

// 메인 변환 함수들 (DTO → Model → UiState)

fun HomeResponseDto.toHome(): Home = Home(
    elderName = elderName,
    balloonMessage = aiSummary.orEmpty(),
    isEaten = listOf(mealStatus.breakfast, mealStatus.lunch, mealStatus.dinner).any { it == true },
    breakfastEaten = mealStatus.breakfast,
    lunchEaten = mealStatus.lunch,
    dinnerEaten = mealStatus.dinner,
    totalTaken = medicationStatus.totalTaken,
    totalGoal = medicationStatus.totalGoal,
    medicines = medicationStatus.medicationList
        ?.map { it.toMedicines() }
        ?: emptyList(),
    sleep = sleep?.let { HomeSleep(it.meanHours, it.meanMinutes, it.meanHours != null || it.meanMinutes != null) },
    healthStatus = healthStatus,
    mentalStatus = mentalStatus,
    glucoseLevelAverageToday = bloodSugar?.meanValue,
    unreadNotification = unreadNotification,
)

// Medication DTO → Domain
private fun HomeResponseDto.MedicationDto.toMedicines(): Medicines {
    return Medicines(
        medicineName = type.orEmpty(),
        todayTakenCount = taken,
        todayRequiredCount = goal,
        nextDoseTime = nextTime,
        doseStatusList = doseStatusList?.map { it.toHomeDoseStatus() } ?: emptyList(),
    )
}

// Dose DTO → Domain
private fun HomeResponseDto.DoseStatusDto.toHomeDoseStatus(): HomeDoseStatusList {
    return HomeDoseStatusList(
        time = time.orEmpty(),
        taken = taken,
    )
}

fun Home.toUiState(
    healthInfo: ElderHealthInfo?,
    elderName: String,
): HomeUiState {
    val mergedMedicines = mergeMedicinesForHome(medicines, healthInfo)

    return HomeUiState(
        isLoading = false,
        elderName = elderName,
        balloonMessage = balloonMessage,
        isEaten = isEaten,
        breakfastEaten = breakfastEaten,
        lunchEaten = lunchEaten,
        dinnerEaten = dinnerEaten,
        totalTaken = totalTaken,
        totalGoal = totalGoal,
        medicines = mergedMedicines,
        sleep = sleep,
        healthStatus = healthStatus,
        mentalStatus = mentalStatus,
        glucoseLevelAverageToday = glucoseLevelAverageToday,
        unreadNotification = unreadNotification,
    )
}

// Home의 Medicines
private fun mergeMedicinesForHome(
    medicines: List<Medicines>,
    healthInfo: ElderHealthInfo?,
): List<MedicineUiState> {
    if (medicines.isEmpty()) return emptyList()

    return medicines.map { med ->
        MedicineUiState(
            medicineName = med.medicineName,
            todayTakenCount = med.todayTakenCount,
            todayRequiredCount = med.todayRequiredCount,
            nextDoseTime = med.nextDoseTime,
            doseStatusList = med.doseStatusList?.map { dose ->
                DoseStatusUiState(
                    time = dose.time,
                    taken = dose.taken,
                )
            } ?: emptyList(),
        )
    }.let { list ->
        // healthInfo 순서로 정렬
        val correctOrder = healthInfo?.medications
            ?.flatMap { it.value }
            ?.distinct()
            ?: emptyList()

        list.sortedBy { med ->
            correctOrder.indexOf(med.medicineName).let {
                if (it == -1) Int.MAX_VALUE else it
            }
        }
    }
}

// Fallback 로직 (서버 에러 시 사용)

object HomeMapper {

    // 서버 에러 시 설정 정보로 기본 화면 만들기 (복약 기록 0으로 표시)
    fun fromHealthInfo(
        healthInfo: ElderHealthInfo?,
        elderName: String,
    ): HomeUiState {
        val medicines = createFallbackMedicines(healthInfo)
        val sortedMedicines = sortMedicinesByHealthInfo(medicines, healthInfo)

        return HomeUiState(
            isLoading = false,
            elderName = elderName,
            medicines = sortedMedicines,
        )
    }

    // 기본 약 목록 (설정 기준)
    private fun createFallbackMedicines(
        healthInfo: ElderHealthInfo?,
    ): List<MedicineUiState> {
        val medications = healthInfo?.medications

        if (medications.isNullOrEmpty()) {
            return listOf(
                MedicineUiState(
                    medicineName = "복약 정보 없음",
                    todayTakenCount = 0,
                    todayRequiredCount = 0,
                    nextDoseTime = "-",
                    doseStatusList = emptyList(),
                ),
            )
        }

        val defaultNextDose = getDefaultNextDose(medications.keys.firstOrNull())

        return medications
            .flatMap { (time, medNames) -> medNames.map { medName -> medName to time } }
            .groupBy { it.first }
            .map { (medName, group) ->
                MedicineUiState(
                    medicineName = medName,
                    todayTakenCount = 0,
                    todayRequiredCount = group.size,
                    nextDoseTime = defaultNextDose,
                    doseStatusList = emptyList(),
                )
            }
    }

    // 약 순서 정렬 (설정 기준)
    private fun sortMedicinesByHealthInfo(
        medicines: List<MedicineUiState>,
        healthInfo: ElderHealthInfo?,
    ): List<MedicineUiState> {
        val correctOrder = healthInfo?.medications
            ?.flatMap { it.value }
            ?.distinct()
            ?: emptyList()

        return medicines.sortedBy { med ->
            correctOrder.indexOf(med.medicineName).let {
                if (it == -1) Int.MAX_VALUE else it
            }
        }
    }

    private fun getDefaultNextDose(firstTimeKey: Any?): String {
        return when (firstTimeKey?.toString()?.uppercase()) {
            "MORNING" -> "아침"
            "LUNCH" -> "점심"
            "DINNER" -> "저녁"
            else -> "-"
        }
    }
}
