package com.konkuk.medicarecall.data.mapper

import com.konkuk.medicarecall.data.dto.request.MedicationSchedule
import com.konkuk.medicarecall.ui.type.MedicationTimeType

object ElderHealthMapper {

    fun toRequestSchedules(uiData: Map<MedicationTimeType, List<String>>): List<MedicationSchedule> {
        val medicationToTimesMap = mutableMapOf<String, MutableList<MedicationTimeType>>()

        uiData.forEach { (timeType, medications) ->
            medications.forEach { medicationName ->
                medicationToTimesMap.getOrPut(medicationName) { mutableListOf() }.add(timeType)
            }
        }

        return medicationToTimesMap.map { (medicationName, medicationTimeType) ->
            MedicationSchedule(
                medicationName = medicationName,
                scheduleTimes = medicationTimeType.map { it },
            )
        }
    }
}
