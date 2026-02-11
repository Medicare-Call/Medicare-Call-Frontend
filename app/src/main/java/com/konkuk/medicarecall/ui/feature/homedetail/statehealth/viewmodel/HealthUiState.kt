package com.konkuk.medicarecall.ui.feature.homedetail.statehealth.viewmodel

import com.konkuk.medicarecall.domain.model.Health

data class HealthUiState(
    val health: Health = Health(),
)
