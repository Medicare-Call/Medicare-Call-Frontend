package com.konkuk.medicarecall.ui.model

import com.konkuk.medicarecall.ui.type.MedicationTimeType

data class ElderHealthData(
    val diseaseNames: List<String>,
    val medicationMap: Map<MedicationTimeType, List<String>>,
    val notes: List<String>,
    var id: Int? = null
)
