package com.konkuk.medicarecall.data.mapper

import com.konkuk.medicarecall.data.dto.response.GlucoseResponseDto
import com.konkuk.medicarecall.domain.model.Glucose
import com.konkuk.medicarecall.domain.model.GlucosePoint
import com.konkuk.medicarecall.ui.feature.homedetail.glucoselevel.viewmodel.GlucoseUiState
import com.konkuk.medicarecall.ui.model.GlucoseTiming
import com.konkuk.medicarecall.ui.model.GraphDataPoint
import kotlinx.datetime.LocalDate

fun GlucoseResponseDto.toDomain(): Glucose =
    Glucose(
        graphDataPoints = data.map { item ->
            GlucosePoint(
                date = item.date,
                value = item.value,
            )
        },
        hasNext = hasNextPage,
    )

fun Glucose.toUiState(
    timing: GlucoseTiming,
    isLoading: Boolean = false,
    selectedIndex: Int = -1,
): GlucoseUiState =
    GlucoseUiState(
        graphDataPoints = graphDataPoints.map { p ->
            GraphDataPoint(
                date = LocalDate.parse(p.date),
                value = p.value.toFloat(),
            )
        },
        selectedTiming = timing,
        hasNext = hasNext,
        isLoading = isLoading,
        selectedIndex = selectedIndex,
    )
