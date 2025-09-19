package com.konkuk.medicarecall.ui.feature.homedetail.statemental.model

import kotlinx.serialization.Serializable


@Serializable
data class MentalResponseDto(
    val date: String,
    val commentList: List<String>? = null
)
