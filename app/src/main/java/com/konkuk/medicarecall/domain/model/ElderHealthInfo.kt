package com.konkuk.medicarecall.domain.model

import com.konkuk.medicarecall.domain.model.type.HealthIssueType
import com.konkuk.medicarecall.domain.model.type.MedicationTime

data class ElderHealthInfo(
    val elderId: Long,
    val name: String,
    val diseases: List<String> = emptyList(),
    val medications: Map<MedicationTime, List<String>> = emptyMap(),
    val notes: List<HealthIssueType> = emptyList(),
)
