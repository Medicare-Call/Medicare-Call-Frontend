package com.konkuk.medicarecall.ui.feature.login.elder.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.exception.HttpException
import com.konkuk.medicarecall.data.repository.ElderRegisterRepository
import com.konkuk.medicarecall.ui.type.ElderResidenceType
import com.konkuk.medicarecall.ui.type.RelationshipType
import com.konkuk.medicarecall.ui.type.GenderType
import com.konkuk.medicarecall.ui.type.MedicationTimeType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LoginElderViewModel(
    private val elderRegisterRepository: ElderRegisterRepository,
) : ViewModel() {

    private val _loginElderUiState = MutableStateFlow(LoginElderUiState())
    val loginElderUiState: StateFlow<LoginElderUiState> = _loginElderUiState.asStateFlow()

    // ------------------어르신 기본 정보 관련 함수------------------

    fun updateElderGender(gender: GenderType) {
        _loginElderUiState.update { state ->
            state.copy(
                eldersList = state.eldersList.mapIndexed { index, elder ->
                    if (index == state.selectedIndex) elder.copy(gender = gender) else elder
                },
            )
        }
    }

    fun updateElderRelationship(relationship: RelationshipType) {
        _loginElderUiState.update { state ->
            state.copy(
                eldersList = state.eldersList.mapIndexed { index, elder ->
                    if (index == state.selectedIndex) elder.copy(relationship = relationship) else elder
                },
            )
        }
    }

    fun updateElderLivingType(livingType: ElderResidenceType) {
        _loginElderUiState.update { state ->
            state.copy(
                eldersList = state.eldersList.mapIndexed { index, elder ->
                    if (index == state.selectedIndex) elder.copy(livingType = livingType) else elder
                },
            )
        }
    }

    fun selectElder(index: Int) {
        _loginElderUiState.update { state ->
            state.copy(selectedIndex = index)
        }
    }

    fun addElder() {
        _loginElderUiState.update { state ->
            state.copy(
                eldersList = state.eldersList + LoginElderData(),
                selectedIndex = state.eldersList.size,
            )
        }
    }

    fun removeElder(index: Int) {
        _loginElderUiState.update { state ->
            val newSelectedIndex = if (state.selectedIndex >= state.eldersList.size - 1) {
                (state.eldersList.size - 2).coerceAtLeast(0)
            } else {
                state.selectedIndex
            }
            state.copy(
                eldersList = state.eldersList.filterIndexed { i, _ -> i != index },
                selectedIndex = newSelectedIndex,
            )
        }
    }

    fun isInputComplete(): Boolean {
        return loginElderUiState.value.eldersList.all {
            it.nameState.text.isNotBlank() &&
                it.birthDateState.text.length == 8 &&
                it.phoneNumberState.text.length == 11 &&
                it.relationship != null &&
                it.livingType != null
        }
    }

    // ------------------건강정보 관련 함수------------------

    fun updateDiseasesText(text: String) {
        _loginElderUiState.update { state ->
            state.copy(diseaseInputText = text)
        }
    }

    fun updateMedicationText(text: String) {
        _loginElderUiState.update { state ->
            state.copy(medicationInputText = text)
        }
    }

    fun addDisease(disease: String) {
        _loginElderUiState.update { state ->
            state.copy(
                eldersList = state.eldersList.mapIndexed { index, elder ->
                    if (index == state.selectedIndex && disease !in elder.diseases) {
                        elder.copy(diseases = elder.diseases + disease)
                    } else {
                        elder
                    }
                },
            )
        }
    }

    fun removeDisease(disease: String) {
        _loginElderUiState.update { state ->
            state.copy(
                eldersList = state.eldersList.mapIndexed { index, elder ->
                    if (index == state.selectedIndex) {
                        elder.copy(diseases = elder.diseases.filter { it != disease })
                    } else {
                        elder
                    }
                },
            )
        }
    }

    fun addHealthNote(note: String) {
        _loginElderUiState.update { state ->
            state.copy(
                eldersList = state.eldersList.mapIndexed { index, elder ->
                    if (index == state.selectedIndex && note !in elder.notes) {
                        elder.copy(notes = elder.notes + note)
                    } else {
                        elder
                    }
                },
            )
        }
    }

    fun removeHealthNote(note: String) {
        _loginElderUiState.update { state ->
            state.copy(
                eldersList = state.eldersList.mapIndexed { index, elder ->
                    if (index == state.selectedIndex) {
                        elder.copy(notes = elder.notes.filter { it != note })
                    } else {
                        elder
                    }
                },
            )
        }
    }

    fun addMedication(time: MedicationTimeType?, medicine: String) {
        if (time == null) return

        _loginElderUiState.update { state ->
            state.copy(
                eldersList = state.eldersList.mapIndexed { index, elder ->
                    if (index == state.selectedIndex) {
                        val currentList = elder.medicationMap[time] ?: emptyList()
                        if (medicine !in currentList) {
                            val updatedMap = elder.medicationMap + (time to (currentList + medicine))
                            elder.copy(medicationMap = updatedMap)
                        } else {
                            elder
                        }
                    } else {
                        elder
                    }
                },
            )
        }
    }

    fun removeMedication(time: MedicationTimeType, medicine: String) {
        _loginElderUiState.update { state ->
            state.copy(
                eldersList = state.eldersList.mapIndexed { index, elder ->
                    if (index == state.selectedIndex) {
                        val currentList = elder.medicationMap[time] ?: emptyList()
                        val updatedList = currentList.filter { it != medicine }
                        val updatedMap = if (updatedList.isEmpty()) {
                            elder.medicationMap - time
                        } else {
                            elder.medicationMap + (time to updatedList)
                        }
                        elder.copy(medicationMap = updatedMap)
                    } else {
                        elder
                    }
                },
            )
        }
    }

    fun selectMedicationTime(time: MedicationTimeType) {
        _loginElderUiState.update { state ->
            state.copy(
                selectedMedicationTimes = if (time in state.selectedMedicationTimes) {
                    state.selectedMedicationTimes - time
                } else {
                    state.selectedMedicationTimes + time
                },
            )
        }
    }

    // ------------------API 요청------------------

    fun postElderBulk() {
        viewModelScope.launch {
            elderRegisterRepository.postElderBulk(loginElderUiState.value.eldersList)
                .onSuccess { response ->
                    _loginElderUiState.update { state ->
                        state.copy(
                            eldersList = state.eldersList.mapIndexed { index, elderData ->
                                elderData.copy(id = response[index].id.toLong())
                            },
                        )
                    }
                }
                .onFailure { exception ->
                    when (exception) {
                        is HttpException -> {
                            Log.e("httplog", "어르신 일괄등록 실패: ${exception.code()}, ${exception.message}")
                        }
                    }
                }
        }
    }

    suspend fun postElderHealthInfoBulk() {
        elderRegisterRepository.postElderHealthInfoBulk(loginElderUiState.value.eldersList)
            .onSuccess {
                Log.d("elderHealthRegister", "Success")
            }
            .onFailure { exception ->
                when (exception) {
                    is HttpException -> {
                        Log.e("elderHealthRegister", "어르신 건강정보 일괄등록 실패: ${exception.code()}, ${exception.message}")
                    }
                }
            }
    }
}
