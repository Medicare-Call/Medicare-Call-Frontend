package com.konkuk.medicarecall.domain.model

data class Glucose(
    val graphDataPoints: List<GlucosePoint> = emptyList(),
    val hasNext: Boolean = true,
)

data class GlucosePoint(
    val date: String, // 측정 시점
    val value: Int, // 혈당 수치
)
