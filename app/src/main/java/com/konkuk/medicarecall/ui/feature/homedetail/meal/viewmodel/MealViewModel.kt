package com.konkuk.medicarecall.ui.feature.homedetail.meal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.repository.MealRepository
import com.konkuk.medicarecall.domain.util.getCurrentWeekDates
import com.konkuk.medicarecall.domain.util.now
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.koin.android.annotation.KoinViewModel

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
        return _selectedDate.value.getCurrentWeekDates()
    }

    // 식사 상태
    private companion object {
        const val TAG = "MEAL_API"
    }

    private val _meals = MutableStateFlow(MealUiState())
    val meals: StateFlow<MealUiState> = _meals

    fun loadMealsForDate(elderId: Long, date: LocalDate) {
        viewModelScope.launch {
            mealRepository.getMeals(elderId, date)
                .onSuccess { meals ->
                    _meals.value = MealUiState(meals = meals)
                }
                .onFailure {
                    _meals.value = MealUiState()
                }
        }
    }
}
