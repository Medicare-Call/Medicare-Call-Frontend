package com.konkuk.medicarecall.ui.feature.homedetail.meal.viewmodel

data class MealUiState(
    val mealTime: String, // 아침 점심 저녁
    val description: String, // 식사 내용
    val isRecorded: Boolean, // 식사 기록 여부
    val isEaten: Boolean?, // 식사 유무, null값 허용
) {
    companion object {
        val EMPTY = MealUiState(
            mealTime = "",
            description = "",
            isRecorded = false,
            isEaten = null,
        )
    }
}
