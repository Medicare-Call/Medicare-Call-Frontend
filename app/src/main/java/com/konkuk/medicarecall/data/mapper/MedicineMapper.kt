package com.konkuk.medicarecall.data.mapper

import com.konkuk.medicarecall.data.dto.response.MedicineResponseDto
import com.konkuk.medicarecall.domain.model.DoseStatus
import com.konkuk.medicarecall.domain.model.DoseStatusItem
import com.konkuk.medicarecall.domain.model.Medicine

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
                        true -> DoseStatus.TAKEN // 먹음
                        false -> DoseStatus.SKIPPED // 안먹음
                        null -> DoseStatus.NOT_RECORDED // 미기록
                    },
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
