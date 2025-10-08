package com.konkuk.medicarecall.ui.model

import com.konkuk.medicarecall.ui.type.MedicationTimeType

data class ElderHealthData(
    val diseaseNames: List<String> = emptyList(),
    val medicationMap: Map<MedicationTimeType, List<String>> = emptyMap(),
    val notes: List<String> = emptyList(),
    var id: Int? = null,
)
