package com.konkuk.medicarecall.ui.feature.settings.subscription.viewmodel

import com.konkuk.medicarecall.ui.model.ElderSubscription

data class SettingsSubscriptionDetailUiState(
    val subscriptionData: ElderSubscription? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) {
    val hasData: Boolean get() = subscriptionData != null
    val hasError: Boolean get() = errorMessage != null
}
