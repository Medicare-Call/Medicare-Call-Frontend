package com.konkuk.medicarecall.ui.feature.settings.subscription.viewmodel

import com.konkuk.medicarecall.data.dto.response.EldersSubscriptionResponseDto

data class SettingsSubscriptionUiState(
    val subscriptions: List<EldersSubscriptionResponseDto> = emptyList(),
    val errorMessage: String? = null,
)
