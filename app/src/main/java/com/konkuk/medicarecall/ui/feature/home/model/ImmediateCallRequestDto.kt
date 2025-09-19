package com.konkuk.medicarecall.ui.feature.home.model

import kotlinx.serialization.Serializable

@Serializable
data class ImmediateCallRequestDto(
    val elderId: Int,
    val careCallOption: String, // FIRST, SECOND, THIRD
)
