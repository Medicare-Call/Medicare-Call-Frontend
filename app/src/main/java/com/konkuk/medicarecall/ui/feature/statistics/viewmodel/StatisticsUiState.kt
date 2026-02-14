package com.konkuk.medicarecall.ui.feature.statistics.viewmodel

import com.konkuk.medicarecall.domain.util.getWeekRange
import com.konkuk.medicarecall.domain.util.now
import kotlinx.datetime.LocalDate

data class StatisticsUiState(
    val isLoading: Boolean = false,
    val summary: WeeklySummaryUiState? = null,
    val error: String? = null,
    val selectedElderId: Long = -1L,
    val currentWeek: Pair<LocalDate, LocalDate> = LocalDate.now().getWeekRange(),
    val isLatestWeek: Boolean = true,
    val isEarliestWeek: Boolean = false,
    val eldersMap: Map<Long, String> = emptyMap(),
)
