package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.elders.MealService
import com.konkuk.medicarecall.data.repository.MealRepository
import com.konkuk.medicarecall.ui.feature.homedetail.meal.viewmodel.MealUiState
import org.koin.core.annotation.Single
import java.time.LocalDate

@Single
class MealRepositoryImpl(
    private val mealService: MealService,
) : MealRepository {
    override suspend fun getMealUiStateList(elderId: Int, date: LocalDate): List<MealUiState> {
        val response = mealService.getDailyMeal(elderId, date.toString())

        return if (response.isSuccessful) {
            val body = response.body()
                ?: error("Meal response body is null")

            listOf(
                MealUiState(
                    mealTime = "아침",
                    description = body.meals.breakfast ?: "식사 기록 전이에요.",
                    isRecorded = body.meals.breakfast != null,
                    isEaten = null,
                ),
                MealUiState(
                    mealTime = "점심",
                    description = body.meals.lunch ?: "식사 기록 전이에요.",
                    isRecorded = body.meals.lunch != null,
                    isEaten = null,
                ),
                MealUiState(
                    mealTime = "저녁",
                    description = body.meals.dinner ?: "식사 기록 전이에요.",
                    isRecorded = body.meals.dinner != null,
                    isEaten = null,
                ),
            )
        } else {
            if (response.code == 404) {
                listOf(
                    MealUiState("아침", "식사 기록 전이에요.", false, null),
                    MealUiState("점심", "식사 기록 전이에요.", false, null),
                    MealUiState("저녁", "식사 기록 전이에요.", false, null),
                )
            } else {
                error("Failed to fetch meal data: ${response.code}")
            }
        }
    }
}
