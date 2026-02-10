package com.konkuk.medicarecall.data.mapper

import com.konkuk.medicarecall.data.dto.response.MealResponseDto
import com.konkuk.medicarecall.domain.model.Meal

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
