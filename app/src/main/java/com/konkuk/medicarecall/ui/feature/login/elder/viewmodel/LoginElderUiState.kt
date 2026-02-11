package com.konkuk.medicarecall.ui.feature.login.elder.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import com.konkuk.medicarecall.domain.model.Elder
import com.konkuk.medicarecall.domain.model.ElderHealthInfo
import com.konkuk.medicarecall.domain.model.ElderInfo
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

        val info = ElderInfo(
            elderId = this.id,
            name = this.nameState.text.toString(),
            birthDate = this.birthDateState.text.toString(),
            gender = this.gender,
            phone = this.phoneNumberState.text.toString(),
            relationship = this.relationship,
            residenceType = this.livingType,
        )

        val medicationMap = medications.toMedicationMap()

        val healthInfo = ElderHealthInfo(
            elderId = this.id,
            name = this.nameState.text.toString(),
            diseases = this.diseases,
            medications = medicationMap,
            notes = this.notes,
        )

        return Elder(
            info = info,
            healthInfo = healthInfo,
        )
    }

    companion object {
        fun Elder.toLoginElderData(): LoginElderData {
            return LoginElderData(
                id = this.info.elderId,
                nameState = TextFieldState(this.info.name),
                birthDateState = TextFieldState(this.info.birthDate),
                gender = this.info.gender,
                phoneNumberState = TextFieldState(this.info.phone),
                relationship = this.info.relationship,
                livingType = this.info.residenceType,
                diseases = this.healthInfo.diseases,
                medications = this.healthInfo.medications.toMedicationList(),
                notes = this.healthInfo.notes,
            )
        }

        private fun List<Medication>.toMedicationMap(): Map<MedicationTime, List<String>> {
            val result = mutableMapOf<MedicationTime, MutableList<String>>()
            for (med in this) {
                for (time in med.times) {
                    result.getOrPut(time) { mutableListOf() }.add(med.medicine)
                }
            }
            return result
        }

        private fun Map<MedicationTime, List<String>>.toMedicationList(): List<Medication> {
            val timesByMed = linkedMapOf<String, MutableList<MedicationTime>>()
            for ((time, meds) in this) {
                for (med in meds) {
                    timesByMed.getOrPut(med) { mutableListOf() }.add(time)
                }
            }
            return timesByMed.map { (medName, times) ->
                Medication(medicine = medName, times = times)
            }
        }
    }
}
