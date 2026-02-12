package com.konkuk.medicarecall.data.mapper

import com.konkuk.medicarecall.data.dto.request.ElderBulkHealthInfoRequestDto
import com.konkuk.medicarecall.data.dto.request.ElderBulkRegisterRequestDto
import com.konkuk.medicarecall.data.dto.response.ElderResponseDto
import com.konkuk.medicarecall.domain.model.Elder
import com.konkuk.medicarecall.domain.model.ElderInfo
import com.konkuk.medicarecall.domain.model.type.ElderResidence
import com.konkuk.medicarecall.domain.model.type.GenderType
import com.konkuk.medicarecall.domain.model.type.Relationship

fun List<ElderResponseDto>.toModels(): List<Elder> = this.map { it.toModel() }

fun ElderResponseDto.toModel(): Elder = Elder(
    info = ElderInfo(
        elderId = this.elderId,
        name = this.name,
        birthDate = this.birthDate,
        gender = GenderType.fromString(this.gender),
        phone = this.phone,
        relationship = Relationship.fromString(this.relationship),
        residenceType = ElderResidence.fromString(this.residenceType),
    ),
)

fun Elder.toElderBulkRequestDto(): ElderBulkRegisterRequestDto.ElderInfo = ElderBulkRegisterRequestDto.ElderInfo(
    name = this.info.name,
    birthDate = this.info.birthDate,
    gender = this.info.gender.name,
    phone = this.info.phone,
    relationship = this.info.relationship.name,
    residenceType = this.info.residenceType.name,
)

fun Elder.toElderHealthBulkRequestDto(): ElderBulkHealthInfoRequestDto.HealthInfo {
    val schedules = ElderHealthMapper.toMedicationSchedules(this.healthInfo.medications)
    return ElderBulkHealthInfoRequestDto.HealthInfo(
        elderId = this.info.elderId,
        diseaseNames = this.healthInfo.diseases,
        medicationSchedules = schedules.map {
            ElderBulkHealthInfoRequestDto.HealthInfo.MedicationSchedule(
                medicationName = it.medicationName,
                scheduleTimes = it.scheduleTimes.map { time -> time.name },
            )
        },
        notes = this.healthInfo.notes.map { it.name },
    )
}
