package com.konkuk.medicarecall.ui.feature.homedetail.sleep.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.konkuk.medicarecall.data.repository.SleepRepository
import com.konkuk.medicarecall.domain.util.now
import com.konkuk.medicarecall.ui.navigation.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SleepViewModel(
    private val sleepRepository: SleepRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    // 캘린더 상태
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate
    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun resetToToday() {
        _selectedDate.value = LocalDate.now()
    }

    // 수면 상태
    private companion object {
        const val TAG = "SLEEP_API"
    }

    private val _uiState = MutableStateFlow(SleepUiState())
    val uiState: StateFlow<SleepUiState> = _uiState.asStateFlow()

    private val elderId = savedStateHandle.toRoute<Route.SleepDetail>().elderId
    fun loadSleepDataForDate(date: LocalDate) {
        viewModelScope.launch {
            sleepRepository.getSleepData(
                elderId = elderId,
                date = date,
            ).onSuccess { sleep ->
                _uiState.update {
                    SleepUiState(sleep = sleep)
                }
            }.onFailure { error ->
            }
        }
    }
}
