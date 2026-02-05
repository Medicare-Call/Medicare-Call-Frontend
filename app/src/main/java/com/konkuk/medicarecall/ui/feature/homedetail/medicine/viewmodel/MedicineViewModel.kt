package com.konkuk.medicarecall.ui.feature.homedetail.medicine.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.mapper.toMedicineUiStates
import com.konkuk.medicarecall.data.repository.MedicineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@KoinViewModel
class MedicineViewModel(
    private val medicineRepository: MedicineRepository,
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

    // 복약 상태
    private companion object {
        const val TAG = "MED_API"
    }

    data class ScreenState(
        val loading: Boolean = false,
        val items: List<MedicineUiState> = emptyList(),
        val emptyDate: LocalDate? = null,
        val hasConfiguredMeds: Boolean = false,
    )

    private val _state = MutableStateFlow(ScreenState())
    val state: StateFlow<ScreenState> = _state

    fun loadMedicinesForDate(elderId: Int, date: LocalDate) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, emptyDate = null) }

            runCatching {
                medicineRepository.getMedicines(elderId, date)  // Model
            }.onSuccess { medicines ->
                val uiList = medicines.toMedicineUiStates()  // Model → UiState
                _state.update {
                    it.copy(
                        loading = false,
                        items = uiList,
                        emptyDate = if (uiList.isEmpty()) date else null,
                        hasConfiguredMeds = uiList.isNotEmpty(),
                    )
                }
            }.onFailure {
                _state.update {
                    it.copy(
                        loading = false,
                        items = emptyList(),
                        emptyDate = date,
                        hasConfiguredMeds = false,
                    )
                }
            }
        }
    }
}
