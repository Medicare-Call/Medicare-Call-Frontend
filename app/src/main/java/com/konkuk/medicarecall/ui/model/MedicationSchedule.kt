package com.konkuk.medicarecall.ui.model

import com.konkuk.medicarecall.domain.model.type.MedicationTime

data class MedicationSchedule(
    val medicationName: String,
    val scheduleTimes: List<MedicationTime>,
)
