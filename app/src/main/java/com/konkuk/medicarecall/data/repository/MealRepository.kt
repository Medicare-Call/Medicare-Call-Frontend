package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.domain.model.Meal
import java.time.LocalDate

interface MealRepository {
    suspend fun getMeals(elderId: Int, date: LocalDate): Result<List<Meal>>
}
