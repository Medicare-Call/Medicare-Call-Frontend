package com.konkuk.medicarecall.data.mapper

import com.konkuk.medicarecall.domain.model.Mental
import com.konkuk.medicarecall.ui.feature.homedetail.statemental.viewmodel.MentalUiState

fun Mental.toUiState(): MentalUiState =
    MentalUiState(
        mentalSummary = mentalSummary,
    )
