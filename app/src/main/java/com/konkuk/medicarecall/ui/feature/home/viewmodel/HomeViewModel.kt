package com.konkuk.medicarecall.ui.feature.home.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.mapper.HomeMapper
import com.konkuk.medicarecall.data.mapper.toUiState
import com.konkuk.medicarecall.data.repository.ElderIdRepository
import com.konkuk.medicarecall.data.repository.EldersHealthInfoRepository
import com.konkuk.medicarecall.data.repository.HomeRepository
import com.konkuk.medicarecall.domain.model.ElderInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val homeRepository: HomeRepository,
    private val savedStateHandle: SavedStateHandle,
    private val eldersHealthInfoRepository: EldersHealthInfoRepository,
    private val elderIdRepository: ElderIdRepository,
) : ViewModel() {

    // 이름 업데이트 수신
    private val _updatedName: StateFlow<String?> =
        savedStateHandle.getStateFlow("ELDER_NAME_UPDATED", null)

    val updatedName: StateFlow<String?> = _updatedName

    fun clearUpdatedName() {
        savedStateHandle.remove<String>("ELDER_NAME_UPDATED")
    }

    fun overrideName(newName: String) {
        val id = selectedElderId.value
        if (id == -1L) return

        _elderInfoList.value = elderInfoList.value.map {
            if (it.elderId == id) it.copy(name = newName) else it
        }
        _homeUiState.value = _homeUiState.value.copy(elderName = newName)

        _homeUiState.update { it.copy(isLoading = false) }
    }

    fun callImmediate(
        careCallTimeOption: String,
    ) {
        viewModelScope.launch {
            homeRepository.requestImmediateCareCall(
                elderId = selectedElderId.value,
                careCallOption = careCallTimeOption,
            )
        }
    }

    private companion object {
        const val TAG = "HomeViewModel"
        const val KEY_SELECTED_ELDER_ID = "selectedElderId"
    }

    // 홈 화면 상태 (isLoading 포함)
    private val _homeUiState = MutableStateFlow(HomeUiState.EMPTY)
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    // 어르신 전체 목록
    private val _elderInfoList = MutableStateFlow<List<ElderInfo>>(emptyList())
    val elderInfoList: StateFlow<List<ElderInfo>> = _elderInfoList.asStateFlow()

    // 현재 선택된 어르신 ID
    private val _selectedElderId = MutableStateFlow(
        savedStateHandle.get<Long>(KEY_SELECTED_ELDER_ID) ?: -1L,
    )
    val selectedElderId: StateFlow<Long> = _selectedElderId.asStateFlow()

    init {
        fetchElderList()

        // 선택된 ID가 바뀌면 해당 어르신의 홈 데이터를 불러옴 + 저장소에 저장
        viewModelScope.launch {
            _selectedElderId.collect { elderId ->
                if (elderId != -1L) {
                    savedStateHandle[KEY_SELECTED_ELDER_ID] = elderId
                    fetchHomeSummaryForToday(elderId)
                } else {
                    _homeUiState.value = HomeUiState.EMPTY.copy(isLoading = false)
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
                if (_selectedElderId.value != -1L) {
                    fetchHomeSummaryForToday(_selectedElderId.value)
                }
            } finally {
                onComplete?.invoke()
            }
        }
    }

    // 로컬에서 어르신 전체 목록을 불러옴
    fun fetchElderList() {
        viewModelScope.launch {
            if (_elderInfoList.value.isEmpty()) {
                _homeUiState.update { it.copy(isLoading = true) }
            }
            val elderIdMap = elderIdRepository.getElderIds()
            _elderInfoList.value = elderIdMap.map {
                ElderInfo(elderId = it.key, name = it.value)
            }
            val restoredId = savedStateHandle.get<Long>(KEY_SELECTED_ELDER_ID) ?: -1L
            if (restoredId != -1L && _elderInfoList.value.any { it.elderId == restoredId }) {
                _selectedElderId.value = restoredId
            } else if (_selectedElderId.value == -1L && _elderInfoList.value.isNotEmpty()) {
                _selectedElderId.value = _elderInfoList.value.first().elderId
            }
        }
    }

    /**
     * 특정 어르신 ID를 받아서 홈 화면 데이터를 서버에 요청합니다.
     */
    private fun fetchHomeSummaryForToday(elderId: Long) {
        viewModelScope.launch {
            _homeUiState.update { it.copy(isLoading = true) }

            homeRepository.getHomeSummary(elderId)
                .onSuccess { home ->
                    // 설정/건강정보 최신화
                    eldersHealthInfoRepository.refresh()
                    val healthInfo = eldersHealthInfoRepository
                        .getEldersHealthInfo()
                        .getOrNull()
                        ?.firstOrNull { it.elderId == elderId }

                    // 로컬 캐시의 이름 우선 사용
                    val elderName = _elderInfoList.value
                        .find { it.elderId == elderId }?.name
                        ?: home.elderName

                    val uiState = home.toUiState(
                        healthInfo = healthInfo,
                        elderName = elderName,
                    )

                    _homeUiState.value = uiState
                }
                .onFailure { error ->
                    handleHomeSummaryError(elderId)
                }
        }
    }

    /**
     * 홈 요약 로딩 실패 시 fallback 상태 생성
     */
    private suspend fun handleHomeSummaryError(elderId: Long) {
        val healthInfo = eldersHealthInfoRepository
            .getEldersHealthInfo()
            .getOrNull()
            ?.firstOrNull { it.elderId == elderId }

        val elderName = healthInfo?.name
            ?: _elderInfoList.value.find { it.elderId == elderId }?.name
            ?: ""

        // Mapper에게 fallback 생성 위임
        val fallbackState = HomeMapper.fromHealthInfo(
            healthInfo = healthInfo,
            elderName = elderName,
        )

        _homeUiState.value = fallbackState
    }

    // 드롭다운에서 어르신을 선택했을 때 호출
    fun selectElder(name: String) {
        if (_homeUiState.value.isLoading) return

        val id = elderIdByName[name] ?: return

        if (_selectedElderId.value != id) {
            _selectedElderId.value = id
        }
    }

    // 드롭다운 표시용 이름 리스트
    val elderNameList: StateFlow<List<String>> = _elderInfoList
        .map { list -> list.map { it.name } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 이름 → ID 매핑
    private val elderIdByName: Map<String, Long>
        get() = _elderInfoList.value.associate { it.name to it.elderId }
}
