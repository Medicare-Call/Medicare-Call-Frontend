package com.konkuk.medicarecall.ui.feature.homedetail.sleep.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.konkuk.medicarecall.data.repository.SleepRepository
import com.konkuk.medicarecall.ui.navigation.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.time.LocalDate

@KoinViewModel
class SleepViewModel(
    private val sleepRepository: SleepRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SleepUiState())
    val uiState: StateFlow<SleepUiState> = _uiState.asStateFlow()

    private val elderId = savedStateHandle.toRoute<Route.SleepDetail>().elderId
    fun loadSleepDataForDate(date: LocalDate) {
        viewModelScope.launch {
            sleepRepository.getSleepData(
                elderId = elderId,
                date = date,
            ).onSuccess { data ->
                _uiState.update {
                    SleepUiState(
                        date = data.date,
                        totalSleepHours = data.totalSleepHours,
                        totalSleepMinutes = data.totalSleepMinutes,
                        bedTime = data.bedTime,
                        wakeUpTime = data.wakeUpTime,
                    )
                }
            }.onFailure { error ->
                Log.e("SleepViewModel", "Error loading sleep data", error)
            }
        }
    }
}
