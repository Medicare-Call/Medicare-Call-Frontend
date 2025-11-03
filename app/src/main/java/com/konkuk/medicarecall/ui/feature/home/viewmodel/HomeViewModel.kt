package com.konkuk.medicarecall.ui.feature.home.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.repository.EldersHealthInfoRepository
import com.konkuk.medicarecall.data.repository.EldersInfoRepository
import com.konkuk.medicarecall.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.time.LocalDate
import javax.inject.Inject

data class ElderInfo(val id: Int, val name: String, val phone: String?)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val eldersInfoRepository: EldersInfoRepository,
    private val homeRepository: HomeRepository,
    private val savedStateHandle: SavedStateHandle,
    private val eldersHealthInfoRepository: EldersHealthInfoRepository,
) : ViewModel() {
    fun overrideName(newName: String) {
        val id = selectedElderId.value ?: return

        _elderInfoList.value = elderInfoList.value.map {
            if (it.id == id) it.copy(name = newName) else it
        }
        _homeUiState.value = _homeUiState.value.copy(elderName = newName)

        _homeUiState.update { it.copy(isLoading = false) }
        softRefreshCurrentElder()
    }

    var isLoading by mutableStateOf(true)

    fun callImmediate(
        careCallTimeOption: String,
    ) {
        viewModelScope.launch {
            homeRepository.requestImmediateCareCall(
                elderId = selectedElderId.value!!,
                careCallOption = careCallTimeOption,
            )
        }
    }

    private companion object {
        const val TAG = "HomeViewModel"
        const val KEY_SELECTED_ELDER_ID = "selectedElderId"
    }

    // 홈 화면 상태 (isLoading 포함)
    private val _homeUiState = MutableStateFlow(HomeUiState.Companion.EMPTY)
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    // 어르신 전체 목록
    private val _elderInfoList = MutableStateFlow<List<ElderInfo>>(emptyList())
    val elderInfoList: StateFlow<List<ElderInfo>> = _elderInfoList.asStateFlow()

//    드롭다운에 표시할 어르신 이름 목록
//    val elderNameList: StateFlow<List<String>> = _elderInfoList.mapState { list ->
//        list.map { it.name }
//    }

    // 현재 선택된 어르신 ID
    private val _selectedElderId = MutableStateFlow<Int?>(
        savedStateHandle.get<Int?>(KEY_SELECTED_ELDER_ID),
    )
    val selectedElderId: StateFlow<Int?> = _selectedElderId.asStateFlow()

    init {
        fetchElderList()

        // 선택된 ID가 바뀌면 해당 어르신의 홈 데이터를 불러옴 + 저장소에 저장
        viewModelScope.launch {
            _selectedElderId.collect { elderId ->
                if (elderId != null) {
                    savedStateHandle[KEY_SELECTED_ELDER_ID] = elderId
                    fetchHomeSummaryForToday(elderId)
                } else {
                    _homeUiState.value = HomeUiState.Companion.EMPTY.copy(isLoading = false)
                }
            }
        }
    }

    // 케어콜 및 화면 재진입 시 데이터 갱신
    fun forceRefreshHomeData(onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            try {
                eldersHealthInfoRepository.refresh()
                fetchElderList() // 이름/리스트 서버와 동기화
                _selectedElderId.value?.let { fetchHomeSummaryForToday(it) }
            } finally {
                onComplete?.invoke()
            }
        }
    }

    // 서버에서 어르신 전체 목록을 불러옴
    fun fetchElderList() {
        viewModelScope.launch {
            if (_elderInfoList.value.isEmpty()) {
                _homeUiState.update { it.copy(isLoading = true) }
            }
            eldersInfoRepository.getElders()
                .onSuccess { elders ->
                    _elderInfoList.value = elders.map {
                        ElderInfo(id = it.elderId, name = it.name, phone = it.phone)
                    }
                    val restoredId = savedStateHandle.get<Int?>(KEY_SELECTED_ELDER_ID)
                    if (restoredId != null && _elderInfoList.value.any { it.id == restoredId }) {
                        _selectedElderId.value = restoredId
                    } else if (_selectedElderId.value == null && _elderInfoList.value.isNotEmpty()) {
                        _selectedElderId.value = _elderInfoList.value.first().id
                    }
                }
                .onFailure { error ->
                    Log.e(TAG, "어르신 목록 로딩 실패", error)
                    _homeUiState.update { it.copy(isLoading = false) }
                }
        }
    }

    /**
     * 특정 어르신 ID를 받아서 홈 화면 데이터를 서버에 요청합니다.
     */
    private fun fetchHomeSummaryForToday(elderId: Int) {
        viewModelScope.launch {
            _homeUiState.update { it.copy(isLoading = false) }
            val today = LocalDate.now()
            try {
                // ① 요약 API 호출
                val uiFromServer = homeRepository.getHomeUiState(elderId, today)

                // ② 설정/건강정보 최신화(중요)
                eldersHealthInfoRepository.refresh()
                val healthInfo = eldersHealthInfoRepository.getEldersHealthInfo()
                    .getOrNull()
                    ?.firstOrNull { it.elderId == elderId }

                val correctName = _elderInfoList.value.find { it.id == elderId }?.name
                    ?: uiFromServer.elderName

                // 정렬 기준(설정에 등록된 전체 약 순서)
                val correctMedicationOrder = healthInfo?.medications
                    ?.flatMap { it.value }
                    ?.distinct()
                    ?: emptyList()

                // ③ 요약 API에서 약이 없으면 설정 복약으로 대체
                val fallbackMedicines = healthInfo?.medications
                    ?.flatMap { (time, medNames) -> medNames.map { medName -> medName to time } }
                    ?.groupBy { it.first }
                    ?.map { (medName, group) ->
                        MedicineUiState(
                            medicineName = medName,
                            todayTakenCount = 0, // 요약이 없으니 기본 0
                            todayRequiredCount = group.size, // 같은 약이 여러 복용시간이면 개수 = 요구횟수
                            nextDoseTime = "-", // 시간표시 필요없으면 "-" 유지
                        )
                    }
                    ?: emptyList()

                val mergedMedicines = when {
                    uiFromServer.medicines.isNotEmpty() -> uiFromServer.medicines
                    else -> fallbackMedicines
                }.sortedBy { med ->
                    correctMedicationOrder.indexOf(med.medicineName)
                        .let { if (it == -1) Int.MAX_VALUE else it }
                }

                _homeUiState.value = uiFromServer.copy(
                    elderName = correctName,
                    medicines = mergedMedicines,
                    isLoading = false,
                )
            } catch (e: Exception) {
                // 기존 로직 유지(404면 완전 폴백)
                if (e is HttpException && e.code() == 404) {
                    val fallbackUiState = createFallbackHomeUiState(elderId)
                    _homeUiState.value = fallbackUiState.copy(isLoading = false)
                } else {
                    Log.e(TAG, "getHomeSummary failed elderId=$elderId", e)
                    _homeUiState.value = HomeUiState.Companion.EMPTY.copy(isLoading = false)
                }
            } finally {
                _homeUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private suspend fun createFallbackHomeUiState(elderId: Int): HomeUiState {
        val healthInfo = eldersHealthInfoRepository.getEldersHealthInfo()
            .getOrNull()
            ?.firstOrNull { it.elderId == elderId }
        val elderName =
            healthInfo?.name ?: _elderInfoList.value.find { it.id == elderId }?.name ?: ""
        val fallbackMedicines = healthInfo?.medications
            ?.flatMap { (time, medNames) ->
                medNames.map { medName -> medName to time }
            }
            ?.groupBy { it.first }
            ?.map { (medName, group) ->
                MedicineUiState(
                    medicineName = medName,
                    todayTakenCount = 0,
                    todayRequiredCount = group.size,
                    nextDoseTime = "-",
                )
            } ?: emptyList()
        val correctMedicationOrder = healthInfo?.medications
            ?.flatMap { it.value }
            ?.distinct() ?: emptyList()
        val sortedFallbackMedicines = fallbackMedicines.sortedBy { medUiState ->
            correctMedicationOrder.indexOf(medUiState.medicineName)
                .let { if (it == -1) Int.MAX_VALUE else it }
        }
        return HomeUiState.Companion.EMPTY.copy(
            elderName = elderName,
            medicines = sortedFallbackMedicines,
        )
    }

    // 드롭다운에서 어르신을 선택했을 때 호출
    fun selectElder(name: String) {
        if (_homeUiState.value.isLoading) return

        val id: Int = elderIdByName.value[name] ?: return
        if (_selectedElderId.value != id) {
            _selectedElderId.value = id
        }
    }

    // 드롭다운 표시용 이름 리스트
    val elderNameList: StateFlow<List<String>> = _elderInfoList.mapState(viewModelScope) { list ->
        list.map { it.name }
    }

    // 이름 → ID 매핑
    private val elderIdByName: StateFlow<Map<String, Int>> =
        _elderInfoList.mapState(viewModelScope) { list ->
            list.associate { it.name to it.id }
        }

    private fun softRefreshCurrentElder(timeoutMs: Long = 1200L) {
        val id = selectedElderId.value ?: return
        viewModelScope.launch {
            val keepName = _homeUiState.value.elderName

            runCatching {
                withTimeout(timeoutMs) {
                    val today = java.time.LocalDate.now()
                    val uiFromServer = homeRepository.getHomeUiState(id, today)

                    val healthInfo = runCatching {
                        eldersHealthInfoRepository.refresh()
                        eldersHealthInfoRepository.getEldersHealthInfo().getOrNull()
                            ?.firstOrNull { it.elderId == id }
                    }.getOrNull()

                    val correctMedicationOrder = healthInfo?.medications
                        ?.flatMap { it.value }
                        ?.distinct().orEmpty()

                    val mergedMedicines = if (uiFromServer.medicines.isNotEmpty()) {
                        uiFromServer.medicines
                    } else {
                        healthInfo?.medications
                            ?.flatMap { (time, meds) -> meds.map { it to time } }
                            ?.groupBy { it.first }
                            ?.map { (name, group) ->
                                MedicineUiState(
                                    medicineName = name,
                                    todayTakenCount = 0,
                                    todayRequiredCount = group.size,
                                    nextDoseTime = "-",
                                )
                            }.orEmpty()
                    }.sortedBy { med ->
                        correctMedicationOrder.indexOf(med.medicineName)
                            .let { if (it == -1) Int.MAX_VALUE else it }
                    }

                    _homeUiState.value = uiFromServer.copy(
                        elderName = keepName,
                        medicines = mergedMedicines,
                        isLoading = false,
                    )
                }
            }.onFailure {
                _homeUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    // StateFlow 변환용 확장 함수
    fun <T, R> StateFlow<T>.mapState(
        scope: CoroutineScope,
        transform: (T) -> R,
    ): StateFlow<R> {
        return map(transform).stateIn(scope, SharingStarted.Eagerly, transform(value))
    }
}
