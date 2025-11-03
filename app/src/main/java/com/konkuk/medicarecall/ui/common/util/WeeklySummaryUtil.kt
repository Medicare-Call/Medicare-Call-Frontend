package com.konkuk.medicarecall.ui.common.util

import androidx.compose.ui.graphics.Color
import com.konkuk.medicarecall.ui.feature.statistics.viewmodel.WeeklyMealUiState
import com.konkuk.medicarecall.ui.feature.statistics.viewmodel.WeeklyMedicineUiState
import com.konkuk.medicarecall.ui.theme.MediCareCallColors

object WeeklySummaryUtil {
    fun getMealIconColor(meal: WeeklyMealUiState, colors: MediCareCallColors): Color {
        return when {
            meal.eatenCount == meal.totalCount -> colors.positive
            meal.eatenCount in 1 until meal.totalCount -> colors.warning2
            meal.eatenCount == 0 -> colors.negative
            else -> colors.gray3
        }
    }

    fun getMedicineIconColor(medicine: WeeklyMedicineUiState, colors: MediCareCallColors): Color {
        return when {
            medicine.takenCount == medicine.totalCount -> colors.positive
            medicine.takenCount in 1 until medicine.totalCount -> colors.warning2
            medicine.takenCount == 0 -> colors.negative
            else -> colors.gray3
        }
    }
}
