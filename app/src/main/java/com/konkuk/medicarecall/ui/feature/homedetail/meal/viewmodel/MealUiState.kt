package com.konkuk.medicarecall.ui.feature.homedetail.meal.viewmodel

import com.konkuk.medicarecall.domain.model.Meal

data class MealUiState(
    val meals: List<Meal> = emptyList(),
)
