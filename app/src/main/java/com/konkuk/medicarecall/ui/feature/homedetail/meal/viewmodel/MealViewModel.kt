package com.konkuk.medicarecall.ui.feature.homedetail.meal.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.exception.HttpException
import com.konkuk.medicarecall.data.repository.MealRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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
            val formatted = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
            Log.d(TAG, "Request elderId=$elderId, date=$formatted")

            try {
                val result = mealRepository.getMealUiStateList(elderId, date)
                _meals.value = result
                Log.i(TAG, "Success elderId=$elderId, date=$formatted, items=${result.size}")
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        when (e.code()) {
                            404 -> {
                                // 미기록
                                Log.i(TAG, "No data (404) elderId=$elderId, date=$formatted")
                                _meals.value = defaultUnrecordedMeals()
                            }

                            400 -> {
                                Log.w(
                                    TAG,
                                    "Bad request (400) elderId=$elderId, date=$formatted, msg=${e.message}",
                                )
                                _meals.value = defaultUnrecordedMeals()
                            }

                            401, 403 -> {
                                Log.w(TAG, "Unauthorized (${e.code()}) elderId=$elderId")
                                _meals.value = defaultUnrecordedMeals()
                            }

                            else -> {
                                Log.e(
                                    TAG,
                                    "API error code=${e.code()} elderId=$elderId, date=$formatted",
                                    e,
                                )
                                _meals.value = defaultUnrecordedMeals()
                            }
                        }
                    }

                    else -> {
                        Log.e(TAG, "Unexpected error elderId=$elderId, date=$formatted", e)
                        _meals.value = defaultUnrecordedMeals()
                    }
                }
            }
        }
    }

    private fun defaultUnrecordedMeals(): List<MealUiState> =
        listOf("아침", "점심", "저녁").map {
            MealUiState(
                mealTime = it,
                description = "식사 기록 전이에요.",
                isRecorded = false,
                isEaten = null,
            )
        }
}
