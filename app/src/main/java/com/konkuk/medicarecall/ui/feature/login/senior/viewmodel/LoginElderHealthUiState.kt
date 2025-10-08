package com.konkuk.medicarecall.ui.feature.login.senior.viewmodel

import com.konkuk.medicarecall.ui.model.ElderHealthData
import com.konkuk.medicarecall.ui.type.MedicationTimeType

data class LoginElderHealthUiState(
    val elderHealthList: List<ElderHealthData> = listOf(ElderHealthData()),
    val selectedIndex: Int = 0,
    val selectedMedicationTimes: List<MedicationTimeType> = emptyList(),
    val diseaseInputText: String = "",
    val medicationInputText: String = "",
)
