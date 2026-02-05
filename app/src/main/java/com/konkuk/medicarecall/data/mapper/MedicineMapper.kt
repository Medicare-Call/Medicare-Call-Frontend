package com.konkuk.medicarecall.data.mapper

import com.konkuk.medicarecall.data.dto.response.MedicineResponseDto
import com.konkuk.medicarecall.domain.model.DoseStatus
import com.konkuk.medicarecall.domain.model.DoseStatusItem
import com.konkuk.medicarecall.domain.model.Medicine
import com.konkuk.medicarecall.ui.feature.homedetail.medicine.viewmodel.MedicineUiState
import com.konkuk.medicarecall.ui.feature.homedetail.medicine.viewmodel.DoseStatus as UiDoseStatus
import com.konkuk.medicarecall.ui.feature.homedetail.medicine.viewmodel.DoseStatusItem as UiDoseStatusItem

// DTO → Model
fun MedicineResponseDto.toMedicines(): List<Medicine> {
    if (medications.isEmpty()) return emptyList()

    val order = listOf("MORNING", "LUNCH", "DINNER")

    return medications.map { med ->
        val doseStatusList = order.mapNotNull { slot ->
            med.times.find { it.time == slot }?.let { t ->
                DoseStatusItem(
                    time = slot,
                    doseStatus = when (t.taken) {
                        true -> DoseStatus.TAKEN
                        false -> DoseStatus.SKIPPED
                        null -> DoseStatus.NOT_RECORDED
                    }
                )
            }
        }

        Medicine(
            medicineName = med.type,
            todayTakenCount = med.takenCount,
            todayRequiredCount = med.goalCount,
            nextDoseTime = med.nextTime,
            doseStatusList = doseStatusList,
        )
    }
}

// Model → UiState
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

        // goalCount만큼 패딩
        val padded = if (uiDoseStatusList.size < (med.todayRequiredCount ?: 0)) {
            uiDoseStatusList + List((med.todayRequiredCount ?: 0) - uiDoseStatusList.size) {
                UiDoseStatusItem("", UiDoseStatus.NOT_RECORDED)
            }
        } else {
            uiDoseStatusList.take(med.todayRequiredCount ?: uiDoseStatusList.size)
        }

        MedicineUiState(
            medicineName = med.medicineName,
            todayTakenCount = med.todayTakenCount,
            todayRequiredCount = med.todayRequiredCount,
            nextDoseTime = med.nextDoseTime,
            doseStatusList = padded,
        )
    }
}
