package com.konkuk.medicarecall.ui.feature.homedetail.statemental.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.repository.MentalRepository
import com.konkuk.medicarecall.domain.util.now
import com.konkuk.medicarecall.domain.util.previousOrSame
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import org.koin.android.annotation.KoinViewModel
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
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
            base.previousOrSame(DayOfWeek.SUNDAY)
        return (0..6).map { startOfWeek.plus(it.toLong(), DateTimeUnit.DAY) }
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
