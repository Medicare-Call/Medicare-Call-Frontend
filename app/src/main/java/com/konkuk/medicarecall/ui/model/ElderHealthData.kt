package com.konkuk.medicarecall.ui.model

import com.konkuk.medicarecall.domain.model.type.MedicationTime

data class ElderHealthData(
    val diseaseNames: List<String> = emptyList(),
    val medicationMap: Map<MedicationTime, List<String>> = emptyMap(),
    val notes: List<String> = emptyList(),
    var id: Int? = null,
)
