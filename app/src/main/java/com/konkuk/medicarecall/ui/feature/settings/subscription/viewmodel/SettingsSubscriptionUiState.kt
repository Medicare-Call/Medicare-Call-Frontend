package com.konkuk.medicarecall.ui.feature.settings.subscription.viewmodel

import com.konkuk.medicarecall.ui.model.ElderSubscription

data class SettingsSubscriptionUiState(
    val subscriptions: List<ElderSubscription> = emptyList(),
    val errorMessage: String? = null,
) {
    val hasSubscriptions: Boolean get() = subscriptions.isNotEmpty()
    val hasError: Boolean get() = errorMessage != null
}
