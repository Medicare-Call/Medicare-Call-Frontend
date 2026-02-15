package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.api.elders.MealService
import com.konkuk.medicarecall.data.mapper.toMeals
import com.konkuk.medicarecall.data.repository.MealRepository
import com.konkuk.medicarecall.data.util.handleResponse
import com.konkuk.medicarecall.domain.model.Meal
import org.koin.core.annotation.Single
import kotlinx.datetime.LocalDate

@Single
class MealRepositoryImpl(
    private val mealService: MealService,
) : MealRepository {

    override suspend fun getMeals(
        elderId: Long,
        date: LocalDate,
    ): Result<List<Meal>> = runCatching {
        val response = mealService.getDailyMeal(
            elderId = elderId,
            date = date.toString(),
        )

        val dto = response.handleResponse()

        dto.toMeals()
    }
}
