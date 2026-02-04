package com.konkuk.medicarecall.ui.feature.homedetail.meal.viewmodel

data class MealUiState(
    val mealTime: String = "", // 아침 점심 저녁
    val description: String = "", // 식사 내용
) {
    val isRecorded: Boolean
        get() = description.isNotBlank()
}
