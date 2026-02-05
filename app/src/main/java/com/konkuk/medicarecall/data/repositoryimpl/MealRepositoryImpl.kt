package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.elders.MealService
import com.konkuk.medicarecall.data.mapper.toMeals
import com.konkuk.medicarecall.data.repository.MealRepository
import com.konkuk.medicarecall.data.util.handleResponse
import com.konkuk.medicarecall.domain.model.Meal
import org.koin.core.annotation.Single
import java.time.LocalDate

@Single
class MealRepositoryImpl(
    private val mealService: MealService,
) : MealRepository {

    override suspend fun getMeals(
        elderId: Int,
        date: LocalDate,
    ): List<Meal> {

        val response = mealService.getDailyMeal(
            elderId = elderId,
            date = date.toString(),
        )

        val dto = response.handleResponse()

        return dto.toMeals()
    }
}
