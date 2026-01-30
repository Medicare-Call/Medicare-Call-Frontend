package com.konkuk.medicarecall.ui.feature.settings.subscription.viewmodel

import com.konkuk.medicarecall.data.dto.response.EldersSubscriptionResponseDto

data class SettingsSubscriptionDetailUiState(
    val subscriptionData: EldersSubscriptionResponseDto? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
