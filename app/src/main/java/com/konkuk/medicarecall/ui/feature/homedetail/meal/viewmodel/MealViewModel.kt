package com.konkuk.medicarecall.ui.feature.homedetail.meal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.mapper.toMealUiStates
import com.konkuk.medicarecall.data.repository.MealRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@KoinViewModel
class MealViewModel(
    private val mealRepository: MealRepository,
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

    // 식사 상태
    private companion object {
        const val TAG = "MEAL_API"
    }

    private val _meals = MutableStateFlow<List<MealUiState>>(emptyList())
    val meals: StateFlow<List<MealUiState>> = _meals

    fun loadMealsForDate(elderId: Int, date: LocalDate) {
        viewModelScope.launch {
            try {
                val meals = mealRepository.getMeals(elderId, date)  // Model
                _meals.value = meals.toMealUiStates()  // Model → UiState
            } catch (e: Exception) {
                _meals.value = emptyList()
            }
        }
    }
}
