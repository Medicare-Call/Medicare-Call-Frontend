package com.konkuk.medicarecall.data.mapper

import com.konkuk.medicarecall.data.dto.response.MealResponseDto
import com.konkuk.medicarecall.domain.model.Meal
import com.konkuk.medicarecall.ui.feature.homedetail.meal.viewmodel.MealUiState

fun MealResponseDto.toMeals(): List<Meal> = listOfNotNull(
    meals.breakfast?.let {
        Meal(
            mealTime = "아침",
            description = it,
        )
    },
    meals.lunch?.let {
        Meal(
            mealTime = "점심",
            description = it,
        )
    },
    meals.dinner?.let {
        Meal(
            mealTime = "저녁",
            description = it,
        )
    },
)

fun List<Meal>.toMealUiStates(): List<MealUiState> =
    if (isEmpty()) {
        defaultUnrecordedMeals()
    } else {
        map { meal ->
            MealUiState(
                mealTime = meal.mealTime,
                description = meal.description ?: "식사 기록 전이에요.",
            )
        }
    }

private fun defaultUnrecordedMeals(): List<MealUiState> =
    listOf("아침", "점심", "저녁").map {
        MealUiState(
            mealTime = it,
            description = "식사 기록 전이에요.",
        )
    }
