package com.konkuk.medicarecall.domain.model

import com.konkuk.medicarecall.domain.model.type.MedicationTime

data class Medication(
    val medicine: String = "",
    val times: List<MedicationTime> = emptyList(),
)
