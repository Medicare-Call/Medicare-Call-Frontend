package com.konkuk.medicarecall.data.mapper

import com.konkuk.medicarecall.data.dto.request.ElderHealthRegisterRequestDto
import com.konkuk.medicarecall.data.dto.request.MedicationSchedule
import com.konkuk.medicarecall.data.dto.response.EldersHealthResponseDto
import com.konkuk.medicarecall.domain.model.ElderHealthInfo
import com.konkuk.medicarecall.domain.model.type.MedicationTime

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

    fun toMedicationSchedules(medications: Map<MedicationTime, List<String>>): List<MedicationSchedule> {
        val timesByMed = linkedMapOf<String, MutableSet<MedicationTime>>()
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
}
