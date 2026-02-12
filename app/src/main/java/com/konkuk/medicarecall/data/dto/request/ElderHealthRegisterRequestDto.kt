package com.konkuk.medicarecall.data.dto.request

import com.konkuk.medicarecall.domain.model.type.HealthIssueType
import com.konkuk.medicarecall.domain.model.type.MedicationTime
import kotlinx.serialization.Serializable

@Serializable
data class ElderHealthRegisterRequestDto(
    val diseaseNames: List<String>? = null,
    val medicationSchedules: List<MedicationSchedule>? = null,
    val notes: List<HealthIssueType>? = null,
)

@Serializable
data class MedicationSchedule(
    val medicationName: String,
    val scheduleTimes: List<MedicationTime>,
)
