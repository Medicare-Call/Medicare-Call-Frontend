package com.konkuk.medicarecall.ui.feature.login.elder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.exception.HttpException
import com.konkuk.medicarecall.data.repository.ElderRegisterRepository
import com.konkuk.medicarecall.data.repository.EldersInfoRepository

import com.konkuk.medicarecall.domain.model.Medication
import com.konkuk.medicarecall.domain.model.type.ElderResidence
import com.konkuk.medicarecall.domain.model.type.GenderType
import com.konkuk.medicarecall.domain.model.type.HealthIssueType
import com.konkuk.medicarecall.domain.model.type.MedicationTime
import com.konkuk.medicarecall.domain.model.type.Relationship
import com.konkuk.medicarecall.ui.feature.login.elder.viewmodel.LoginElderData.Companion.toLoginElderData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LoginElderViewModel(
    private val elderRegisterRepository: ElderRegisterRepository,
    private val elderInfoRepository: EldersInfoRepository,
) : ViewModel() {

    private val _loginElderUiState = MutableStateFlow(LoginElderUiState())
    val loginElderUiState: StateFlow<LoginElderUiState> =
        _loginElderUiState.onStart {
            fetchElderList()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LoginElderUiState(),
        )

    private val _uiEvent = Channel<LoginElderEvent>()
    val uiEvent: Flow<LoginElderEvent> = _uiEvent.receiveAsFlow()

    private fun fetchElderList() {
        viewModelScope.launch {
            elderInfoRepository.getEldersV2()
                .onSuccess { data ->
                    _loginElderUiState.update { state ->
                        state.copy(
                            eldersList = data.map { it.toLoginElderData() },
                        )
                    }
                }.onFailure { error ->
                }
        }
    }

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

    fun updateElderRelationship(relationship: Relationship) {
        _loginElderUiState.update { state ->
            state.copy(
                eldersList = state.eldersList.mapIndexed { index, elder ->
                    if (index == state.selectedIndex) elder.copy(relationship = relationship) else elder
                },
            )
        }
    }

    fun updateElderLivingType(livingType: ElderResidence) {
        _loginElderUiState.update { state ->
            state.copy(
                eldersList = state.eldersList.mapIndexed { index, elder ->
                    if (index == state.selectedIndex) elder.copy(livingType = livingType) else elder
                },
            )
        }
    }

    fun setSelectedIndex(index: Int) {
        _loginElderUiState.update { state ->
            state.copy(
                selectedIndex = index,
                selectedMedicationTimes = emptySet(), // 인덱스 변경 시 복약 타임 초기화
            )
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

    fun addHealthNote(note: HealthIssueType) {
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

    fun removeHealthNote(note: HealthIssueType) {
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

    fun addMedication(medicine: String) {
        _loginElderUiState.update { state ->
            val selectedTimes = state.selectedMedicationTimes.toList()
            if (selectedTimes.isEmpty()) return@update state

            state.copy(
                eldersList = state.eldersList.mapIndexed { index, elder ->
                    if (index == state.selectedIndex) {
                        val existingMed = elder.medications.find { it.medicine == medicine }
                        val updatedMedications = if (existingMed != null) {
                            elder.medications.map {
                                if (it.medicine == medicine) {
                                    it.copy(times = (it.times + selectedTimes).distinct())
                                } else it
                            }
                        } else {
                            elder.medications + Medication(medicine, selectedTimes)
                        }
                        elder.copy(medications = updatedMedications)
                    } else {
                        elder
                    }
                },
                selectedMedicationTimes = emptySet(),
            )
        }
    }

    fun removeMedication(medication: Medication) {
        _loginElderUiState.update { state ->
            state.copy(
                eldersList = state.eldersList.mapIndexed { index, elder ->
                    if (index == state.selectedIndex) {
                        elder.copy(medications = elder.medications.filter { it != medication })
                    } else {
                        elder
                    }
                },
            )
        }
    }

    fun selectMedicationTime(time: MedicationTime) {
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
                .onSuccess { data ->
                    _loginElderUiState.update { state ->
                        state.copy(
                            eldersList = data.map { it.toLoginElderData() },
                        )
                    }
                }
                .onFailure { exception ->
                    when (exception) {
                        is HttpException -> {
                        }
                    }
                }
        }
    }

    fun postElderHealthInfoBulk() {
        viewModelScope.launch {
            elderRegisterRepository.postElderHealthInfoBulk(loginElderUiState.value.eldersList)
                .onSuccess {
                    _uiEvent.send(LoginElderEvent.NavigateToCareCallSetting)
                }
                .onFailure { exception ->
                    when (exception) {
                        is HttpException -> {
                        }
                    }
                }
        }
    }
}
