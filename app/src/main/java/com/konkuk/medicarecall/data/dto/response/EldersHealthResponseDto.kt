package com.konkuk.medicarecall.data.dto.response

import com.konkuk.medicarecall.domain.model.type.HealthIssueType
import com.konkuk.medicarecall.domain.model.type.MedicationTime
import kotlinx.serialization.Serializable

@Serializable
data class EldersHealthResponseDto(
    val elderId: Long,
    val name: String,
    val diseases: List<String> = emptyList(),
    val medications: Map<MedicationTime, List<String>> = emptyMap(),
    val notes: List<HealthIssueType>,
)
