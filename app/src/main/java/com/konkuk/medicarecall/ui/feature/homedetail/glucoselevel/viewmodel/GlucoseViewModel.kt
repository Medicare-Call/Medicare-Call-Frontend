package com.konkuk.medicarecall.ui.feature.homedetail.glucoselevel.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.mapper.toUiState
import com.konkuk.medicarecall.data.repository.GlucoseRepository
import com.konkuk.medicarecall.ui.model.GlucoseTiming
import com.konkuk.medicarecall.ui.model.GraphDataPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class GlucoseViewModel(
    private val glucoseRepository: GlucoseRepository,
) : ViewModel() {

    private companion object {
        const val TAG = "GLUCOSE_VM"
    }

    // UI State
    private val _uiState = MutableStateFlow(GlucoseUiState())
    val uiState: StateFlow<GlucoseUiState> = _uiState.asStateFlow()

    // 내부 캐시
    private val _beforeMealData = MutableStateFlow<List<GraphDataPoint>>(emptyList())
    private val _afterMealData = MutableStateFlow<List<GraphDataPoint>>(emptyList())

    fun getGlucoseData(
        elderId: Long,
        counter: Int,
        type: GlucoseTiming,
        isRefresh: Boolean = false,
    ) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            glucoseRepository
                .getGlucoseGraph(elderId, counter, type.name)
                .onSuccess { glucose ->

                    val newPoints = glucose.toUiState(
                        timing = type,
                    ).graphDataPoints

                    if (type == GlucoseTiming.BEFORE_MEAL) {
                        _beforeMealData.update { current ->
                            if (isRefresh) newPoints else current + newPoints
                        }
                    } else {
                        _afterMealData.update { current ->
                            if (isRefresh) newPoints else current + newPoints
                        }
                    }

                    if (_uiState.value.selectedTiming == type) {
                        val cache =
                            if (type == GlucoseTiming.BEFORE_MEAL)
                                _beforeMealData.value
                            else
                                _afterMealData.value

                        _uiState.update {
                            it.copy(
                                graphDataPoints = cache,
                                hasNext = glucose.hasNext,
                                isLoading = false,
                                selectedIndex =
                                if (isRefresh) cache.lastIndex
                                else it.selectedIndex,
                            )
                        }
                    } else {
                        _uiState.update { it.copy(isLoading = false) }
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }

    /** 타이밍 전환*/
    fun updateTiming(newTiming: GlucoseTiming) {
        val dataToShow =
            if (newTiming == GlucoseTiming.BEFORE_MEAL) _beforeMealData.value else _afterMealData.value
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
