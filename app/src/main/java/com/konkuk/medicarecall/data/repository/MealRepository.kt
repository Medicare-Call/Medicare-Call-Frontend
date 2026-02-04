package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.ui.feature.homedetail.meal.viewmodel.MealUiState
import java.time.LocalDate

interface MealRepository {
    suspend fun getMeals(elderId: Int, date: LocalDate): List<MealUiState>
}
