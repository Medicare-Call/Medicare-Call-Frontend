package com.konkuk.medicarecall.data.mapper

import com.konkuk.medicarecall.data.dto.response.HealthResponseDto
import com.konkuk.medicarecall.domain.model.Health
import com.konkuk.medicarecall.ui.feature.homedetail.statehealth.viewmodel.HealthUiState

fun HealthResponseDto.toHealth(): Health =
    Health(
        symptoms = symptomList.orEmpty(),
        symptomAnalysis = analysisComment,
    )

fun Health.toUiState(): HealthUiState =
    if (symptoms.isEmpty() && symptomAnalysis.isNullOrBlank()) {
        HealthUiState.EMPTY
    } else {
        HealthUiState(
            symptoms = symptoms,
            symptomAnalysis = symptomAnalysis.orEmpty(),
            isRecorded = true,
        )
    }
