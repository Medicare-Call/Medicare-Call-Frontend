package com.konkuk.medicarecall.ui.feature.homedetail.glucoselevel.viewmodel

import com.konkuk.medicarecall.ui.model.GlucoseTiming
import com.konkuk.medicarecall.ui.model.GraphDataPoint

data class GlucoseUiState(
    val graphDataPoints: List<GraphDataPoint> = emptyList(),
    val selectedTiming: GlucoseTiming = GlucoseTiming.BEFORE_MEAL,
    val hasNext: Boolean = true,
    val isLoading: Boolean = false,
    val selectedIndex: Int = -1,
)
