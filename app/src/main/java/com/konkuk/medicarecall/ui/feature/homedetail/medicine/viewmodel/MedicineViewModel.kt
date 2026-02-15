package com.konkuk.medicarecall.ui.feature.homedetail.medicine.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.repository.MedicineRepository
import com.konkuk.medicarecall.domain.util.getCurrentWeekDates
import com.konkuk.medicarecall.domain.util.now
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.koin.android.annotation.KoinViewModel

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
        return _selectedDate.value.getCurrentWeekDates()
    }

    // 복약 상태
    private companion object {
        const val TAG = "MED_API"
    }

    data class ScreenState(
        val loading: Boolean = false,
        val medicines: List<MedicineUiState> = emptyList(),
        val emptyDate: LocalDate? = null,
        val hasConfiguredMeds: Boolean = false,
    )

    private val _state = MutableStateFlow(ScreenState())
    val state: StateFlow<ScreenState> = _state

    fun loadMedicinesForDate(elderId: Long, date: LocalDate) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, emptyDate = null) }

            medicineRepository.getMedicines(elderId, date)
                .onSuccess { medicines ->
                    _state.update {
                        it.copy(
                            loading = false,
                            medicines = medicines.map { model -> MedicineUiState(medicine = model) },
                            emptyDate = if (medicines.isEmpty()) date else null,
                            hasConfiguredMeds = medicines.isNotEmpty(),
                        )
                    }
                }
                .onFailure {
                    _state.update {
                        it.copy(
                            loading = false,
                            medicines = emptyList(),
                            emptyDate = date,
                            hasConfiguredMeds = false,
                        )
                    }
                }
        }
    }
}
