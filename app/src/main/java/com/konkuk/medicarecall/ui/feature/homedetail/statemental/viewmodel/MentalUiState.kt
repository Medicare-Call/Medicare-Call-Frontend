package com.konkuk.medicarecall.ui.feature.homedetail.statemental.viewmodel

data class MentalUiState(
    val mentalSummary: List<String> = emptyList(), // 심리 상태 요약 3줄
) {
    val isRecorded: Boolean
        get() = mentalSummary.isNotEmpty()

    companion object {
        val EMPTY = MentalUiState()
    }
}
