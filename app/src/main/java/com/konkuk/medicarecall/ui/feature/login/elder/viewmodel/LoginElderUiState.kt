package com.konkuk.medicarecall.ui.feature.login.elder.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import com.konkuk.medicarecall.ui.type.ElderResidenceType
import com.konkuk.medicarecall.ui.type.GenderType
import com.konkuk.medicarecall.ui.type.MedicationTimeType
import com.konkuk.medicarecall.ui.type.RelationshipType

data class LoginElderUiState(
    val selectedIndex: Int = 0,
    val eldersList: List<LoginElderData> = listOf(LoginElderData()),
    val selectedMedicationTimes: Set<MedicationTimeType> = emptySet(),
    val diseaseInputText: String = "",
    val medicationInputText: String = "",
)

data class LoginElderData(
    val id: Long = 0L,
    val nameState: TextFieldState = TextFieldState(""),
    val birthDateState: TextFieldState = TextFieldState(""),
    val gender: GenderType? = null,
    val phoneNumberState: TextFieldState = TextFieldState(""),
    val relationship: RelationshipType? = null,
    val livingType: ElderResidenceType? = null,
    val diseases: List<String> = emptyList(),
    val medicationMap: Map<MedicationTimeType, List<String>> = emptyMap(),
    val notes: List<String> = emptyList(),
)
