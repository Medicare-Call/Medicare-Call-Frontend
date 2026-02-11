package com.konkuk.medicarecall.domain.model

import com.konkuk.medicarecall.ui.type.HealthIssueType
import com.konkuk.medicarecall.ui.type.MedicationTimeType

data class ElderHealthInfo(
    val elderId: Int,
    val name: String,
    val diseases: List<String> = emptyList(),
    val medications: Map<MedicationTimeType, List<String>> = emptyMap(),
    val notes: List<HealthIssueType> = emptyList(),
)
