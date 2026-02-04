package com.konkuk.medicarecall.data.mapper

import com.konkuk.medicarecall.data.dto.response.MedicineResponseDto
import com.konkuk.medicarecall.ui.feature.homedetail.medicine.viewmodel.DoseStatus
import com.konkuk.medicarecall.ui.feature.homedetail.medicine.viewmodel.DoseStatusItem
import com.konkuk.medicarecall.ui.feature.homedetail.medicine.viewmodel.MedicineUiState

fun MedicineResponseDto.toMedicineUiStates(
    fallback: List<MedicineUiState>,
): List<MedicineUiState> {

    if (medications.isEmpty()) return fallback

    val order = listOf("MORNING", "LUNCH", "DINNER")
    val kor = mapOf("MORNING" to "아침", "LUNCH" to "점심", "DINNER" to "저녁")

    return medications.map { med ->
        val mapped = order.mapNotNull { slot ->
            med.times.find { it.time == slot }?.let { t ->
                DoseStatusItem(
                    time = kor[slot] ?: slot,
                    doseStatus = when (t.taken) {
                        true -> DoseStatus.TAKEN
                        false -> DoseStatus.SKIPPED
                        null -> DoseStatus.NOT_RECORDED
                    }
                )
            }
        }

        val padded =
            if (mapped.size < med.goalCount) {
                mapped + List(med.goalCount - mapped.size) {
                    DoseStatusItem("", DoseStatus.NOT_RECORDED)
                }
            } else {
                mapped.take(med.goalCount)
            }

        MedicineUiState(
            medicineName = med.type,
            todayRequiredCount = med.goalCount,
            doseStatusList = padded,
        )
    }
}
