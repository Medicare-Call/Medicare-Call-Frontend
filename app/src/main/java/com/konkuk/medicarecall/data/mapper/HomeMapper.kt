package com.konkuk.medicarecall.data.mapper

import com.konkuk.medicarecall.data.dto.response.EldersHealthResponseDto
import com.konkuk.medicarecall.data.dto.response.HomeResponseDto
import com.konkuk.medicarecall.domain.model.Home
import com.konkuk.medicarecall.domain.model.HomeSleep
import com.konkuk.medicarecall.ui.feature.home.viewmodel.DoseStatusUiState
import com.konkuk.medicarecall.ui.feature.home.viewmodel.HomeUiState
import com.konkuk.medicarecall.ui.feature.home.viewmodel.MedicineUiState

object HomeMapper {

    fun from(dto: HomeResponseDto): HomeUiState {
        return HomeUiState(
            isLoading = false,

            // 기본 정보
            elderName = dto.elderName,
            balloonMessage = dto.aiSummary.orEmpty(),

            // 식사
            isEaten = listOf(
                dto.mealStatus.breakfast,
                dto.mealStatus.lunch,
                dto.mealStatus.dinner,
            ).any { it == true },

            breakfastEaten = dto.mealStatus.breakfast,
            lunchEaten = dto.mealStatus.lunch,
            dinnerEaten = dto.mealStatus.dinner,

            // 복약 요약
            totalTaken = dto.medicationStatus.totalTaken,
            totalGoal = dto.medicationStatus.totalGoal,

            medicines = dto.medicationStatus.medicationList?.map { med ->
                MedicineUiState(
                    medicineName = med.type.orEmpty(),
                    todayTakenCount = med.taken,
                    todayRequiredCount = med.goal,
                    nextDoseTime = med.nextTime,
                    doseStatusList = med.doseStatusList?.map { dose ->
                        DoseStatusUiState(
                            time = dose.time.orEmpty(),
                            taken = dose.taken,
                        )
                    } ?: emptyList(),
                )
            } ?: emptyList(),

            // 기타
            sleep = dto.sleep?.let {
                HomeSleep(
                    totalSleepHours = it.meanHours,
                    totalSleepMinutes = it.meanMinutes,
                    isRecorded = it.meanHours != null || it.meanMinutes != null,
                )
            },
            healthStatus = dto.healthStatus,
            mentalStatus = dto.mentalStatus,
            glucoseLevelAverageToday = dto.bloodSugar?.meanValue,
            unreadNotification = dto.unreadNotification,
        )
    }

    // 서버 데이터 + 설정 정보 합쳐서 최종 홈 화면 생성 (약 보완/정렬, 최신 이름 사용)
    fun mergeWithHealthInfo(
        dto: HomeResponseDto,
        healthInfo: EldersHealthResponseDto?,
        elderName: String,
    ): HomeUiState {
        val baseUi = from(dto)
        val mergedMedicines = mergeMedicinesWithHealthInfo(baseUi.medicines, healthInfo)

        return baseUi.copy(
            elderName = elderName,
            medicines = mergedMedicines,
        )
    }

    // 서버 에러 시 설정 정보로 기본 화면 만들기 (복약 기록 0으로 표시)
    fun fromHealthInfo(
        healthInfo: EldersHealthResponseDto?,
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

    // 약 정보 처리

    private fun mergeMedicinesWithHealthInfo(
        serverMedicines: List<MedicineUiState>,
        healthInfo: EldersHealthResponseDto?,
    ): List<MedicineUiState> {
        val medicines = if (serverMedicines.isNotEmpty()) {
            serverMedicines
        } else {
            createFallbackMedicines(healthInfo)
        }

        return sortMedicinesByHealthInfo(medicines, healthInfo)
    }

    // 기본 약 목록 (설정 기준)

    private fun createFallbackMedicines(
        healthInfo: EldersHealthResponseDto?,
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

    // 약 순서 정렬 (설정기준)

    private fun sortMedicinesByHealthInfo(
        medicines: List<MedicineUiState>,
        healthInfo: EldersHealthResponseDto?,
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

// DTO → Home Model
fun HomeResponseDto.toHome(): Home = Home(
    elderName = elderName,
    balloonMessage = aiSummary.orEmpty(),
    isEaten = listOf(mealStatus.breakfast, mealStatus.lunch, mealStatus.dinner).any { it == true },
    breakfastEaten = mealStatus.breakfast,
    lunchEaten = mealStatus.lunch,
    dinnerEaten = mealStatus.dinner,
    totalTaken = medicationStatus.totalTaken,
    totalGoal = medicationStatus.totalGoal,
    medicines = medicationStatus.medicationList?.map { med ->
        Medicines(
            medicineName = med.type.orEmpty(),
            todayTakenCount = med.taken,
            todayRequiredCount = med.goal,
            nextDoseTime = med.nextTime,
            doseStatusList = med.doseStatusList?.map { dose ->
                DoseStatusList(
                    time = dose.time.orEmpty(),
                    taken = dose.taken,
                )
            },
        )
    } ?: emptyList(),
    sleep = sleep?.let { HomeSleep(it.meanHours, it.meanMinutes, it.meanHours != null || it.meanMinutes != null) },
    healthStatus = healthStatus,
    mentalStatus = mentalStatus,
    glucoseLevelAverageToday = bloodSugar?.meanValue,
    unreadNotification = unreadNotification,
)

// Home Model → HomeUiState (healthInfo 병합 버전)
fun Home.toUiState(
    healthInfo: EldersHealthResponseDto?,
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

// Home의 Medicines → MedicineUiState 변환 + healthInfo 병합
private fun mergeMedicinesForHome(
    medicines: List<com.konkuk.medicarecall.domain.model.Medicines>,
    healthInfo: EldersHealthResponseDto?,
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
