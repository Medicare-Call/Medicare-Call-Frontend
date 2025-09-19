package com.konkuk.medicarecall.ui.feature.homedetail.meal.data

import com.konkuk.medicarecall.ui.feature.homedetail.meal.model.MealUiState
import java.time.LocalDate


interface MealRepository {
    suspend fun getMealUiStateList(elderId: Int, date: LocalDate): List<MealUiState>
}
