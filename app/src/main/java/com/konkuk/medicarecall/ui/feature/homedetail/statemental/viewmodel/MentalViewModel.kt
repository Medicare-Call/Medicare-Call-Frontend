package com.konkuk.medicarecall.ui.feature.homedetail.statemental.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.repository.MentalRepository
import com.konkuk.medicarecall.domain.util.getCurrentWeekDates
import com.konkuk.medicarecall.domain.util.now
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.koin.android.annotation.KoinViewModel

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
        return _selectedDate.value.getCurrentWeekDates()
    }

    // 심리 상태
    private val _mental = MutableStateFlow(MentalUiState())
    val mental: StateFlow<MentalUiState> = _mental

    fun loadMentalDataForDate(elderId: Long, date: LocalDate) {
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
