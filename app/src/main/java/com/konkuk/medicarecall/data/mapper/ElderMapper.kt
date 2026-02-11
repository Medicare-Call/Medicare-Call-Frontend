package com.konkuk.medicarecall.data.mapper

import com.konkuk.medicarecall.data.dto.request.ElderBulkHealthInfoRequestDto
import com.konkuk.medicarecall.data.dto.request.ElderBulkRegisterRequestDto
import com.konkuk.medicarecall.data.dto.response.ElderResponseDto
import com.konkuk.medicarecall.domain.model.Elder
import com.konkuk.medicarecall.domain.model.Medication
import com.konkuk.medicarecall.domain.model.type.ElderResidence
import com.konkuk.medicarecall.domain.model.type.GenderType
import com.konkuk.medicarecall.domain.model.type.Relationship

fun List<ElderResponseDto>.toModels(): List<Elder> = this.map { it.toModel() }

fun ElderResponseDto.toModel(): Elder = Elder(
    id = this.elderId,
    name = this.name,
    birthDate = this.birthDate,
    gender = GenderType.fromString(this.gender),
    phoneNumber = this.phone,
    relationship = Relationship.fromString(this.relationship),
    residenceType = ElderResidence.fromString(this.residenceType),
)

fun Elder.toElderBulkRequestDto(): ElderBulkRegisterRequestDto.ElderInfo = ElderBulkRegisterRequestDto.ElderInfo(
    name = this.name,
    birthDate = this.birthDate,
    gender = this.gender.name,
    phone = this.phoneNumber,
    relationship = this.relationship.name,
    residenceType = this.residenceType.name,
)

fun Elder.toElderHealthBulkRequestDto(): ElderBulkHealthInfoRequestDto.HealthInfo = ElderBulkHealthInfoRequestDto.HealthInfo(
    elderId = this.id,
    diseaseNames = this.diseases,
    medicationSchedules = this.medication.map { it.toRequestDto() },
    notes = this.notes.map { it.name },
)

fun Medication.toRequestDto() = ElderBulkHealthInfoRequestDto.HealthInfo.MedicationSchedule(
    medicationName = this.medicine,
    scheduleTimes = this.times.map { it.name },
)
