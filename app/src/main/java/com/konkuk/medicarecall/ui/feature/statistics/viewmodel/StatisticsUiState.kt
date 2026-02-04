package com.konkuk.medicarecall.ui.feature.statistics.viewmodel

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

data class StatisticsUiState(
    val isLoading: Boolean = false,
    val summary: WeeklySummaryUiState? = null,
    val error: String? = null,
    val selectedElderId: Int? = null,
    val currentWeek: Pair<LocalDate, LocalDate> = getDefaultWeekRange(),
    val isLatestWeek: Boolean = true,
    val isEarliestWeek: Boolean = false,
    val eldersMap: Map<Int, String> = emptyMap(),
)

private fun getDefaultWeekRange(): Pair<LocalDate, LocalDate> {
    val start = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    return start to start.plusDays(6)
}
