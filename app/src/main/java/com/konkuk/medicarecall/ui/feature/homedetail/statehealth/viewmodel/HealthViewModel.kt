package com.konkuk.medicarecall.ui.feature.homedetail.statehealth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.repository.HealthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

@KoinViewModel
class HealthViewModel(
    private val healthRepository: HealthRepository,
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

    fun getCurrentWeekDates(): List<LocalDate> {
        val base = _selectedDate.value
        val startOfWeek =
            base.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
        return (0..6).map { startOfWeek.plusDays(it.toLong()) }
    }

    // 건강 상태

    private companion object {
        const val TAG = "HEALTH_API"
    }

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _health = MutableStateFlow(HealthUiState.Companion.EMPTY)
    val health: StateFlow<HealthUiState> = _health

    fun loadHealthDataForDate(elderId: Int, date: LocalDate) {
        viewModelScope.launch {
            if (!_isLoading.value) _isLoading.value = true
            val formatted = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
            Log.d(TAG, "Request elderId=$elderId, date=$formatted")

            healthRepository.getHealthUiState(elderId = elderId, date = date)
                .onSuccess {
                    _health.value = it
                    Log.d(TAG, "Success elderId=$elderId, date=$formatted")
                }
                .onFailure {
                    Log.d(TAG, "Failed elderId=$elderId, date=$formatted")
                    _health.value = HealthUiState.Companion.EMPTY
                }
            _isLoading.value = false
        }
    }
}
