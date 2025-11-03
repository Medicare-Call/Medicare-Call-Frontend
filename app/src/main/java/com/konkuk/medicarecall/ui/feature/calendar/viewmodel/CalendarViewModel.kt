package com.konkuk.medicarecall.ui.feature.calendar.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor() : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    fun resetToToday() {
        _selectedDate.value = LocalDate.now()
    }

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }

    /** 현재 선택된 날짜가 속한 주(일~토)를 반환 */
    fun getCurrentWeekDates(): List<LocalDate> {
        val base = _selectedDate.value
        val startOfWeek =
            base.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
        return (0..6).map { startOfWeek.plusDays(it.toLong()) }
    }
}
