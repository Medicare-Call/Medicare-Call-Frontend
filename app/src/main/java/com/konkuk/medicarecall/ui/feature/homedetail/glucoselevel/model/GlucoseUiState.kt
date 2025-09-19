package com.konkuk.medicarecall.ui.feature.homedetail.glucoselevel.model

data class GlucoseUiState(
    val graphDataPoints: List<GraphDataPoint> = emptyList(),
    val selectedTiming: GlucoseTiming = GlucoseTiming.BEFORE_MEAL,
    val hasNext: Boolean = true,
    val isLoading: Boolean = false,
    val selectedIndex: Int = -1
)
