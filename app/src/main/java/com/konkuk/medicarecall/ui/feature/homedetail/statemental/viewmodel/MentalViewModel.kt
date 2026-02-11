package com.konkuk.medicarecall.ui.feature.homedetail.statemental.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.repository.MentalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import com.konkuk.medicarecall.data.exception.HttpException
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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
    private companion object {
        const val TAG = "MENTAL_API"
    }

    private val _mental = MutableStateFlow(MentalUiState.Companion.EMPTY)
    val mental: StateFlow<MentalUiState> = _mental

    fun loadMental(elderId: Long, date: LocalDate) {
        viewModelScope.launch {
            val ui = mentalRepository.getMentalUiState(elderId, date)
            Log.d("MENTAL_VM", "ui=$ui")
            _mental.value = ui
        }
    }

    fun loadMentalDataForDate(elderId: Long, date: LocalDate) {
        viewModelScope.launch {
            val formatted = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
            Log.d(TAG, "Request elderId=$elderId, date=$formatted")

            try {
                val ui = mentalRepository.getMentalUiState(elderId, date)
                _mental.value = ui

                if (ui.isRecorded) {
                    Log.i(TAG, "Loaded data: $ui")
                } else {
                    Log.i(TAG, "No data (EMPTY) elderId=$elderId, date=$formatted")
                }
            } catch (e: HttpException) {
                if (e.code() == 404) {
                    Log.i(TAG, "No data (404) elderId=$elderId, date=$formatted")
                    _mental.value = MentalUiState.Companion.EMPTY
                } else {
                    Log.e(TAG, "API error code=${e.code()} elderId=$elderId, date=$formatted", e)
                    _mental.value = MentalUiState.Companion.EMPTY
                }
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error elderId=$elderId, date=$formatted", e)
                _mental.value = MentalUiState.Companion.EMPTY
            }
        }
    }
}
