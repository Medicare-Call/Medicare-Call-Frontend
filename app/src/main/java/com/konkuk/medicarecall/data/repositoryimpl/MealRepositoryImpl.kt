package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.elders.MealService
import com.konkuk.medicarecall.data.repository.MealRepository
import com.konkuk.medicarecall.ui.feature.homedetail.meal.viewmodel.MealUiState
import java.time.LocalDate
import javax.inject.Inject

class MealRepositoryImpl @Inject constructor(
    private val mealService: MealService,
) : MealRepository {
    override suspend fun getMealUiStateList(elderId: Int, date: LocalDate): List<MealUiState> {
        val response = mealService.getDailyMeal(elderId, date.toString())

        return listOf(
            MealUiState(
                mealTime = "아침",
                description = response.meals.breakfast ?: "식사 기록 전이에요.",
                isRecorded = response.meals.breakfast != null,
                isEaten = null,
            ),
            MealUiState(
                mealTime = "점심",
                description = response.meals.lunch ?: "식사 기록 전이에요.",
                isRecorded = response.meals.lunch != null,
                isEaten = null,
            ),
            MealUiState(
                mealTime = "저녁",
                description = response.meals.dinner ?: "식사 기록 전이에요.",
                isRecorded = response.meals.dinner != null,
                isEaten = null,
            ),
        )
    }
}
