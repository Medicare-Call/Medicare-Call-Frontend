package com.konkuk.medicarecall.ui.feature.homedetail.statehealth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.repository.HealthRepository
import com.konkuk.medicarecall.domain.util.now
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.koin.android.annotation.KoinViewModel

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

    // 건강 상태
    private companion object {
        const val TAG = "HEALTH_API"
    }

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _health = MutableStateFlow(HealthUiState())
    val health: StateFlow<HealthUiState> = _health

    fun loadHealthDataForDate(elderId: Long, date: LocalDate) {
        viewModelScope.launch {
            if (!_isLoading.value) _isLoading.value = true
            val formatted = date.toString()
            Log.d(TAG, "Request elderId=$elderId, date=$formatted")

            healthRepository.getHealth(elderId, date)
                .onSuccess { health ->
                    _health.value = HealthUiState(health = health)
                    Log.d(TAG, "Success elderId=$elderId, date=$formatted")
                }
                .onFailure {
                    Log.d(TAG, "Failed elderId=$elderId, date=$formatted")
                    _health.value = HealthUiState()
                }
            _isLoading.value = false
        }
    }
}
