package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.domain.model.Meal
import kotlinx.datetime.LocalDate

interface MealRepository {
    suspend fun getMeals(elderId: Long, date: LocalDate): Result<List<Meal>>
}
