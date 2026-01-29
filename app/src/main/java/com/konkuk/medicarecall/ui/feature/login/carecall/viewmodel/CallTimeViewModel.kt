package com.konkuk.medicarecall.ui.feature.login.carecall.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.repository.ElderIdRepository
import com.konkuk.medicarecall.data.repository.SetCallRepository
import com.konkuk.medicarecall.ui.model.CallTimes
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CallTimeViewModel(
    private val setCallRepository: SetCallRepository,
    private val elderIdRepository: ElderIdRepository,
) : ViewModel() {
    val timeMap = mutableStateMapOf<Int, CallTimes>()
    val isLoading = mutableStateOf(false)
    val error = mutableStateOf<Throwable?>(null)
    private val _elderIdMap = MutableStateFlow(emptyMap<Int, String>())
    val elderIdMap = _elderIdMap.asStateFlow();

    private val _showBottomSheet = mutableStateOf(false)
    private val _selectedIndex = mutableIntStateOf(0)
    private val _selectedTabIndex = mutableIntStateOf(0)




    init {
        viewModelScope.launch {
            _elderIdMap.update { elderIdRepository.getElderIds().first(); }
        }
    }

    // Flow -> State 로 뱐환해서 보관
    private val _elderIds = mutableStateOf<Map<Int, String>>(emptyMap())
    val elderIds get() = _elderIds.value // UI에서 접근할 값

    init {
        observeElderIds()
    }

    // suspend + Flow 안전하게 처리하는 함수
    private fun observeElderIds() {
        viewModelScope.launch {
            try {
                elderIdRepository.getElderIds()
                    .collect { result ->
                        _elderIds.value = result
                        Log.d("CallTimeViewModel", "elderIds 업데이트: $result")
                    }
            } catch (e: Exception) {
                Log.e("CallTimeViewModel", "elderIds 수집 실패", e)
                error.value = e
            }
        }
    }

    fun setTimes(id: Int, times: CallTimes) {
        timeMap.put(id, times)
    }

    fun isCompleteFor(id: Int): Boolean {
        val t = timeMap[id] ?: return false
        return t.first != null && t.second != null && t.third != null
    }

    fun isAllComplete(ids: Set<Int>): Boolean =
        ids.isNotEmpty() && ids.all { isCompleteFor(it) }

    fun submitAllByIds(
        elderIds: List<Int>,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        viewModelScope.launch {
            isLoading.value = true
            error.value = null
            try {
                require(elderIds.isNotEmpty()) { "어르신 목록이 비어 있습니다." }

                // 병렬 요청 생성
                val jobs = elderIds.map { id ->
                    val times = timeMap[id] ?: error("'$id'의 시간이 비어있습니다.")
                    async {
                        setCallRepository.saveForElder(id, times).getOrThrow()
                        Log.d("CallTimeViewModel", "Saved call times for id:$id")
                    }
                }

                // 모든 요청이 끝날 때까지 대기
                jobs.awaitAll()
                onSuccess()

            } catch (t: Throwable) {
                Log.e("CallTimeViewModel", "submitAllByName failed", t)
                error.value = t
                onError(t)
            } finally {
                isLoading.value = false
            }
        }
    }

    // 에러 상태 초기화
    fun clearError() {
        error.value = null
    }

    fun setShowBottomSheet(value: Boolean) {
        _showBottomSheet.value = value
    }

    fun setSelectedIndex(index: Int) {
        _selectedIndex.intValue = index
    }

    fun setSelectedTabIndex(index: Int) {
        _selectedTabIndex.intValue = index
    }
}
