package com.konkuk.medicarecall.ui.feature.login.elder.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import com.konkuk.medicarecall.domain.model.Elder
import com.konkuk.medicarecall.domain.model.Medication
import com.konkuk.medicarecall.domain.model.type.ElderResidence
import com.konkuk.medicarecall.domain.model.type.GenderType
import com.konkuk.medicarecall.domain.model.type.HealthIssueType
import com.konkuk.medicarecall.domain.model.type.MedicationTime
import com.konkuk.medicarecall.domain.model.type.Relationship

data class LoginElderUiState(
    val selectedIndex: Int = 0,
    val eldersList: List<LoginElderData> = listOf(LoginElderData()),
    val selectedMedicationTimes: Set<MedicationTime> = emptySet(),
    val diseaseInputText: TextFieldState = TextFieldState(""),
    val medicationInputText: TextFieldState = TextFieldState(""),
)

data class LoginElderData(
    val id: Long = 0L,
    val nameState: TextFieldState = TextFieldState(""),
    val birthDateState: TextFieldState = TextFieldState(""),
    val gender: GenderType? = null,
    val phoneNumberState: TextFieldState = TextFieldState(""),
    val relationship: Relationship? = null,
    val livingType: ElderResidence? = null,
    val diseases: List<String> = emptyList(),
    val medications: List<Medication> = emptyList(),
    val notes: List<HealthIssueType> = emptyList(),
) {
    fun toModel(): Elder {
        require(this.gender != null && this.relationship != null && this.livingType != null)

        return Elder(
            id = this.id,
            name = this.nameState.text.toString(),
            birthDate = this.birthDateState.text.toString(),
            gender = this.gender,
            phoneNumber = this.phoneNumberState.text.toString(),
            relationship = this.relationship,
            residenceType = this.livingType,
            diseases = this.diseases,
            medication = this.medications,
            notes = this.notes,
        )
    }

    companion object {
        fun Elder.toLoginElderData(): LoginElderData {
            return LoginElderData(
                id = this.id,
                nameState = TextFieldState(this.name),
                birthDateState = TextFieldState(this.birthDate),
                gender = this.gender,
                phoneNumberState = TextFieldState(this.phoneNumber),
                relationship = this.relationship,
                livingType = this.residenceType,
                diseases = this.diseases,
                medications = this.medication,
                notes = this.notes,
            )
        }
    }
}
