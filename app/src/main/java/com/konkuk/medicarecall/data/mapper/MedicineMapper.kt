package com.konkuk.medicarecall.data.mapper

import com.konkuk.medicarecall.data.dto.response.MedicineResponseDto
import com.konkuk.medicarecall.domain.model.DoseStatus
import com.konkuk.medicarecall.domain.model.DoseStatusItem
import com.konkuk.medicarecall.domain.model.Medicine
import com.konkuk.medicarecall.ui.feature.homedetail.medicine.viewmodel.MedicineUiState
import com.konkuk.medicarecall.ui.feature.homedetail.medicine.viewmodel.DoseStatus as UiDoseStatus
import com.konkuk.medicarecall.ui.feature.homedetail.medicine.viewmodel.DoseStatusItem as UiDoseStatusItem


fun MedicineResponseDto.toMedicines(): List<Medicine> {
    if (medications.isEmpty()) return emptyList()

    val order = listOf("MORNING", "LUNCH", "DINNER")

    return medications.map { med ->
        // 서버 응답에서 각 시간대별 복약 상태 추출
        val doseStatusList = order.mapNotNull { slot ->
            med.times.find { it.time == slot }?.let { t ->
                DoseStatusItem(
                    time = slot,
                    doseStatus = when (t.taken) {
                        true -> DoseStatus.TAKEN          // 먹음
                        false -> DoseStatus.SKIPPED       // 안먹음
                        null -> DoseStatus.NOT_RECORDED   // 미기록
                    }
                )
            }
        }

        Medicine(
            medicineName = med.type,
            todayTakenCount = med.takenCount,
            todayRequiredCount = med.goalCount,
            doseStatusList = doseStatusList,
        )
    }
}

fun List<Medicine>.toMedicineUiStates(): List<MedicineUiState> {
    val kor = mapOf("MORNING" to "아침", "LUNCH" to "점심", "DINNER" to "저녁")

    return map { med ->
        val uiDoseStatusList = med.doseStatusList.map { dose ->
            UiDoseStatusItem(
                time = kor[dose.time] ?: dose.time,
                doseStatus = when (dose.doseStatus) {
                    DoseStatus.TAKEN -> UiDoseStatus.TAKEN
                    DoseStatus.SKIPPED -> UiDoseStatus.SKIPPED
                    DoseStatus.NOT_RECORDED -> UiDoseStatus.NOT_RECORDED
                }
            )
        }

        // 목표 횟수만큼 UI 칸 채우기
        val padded = if (uiDoseStatusList.size < (med.todayRequiredCount ?: 0)) {
            uiDoseStatusList + List((med.todayRequiredCount ?: 0) - uiDoseStatusList.size) {
                UiDoseStatusItem("", UiDoseStatus.NOT_RECORDED)
            }
        } else {
            uiDoseStatusList.take(med.todayRequiredCount ?: uiDoseStatusList.size)
        }

        MedicineUiState(
            medicineName = med.medicineName,
            todayRequiredCount = med.todayRequiredCount ?: 0,
            doseStatusList = padded,
        )
    }
}
