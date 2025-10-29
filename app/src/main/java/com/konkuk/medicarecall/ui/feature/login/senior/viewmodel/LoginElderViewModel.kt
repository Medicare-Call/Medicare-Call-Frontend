package com.konkuk.medicarecall.ui.feature.login.senior.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.repository.ElderIdRepository
import com.konkuk.medicarecall.data.repository.ElderRegisterRepository
import com.konkuk.medicarecall.data.repository.EldersInfoRepository
import com.konkuk.medicarecall.ui.model.ElderData
import com.konkuk.medicarecall.ui.model.ElderHealthData
import com.konkuk.medicarecall.ui.type.MedicationTimeType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class LoginElderViewModel @Inject constructor(
    private val elderRegisterRepository: ElderRegisterRepository,
    private val elderIdRepository: ElderIdRepository,
    private val eldersInfoRepository: EldersInfoRepository,
) : ViewModel() {
    // 어르신 정보 화면

    private val _elderUiState = MutableStateFlow(LoginElderUiState())
    val elderUiState: StateFlow<LoginElderUiState> = _elderUiState.asStateFlow()

    private val _elderHealthUiState = MutableStateFlow(LoginElderHealthUiState())
    val elderHealthUiState: StateFlow<LoginElderHealthUiState> = _elderHealthUiState.asStateFlow()

    fun updateElderName(name: String) {
        _elderUiState.update { state ->
            state.copy(
                eldersList = state.eldersList.mapIndexed { index, elder ->
                    if (index == state.selectedIndex) elder.copy(name = name) else elder
                },
            )
        }
    }

    fun updateElderBirthDate(birthDate: String) {
        _elderUiState.update { state ->
            state.copy(
                eldersList = state.eldersList.mapIndexed { index, elder ->
                    if (index == state.selectedIndex) elder.copy(birthDate = birthDate) else elder
                },
            )
        }
    }

    fun updateElderGender(gender: Boolean) {
        _elderUiState.update { state ->
            state.copy(
                eldersList = state.eldersList.mapIndexed { index, elder ->
                    if (index == state.selectedIndex) elder.copy(gender = gender) else elder
                },
            )
        }
    }

    fun updateElderPhoneNumber(phoneNumber: String) {
        _elderUiState.update { state ->
            state.copy(
                eldersList = state.eldersList.mapIndexed { index, elder ->
                    if (index == state.selectedIndex) elder.copy(phoneNumber = phoneNumber) else elder
                },
            )
        }
    }

    fun updateElderRelationship(relationship: String) {
        _elderUiState.update { state ->
            state.copy(
                eldersList = state.eldersList.mapIndexed { index, elder ->
                    if (index == state.selectedIndex) elder.copy(relationship = relationship) else elder
                },
            )
        }
    }

    fun updateElderLivingType(livingType: String) {
        _elderUiState.update { state ->
            state.copy(
                eldersList = state.eldersList.mapIndexed { index, elder ->
                    if (index == state.selectedIndex) elder.copy(livingType = livingType) else elder
                },
            )
        }
    }

    fun selectElder(index: Int) {
        _elderUiState.update { state ->
            state.copy(selectedIndex = index)
        }
    }

    fun addElder() {
        _elderUiState.update { state ->
            state.copy(
                eldersList = state.eldersList + ElderData(),
                selectedIndex = state.selectedIndex + 1,
            )
        }
    }

    fun removeElder(index: Int) {
        _elderUiState.update { state ->
            state.copy(
                eldersList = state.eldersList.filterIndexed { i, _ -> i != index },
            )
        }
    }

    fun isInputComplete(): Boolean {
        return elderUiState.value.eldersList.all {
            it.name.isNotBlank() && it.birthDate.length == 8 && it.phoneNumber.length == 11 && it.relationship.isNotBlank() && it.livingType.isNotBlank()
        }
    }

    // suggestion: 어르신 등록과 건강정보 등록이 아예 분리된 만큼,
    // 추후 viewModel 별개로 가지고, 이름과 id값만 내비게이션으로 넘기는 게 나을 것 같습니다.

    // ------------------건강정보 관련 함수------------------

    // 상기한 방법으로 변경할 시 함수 변경 필요
    fun initElderHealthData() {
        _elderHealthUiState.update { state ->
            state.copy(elderHealthList = List(elderUiState.value.eldersList.size) { ElderHealthData() })
        }
    }

    fun selectElderInHealth(index: Int) {
        _elderHealthUiState.update { state ->
            state.copy(selectedIndex = index)
        }
    }

    fun updateDiseasesText(text: String) {
        _elderHealthUiState.update { state ->
            state.copy(diseaseInputText = text)
        }
    }

    fun updateMedicationText(text: String) {
        _elderHealthUiState.update { state ->
            state.copy(medicationInputText = text)
        }
    }

    fun addDisease(disease: String) {
        _elderHealthUiState.update { state ->
            state.copy(
                elderHealthList = state.elderHealthList.mapIndexed { index, elder ->
                    if (index == state.selectedIndex && disease !in elder.diseaseNames) {
                        elder.copy(diseaseNames = elder.diseaseNames + disease)
                    } else {
                        elder
                    }
                },
            )
        }
    }

    fun removeDisease(disease: String) {
        _elderHealthUiState.update { state ->
            state.copy(
                elderHealthList = state.elderHealthList.mapIndexed { index, elder ->
                    if (index == state.selectedIndex) {
                        elder.copy(diseaseNames = elder.diseaseNames.filter { it != disease })
                    } else {
                        elder
                    }
                },
            )
        }
    }

    fun addHealthNote(note: String) {
        _elderHealthUiState.update { state ->
            state.copy(
                elderHealthList = state.elderHealthList.mapIndexed { index, elder ->
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
        _elderHealthUiState.update { state ->
            state.copy(
                elderHealthList = state.elderHealthList.mapIndexed { index, elder ->
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

        _elderHealthUiState.update { state ->
            state.copy(
                elderHealthList = state.elderHealthList.mapIndexed { index, elder ->
                    if (index == state.selectedIndex) {
                        val currentList = elder.medicationMap[time] ?: emptyList()

                        val updatedMap = elder.medicationMap + if (medicine !in (elder.medicationMap[time]
                                ?: emptyList())
                        ) (time to (currentList + medicine)) else return
                        elder.copy(medicationMap = updatedMap)
                    } else {
                        elder
                    }
                },
            )
        }
    }

    fun removeMedication(time: MedicationTimeType, medicine: String) {
        _elderHealthUiState.update { state ->
            state.copy(
                elderHealthList = state.elderHealthList.mapIndexed { index, elder ->
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
        _elderHealthUiState.update { state ->
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
            elderRegisterRepository.postElderBulk(elderUiState.value.eldersList)
                .onSuccess { response ->
                    _elderUiState.update { state ->
                        state.copy(
                            eldersList = state.eldersList.mapIndexed { index, elderData ->
                                elderData.copy(id = response[index].id)
                            },
                        )
                    }
                    _elderHealthUiState.update { state ->
                        state.copy(
                            elderHealthList = state.elderHealthList.mapIndexed { index, healthData ->
                                healthData.copy(id = response[index].id)
                            },
                        )
                    }
                }
                .onFailure { exception ->
                    when (exception) {
                        is HttpException -> {
                            Log.e("httplog", "어르신 일괄등록 실패: ${exception.code()}, ${exception.message()}")
                        }
                    }
                }
        }
    }

    suspend fun postElderHealthInfoBulk() {
        elderRegisterRepository.postElderHealthInfoBulk(elderHealthUiState.value.elderHealthList)
            .onSuccess {
                Log.d("elderHealthRegister", "Success")
            }
            .onFailure { exception ->
                when (exception) {
                    is HttpException -> {
                        Log.e("elderHealthRegister", "어르신 건강정보 일괄등록 실패: ${exception.code()}, ${exception.message()}")
                    }
                }
            }
    }

    fun updateAllElders() { // getElderIds.isNotEmpty == true
        viewModelScope.launch {
            val elderIds = elderIdRepository.getElderIds()
            elderIds.filterIndexed { index, data ->
                data.values.first() == elderUiState.value.eldersList[index].id
            }.forEachIndexed { index, data ->
                eldersInfoRepository.updateElder(
                    data.values.first(),
                    elderUiState.value.eldersList[index],
                ).onSuccess {
                    Log.d("httplog", "어르신 재등록(수정) 성공")
                }.onFailure { exception ->
                    Log.e("httplog", "어르신 정보 등록 실패: ${exception.message}")
                }
            }
        }
    }

    fun updateAllEldersHealthInfo() {
        viewModelScope.launch {
            val elderIds = elderIdRepository.getElderIds()
            elderIds.filterIndexed { index, data ->
                data.values.first() == elderHealthUiState.value.elderHealthList[index].id
            }.forEachIndexed { index, data ->
                runCatching {
                    elderRegisterRepository.postElderHealthInfo(
                        data.values.first(),
                        elderHealthUiState.value.elderHealthList[index],
                    )
                }.onSuccess {
                    Log.d("httplog", "어르신 건강정보 재등록(수정) 성공")
                }
            }
        }
    }
}
