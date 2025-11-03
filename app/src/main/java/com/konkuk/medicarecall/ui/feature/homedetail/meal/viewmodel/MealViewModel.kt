package com.konkuk.medicarecall.ui.feature.homedetail.meal.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.medicarecall.data.repository.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class MealViewModel @Inject constructor(
    private val mealRepository: MealRepository,
) : ViewModel() {

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
                                    "Bad request (400) elderId=$elderId, date=$formatted, msg=${e.message()}",
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
