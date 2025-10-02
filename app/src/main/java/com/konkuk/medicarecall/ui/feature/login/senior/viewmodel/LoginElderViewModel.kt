package com.konkuk.medicarecall.ui.feature.login.senior.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
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
import kotlinx.coroutines.launch
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
        val selectedIndex = _elderUiState.value.selectedIndex
        val updatedList = _elderUiState.value.eldersList.toMutableList()
        updatedList[selectedIndex] = updatedList[selectedIndex].copy(name = name)
        _elderUiState.value = _elderUiState.value.copy(eldersList = updatedList)
    }

    fun updateElderBirthDate(birthDate: String) {
        val selectedIndex = _elderUiState.value.selectedIndex
        val updatedList = _elderUiState.value.eldersList.toMutableList()
        updatedList[selectedIndex] = updatedList[selectedIndex].copy(birthDate = birthDate)
        _elderUiState.value = _elderUiState.value.copy(eldersList = updatedList)
    }

    fun updateElderGender(gender: Boolean) {
        val selectedIndex = _elderUiState.value.selectedIndex
        val updatedList = _elderUiState.value.eldersList.toMutableList()
        updatedList[selectedIndex] = updatedList[selectedIndex].copy(gender = gender)
        _elderUiState.value = _elderUiState.value.copy(eldersList = updatedList)
    }

    fun updateElderPhoneNumber(phoneNumber: String) {
        val selectedIndex = _elderUiState.value.selectedIndex
        val updatedList = _elderUiState.value.eldersList.toMutableList()
        updatedList[selectedIndex] = updatedList[selectedIndex].copy(phoneNumber = phoneNumber)
        _elderUiState.value = _elderUiState.value.copy(eldersList = updatedList)
    }

    fun updateElderRelationship(relationship: String) {
        val selectedIndex = _elderUiState.value.selectedIndex
        val updatedList = _elderUiState.value.eldersList.toMutableList()
        updatedList[selectedIndex] = updatedList[selectedIndex].copy(relationship = relationship)
        _elderUiState.value = _elderUiState.value.copy(eldersList = updatedList)
    }

    fun updateElderLivingType(livingType: String) {
        val selectedIndex = _elderUiState.value.selectedIndex
        val updatedList = _elderUiState.value.eldersList.toMutableList()
        updatedList[selectedIndex] = updatedList[selectedIndex].copy(livingType = livingType)
        _elderUiState.value = _elderUiState.value.copy(eldersList = updatedList)
    }

    fun selectElder(index: Int) {
        _elderUiState.value = _elderUiState.value.copy(
            selectedIndex = index,
        )
    }

    fun addElder() {
        val updatedList = _elderUiState.value.eldersList.toMutableList()
        updatedList.add(ElderData())
        _elderUiState.value = _elderUiState.value.copy(
            eldersList = updatedList,
            selectedIndex = _elderUiState.value.selectedIndex + 1,
        )
    }

    fun removeElder(index: Int) {
        val updatedList = _elderUiState.value.eldersList.toMutableList()
        updatedList.removeAt(index)
        _elderUiState.value = _elderUiState.value.copy(eldersList = updatedList)

    }

    fun isInputComplete(): Boolean {
        return elderUiState.value.eldersList.all {
            it.name.isNotBlank() &&
                it.birthDate.length == 8 &&
                it.phoneNumber.length == 11 &&
                it.relationship.isNotBlank() &&
                it.livingType.isNotBlank()
        }

    }

    // suggestion: 어르신 등록과 건강정보 등록이 아예 분리된 만큼,
    // 추후 viewModel 별개로 가지고, 이름과 id값만 내비게이션으로 넘기는 게 나을 듯.


    var diseaseInputText = mutableStateListOf<MutableState<String>>()
    var diseaseList = mutableStateListOf(mutableStateListOf<String>())

    var medMap = mutableStateListOf<SnapshotStateMap<MedicationTimeType, MutableList<String>>>(

    )
    var medInputText = mutableStateListOf<MutableState<String>>()
    var healthIssueList = mutableStateListOf(mutableStateListOf<String>())


    val elderHealthDataList = mutableStateListOf<ElderHealthData>()

    fun createElderHealthDataList() {

        elderHealthDataList.apply {
            repeat(elderUiState.value.eldersList.size) { index ->
                val currentId = getOrNull(index)?.id // 기존 id 보존

                val healthData = ElderHealthData(
                    diseaseNames = diseaseList[index],
                    medicationMap = medMap[index],
                    notes = healthIssueList[index],
                    id = currentId,
                )

                if (index < size) {
                    set(index, healthData) // 항상 덮어쓰기
                } else {
                    add(healthData)
                }
            }
        }
    }


    // ------------------API 요청------------------
    fun postElderAndHealth() {
        viewModelScope.launch {
            elderRegisterRepository.registerElderAndHealth(
                elders = elderUiState.value.eldersList.size,
                elderInfoList = elderUiState.value.eldersList,
                elderHealthInfo = elderHealthDataList,
            )
                .onSuccess {
                    Log.d("httplog", "어르신 및 건강정보 전부 등록 성공!")
                }
                .onFailure { exception ->
                    Log.e("httplog", "어르신 정보 or 건강정보 등록 실패: ${exception.message}")
                }

        }
    }

    fun updateAllElders() { // getElderIds.isNotEmpty == true
        viewModelScope.launch {
            val elderIds = elderIdRepository.getElderIds()
            elderIds.filterIndexed { index, it ->
                it.values.first() == elderUiState.value.eldersList[index].id
            }.forEachIndexed { index, it ->
                eldersInfoRepository.updateElder(
                    it.values.first(), elderUiState.value.eldersList[index],
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
            elderIds.filterIndexed { index, it ->
                it.values.first() == elderHealthDataList[index].id
            }.forEachIndexed { index, it ->
                runCatching {
                    elderRegisterRepository.postElderHealthInfo(
                        it.values.first(),
                        elderHealthDataList[index],
                    )
                }.onSuccess {
                    Log.d("httplog", "어르신 건강정보 재등록(수정) 성공")
                }
            }
        }
    }
}
