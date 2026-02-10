package com.konkuk.medicarecall.data.mapper

import com.konkuk.medicarecall.data.dto.response.HealthResponseDto
import com.konkuk.medicarecall.domain.model.Health

fun HealthResponseDto.toHealth(): Health =
    Health(
        symptoms = symptomList.orEmpty(),
        symptomAnalysis = analysisComment.orEmpty(),
    )
