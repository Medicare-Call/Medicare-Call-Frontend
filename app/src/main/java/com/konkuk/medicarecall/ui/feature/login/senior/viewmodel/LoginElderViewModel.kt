package com.konkuk.medicarecall.ui.feature.login.senior.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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

    private val _selectedIndex = MutableStateFlow(0)
    val selectedIndex = _selectedIndex.asStateFlow()


    private val _elderDataList = MutableStateFlow<MutableList<ElderData>>()
    val elderDataList = mutableStateListOf(
        ElderData(),
    )


    fun isInputComplete(): Boolean {
        return elderDataList.all {
            it.name.isNotBlank() &&
                it.birthDate.length == 8 &&
                it.phoneNumber.length == 11 &&
                it.relationship.isNotBlank() &&
                it.livingType.isNotBlank()
        }

    }

    // 건강정보 화면
    var selectedElder by mutableIntStateOf(0)
        private set

    fun onSelectedElderChanged(new: Int) {
        selectedElder = new
    }

    var diseaseInputText = mutableStateListOf<MutableState<String>>()
    var diseaseList = mutableStateListOf(mutableStateListOf<String>())

    var medMap = mutableStateListOf<SnapshotStateMap<MedicationTimeType, MutableList<String>>>(

    )
    var medInputText = mutableStateListOf<MutableState<String>>()
    var healthIssueList = mutableStateListOf(mutableStateListOf<String>())


    val elderHealthDataList = mutableStateListOf<ElderHealthData>()

    fun createElderHealthDataList() {

        elderHealthDataList.apply {
            repeat(elderDataList.size) { index ->
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
                elders = elderDataList.size,
                elderInfoList = elderDataList,
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
                it.values.first() == elderDataList[index].id
            }.forEachIndexed { index, it ->
                eldersInfoRepository.updateElder(
                    it.values.first(), elderDataList[index],
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
