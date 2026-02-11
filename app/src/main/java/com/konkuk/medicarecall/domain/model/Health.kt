package com.konkuk.medicarecall.domain.model

data class Health(
    val symptoms: List<String> = emptyList(),
    val symptomAnalysis: String = "",
)
