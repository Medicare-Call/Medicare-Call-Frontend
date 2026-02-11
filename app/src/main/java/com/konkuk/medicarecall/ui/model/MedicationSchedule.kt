package com.konkuk.medicarecall.ui.model

import com.konkuk.medicarecall.ui.type.MedicationTimeType

data class MedicationSchedule(
    val medicationName: String,
    val scheduleTimes: List<MedicationTimeType>,
)
