package com.konkuk.medicarecall.ui.feature.homedetail.statemental.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.repository.MentalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@KoinViewModel
class MentalViewModel(
    private val mentalRepository: MentalRepository,
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

    // 심리 상태
    private val _mental = MutableStateFlow(MentalUiState())
    val mental: StateFlow<MentalUiState> = _mental

    fun loadMentalDataForDate(elderId: Int, date: LocalDate) {
        viewModelScope.launch {
            mentalRepository.getMental(elderId, date)
                .onSuccess { mental ->
                    _mental.value = MentalUiState(mental = mental)
                }
                .onFailure {
                    _mental.value = MentalUiState()
                }
        }
    }
}
