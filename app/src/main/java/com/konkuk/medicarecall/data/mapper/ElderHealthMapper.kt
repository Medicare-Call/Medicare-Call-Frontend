package com.konkuk.medicarecall.data.mapper

import com.konkuk.medicarecall.data.dto.request.ElderHealthRegisterRequestDto
import com.konkuk.medicarecall.data.dto.request.MedicationSchedule
import com.konkuk.medicarecall.data.dto.response.EldersHealthResponseDto
import com.konkuk.medicarecall.ui.model.ElderHealthInfo
import com.konkuk.medicarecall.ui.type.MedicationTimeType

object ElderHealthMapper {

    // ResponseDto → Domain Model
    fun toDomain(dto: EldersHealthResponseDto): ElderHealthInfo {
        return ElderHealthInfo(
            elderId = dto.elderId,
            name = dto.name,
            diseases = dto.diseases,
            medications = dto.medications,
            notes = dto.notes,
        )
    }

    // Domain Model → RequestDto
    fun toRequestDto(model: ElderHealthInfo): ElderHealthRegisterRequestDto {
        val medicationSchedules = toMedicationSchedules(model.medications)
        return ElderHealthRegisterRequestDto(
            diseaseNames = model.diseases,
            medicationSchedules = medicationSchedules,
            notes = model.notes,
        )
    }

    // Map<MedicationTimeType, List<String>> → List<MedicationSchedule>
    private fun toMedicationSchedules(medications: Map<MedicationTimeType, List<String>>): List<MedicationSchedule> {
        val timesByMed = linkedMapOf<String, MutableSet<MedicationTimeType>>()
        for ((time, meds) in medications) {
            for (med in meds) {
                timesByMed.getOrPut(med.trim()) { linkedSetOf() }.add(time)
            }
        }
        return timesByMed.map { (medName, times) ->
            MedicationSchedule(
                medicationName = medName,
                scheduleTimes = times.sortedBy { it.ordinal },
            )
        }
    }

    // Deprecated: Use toRequestDto instead
    @Deprecated("Use toRequestDto instead", ReplaceWith("toMedicationSchedules(uiData)"))
    fun toRequestSchedules(uiData: Map<MedicationTimeType, List<String>>): List<MedicationSchedule> {
        return toMedicationSchedules(uiData)
    }
}
