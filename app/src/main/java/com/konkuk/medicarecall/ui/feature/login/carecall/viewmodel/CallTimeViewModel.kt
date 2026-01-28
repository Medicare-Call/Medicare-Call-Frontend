package com.konkuk.medicarecall.ui.feature.login.carecall.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.repository.SetCallRepository
import com.konkuk.medicarecall.ui.model.CallTimes
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CallTimeViewModel(
    private val setCallRepo: SetCallRepository,
) : ViewModel() {

    // Time data
    private val _timeMap = MutableStateFlow<Map<Int, CallTimes>>(emptyMap())
    val timeMap: StateFlow<Map<Int, CallTimes>> = _timeMap.asStateFlow()

    // UI state
    private val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet: StateFlow<Boolean> = _showBottomSheet.asStateFlow()

    private val _selectedIndex = MutableStateFlow(0)
    val selectedIndex: StateFlow<Int> = _selectedIndex.asStateFlow()

    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex.asStateFlow()

    // Async state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _lastError = MutableStateFlow<Throwable?>(null)
    val lastError: StateFlow<Throwable?> = _lastError.asStateFlow()

    fun setTimes(id: Int, times: CallTimes) {
        _timeMap.update { currentMap ->
            currentMap.toMutableMap().apply { put(id, times) }
        }
    }

    fun isCompleteFor(id: Int): Boolean {
        val t = _timeMap.value[id] ?: return false
        return t.first != null && t.second != null && t.third != null
    }

    fun isAllComplete(ids: List<Int>): Boolean =
        ids.isNotEmpty() && ids.all { isCompleteFor(it) }

    fun submitAllByIds(
        elderIds: List<Int>,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _lastError.value = null
            try {
                require(elderIds.isNotEmpty()) { "어르신 목록이 비어 있습니다." }

                // 병렬 요청 생성
                val jobs = elderIds.map { id ->
                    val times = _timeMap.value[id] ?: error("'$id'의 시간이 비어있습니다.")
                    async {
                        setCallRepo.saveForElder(id, times).getOrThrow()
                        Log.d("CallTimeViewModel", "Saved call times for id:$id")
                    }
                }

                // 모든 요청이 끝날 때까지 대기
                jobs.awaitAll()
                onSuccess()

            } catch (t: Throwable) {
                // 코루틴 취소 예외는 재전파
                if (t is CancellationException) throw t

                Log.e("CallTimeViewModel", "전체 저장 실패", t)
                _lastError.value = t
                onError(t)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 에러 상태 초기화
    fun clearError() {
        _lastError.value = null
    }

    fun setShowBottomSheet(value: Boolean) {
        _showBottomSheet.value = value
    }

    fun setSelectedIndex(index: Int) {
        _selectedIndex.value = index
    }

    fun setSelectedTabIndex(index: Int) {
        _selectedTabIndex.value = index
    }
}
