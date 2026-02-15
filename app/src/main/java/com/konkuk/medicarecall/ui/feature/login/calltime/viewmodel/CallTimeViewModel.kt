package com.konkuk.medicarecall.ui.feature.login.calltime.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.repository.ElderIdRepository
import com.konkuk.medicarecall.data.repository.SetCallRepository
import com.konkuk.medicarecall.ui.model.CallTimes
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CallTimeViewModel(
    private val setCallRepository: SetCallRepository,
    private val elderIdRepository: ElderIdRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CallTimeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeElderIds()
    }

    // suspend + Flow 안전하게 처리하는 함수
    private fun observeElderIds() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(elderMap = elderIdRepository.getElderIds()) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e) }
            }
        }
    }

    fun setTimes(id: Long, times: CallTimes) {
        _uiState.update { it.copy(timeMap = it.timeMap.plus(id to times)) }
    }

    fun isCompleteFor(id: Long): Boolean {
        val t = uiState.value.timeMap[id] ?: return false
        return t.first != null && t.second != null && t.third != null
    }

    fun isAllComplete(ids: Set<Long>): Boolean =
        ids.isNotEmpty() && ids.all { isCompleteFor(it) }

    fun submitAllByIds(
        elderIds: List<Long>,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            _uiState.update { it.copy(error = null) }
            try {
                require(elderIds.isNotEmpty()) { "어르신 목록이 비어 있습니다." }

                // 병렬 요청 생성
                val jobs = elderIds.map { id ->
                    val times = uiState.value.timeMap[id] ?: error("'$id'의 시간이 비어있습니다.")
                    async {
                        setCallRepository.saveForElder(id, times).getOrThrow()
                    }
                }

                // 모든 요청이 끝날 때까지 대기
                jobs.awaitAll()
                onSuccess()
            } catch (t: Throwable) {
                _uiState.update { it.copy(error = t) }
                onError(t)
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun setShowBottomSheet(value: Boolean) {
        _uiState.update { it.copy(showBottomSheet = value) }
    }

    fun setSelectedIndex(index: Int) {
        _uiState.update { it.copy(selectedIndex = index) }
    }

    fun setSelectedTabIndex(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index) }
    }
}
