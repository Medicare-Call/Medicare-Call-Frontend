package com.konkuk.medicarecall.ui.feature.homedetail.glucoselevel.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.repository.GlucoseRepository
import com.konkuk.medicarecall.ui.model.GlucoseTiming
import com.konkuk.medicarecall.ui.model.GraphDataPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class GlucoseViewModel @Inject constructor(
    private val glucoseRepository: GlucoseRepository,
) : ViewModel() {

    private companion object {
        const val TAG = "GLUCOSE_VM"
    }

    // UI State
    private val _uiState = MutableStateFlow(GlucoseUiState())
    val uiState: StateFlow<GlucoseUiState> = _uiState.asStateFlow()

    // 내부 캐시
    private var beforeMealData = mutableStateListOf<GraphDataPoint>()
    private var afterMealData = mutableStateListOf<GraphDataPoint>()

    fun getGlucoseData(
        elderId: Int,
        counter: Int,
        type: GlucoseTiming,
        isRefresh: Boolean = false,
    ) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            glucoseRepository.getGlucoseGraph(elderId, counter, type.toString())
                .onSuccess { response ->
                    val processedData = response.data.reversed()

                    val newData = processedData.map { record ->
                        GraphDataPoint(
                            date = LocalDate.parse(record.date),
                            value = record.value.toFloat(),
                        )
                    }

                    val updatedDataList = when (type) {
                        GlucoseTiming.BEFORE_MEAL -> {
                            if (isRefresh) beforeMealData.clear()
                            beforeMealData.addAll(0, newData)
                            beforeMealData
                        }

                        GlucoseTiming.AFTER_MEAL -> {
                            if (isRefresh) afterMealData.clear()
                            afterMealData.addAll(0, newData)
                            afterMealData
                        }
                    }

                    // 현재 선택된 타이밍과 일치하는 경우에만 UI를 업데이트
                    if (_uiState.value.selectedTiming == type) {
                        val newSelectedIndex = if (isRefresh) {
                            // 새로고침이면 마지막 인덱스
                            updatedDataList.lastIndex
                        } else {
                            // 페이지네이션이면 기존 선택 유지 (새 데이터가 앞에 추가되므로 인덱스 조정)
                            _uiState.value.selectedIndex + newData.size
                        }

                        _uiState.update {
                            it.copy(
                                graphDataPoints = updatedDataList,
                                selectedIndex = newSelectedIndex,
                                hasNext = response.hasNextPage,
                            )
                        }
                    }
                }
                .onFailure { error ->
                    Log.e(TAG, "getGlucoseData failed", error)
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    /** 타이밍 전환*/
    fun updateTiming(newTiming: GlucoseTiming) {
        Log.d(TAG, "updateTiming(newTiming=$newTiming)")
        val dataToShow =
            if (newTiming == GlucoseTiming.BEFORE_MEAL) beforeMealData else afterMealData
        _uiState.update {
            it.copy(
                graphDataPoints = dataToShow,
                selectedTiming = newTiming,
                hasNext = true, // 타이밍 전환 시에는 항상 다음 페이지가 있다고 가정
                selectedIndex = dataToShow.lastIndex.takeIf { i -> i >= 0 } ?: -1,
            )
        }
    }

    fun onClickDots(newIndex: Int) {
        _uiState.value = _uiState.value.copy(selectedIndex = newIndex)
    }
}
