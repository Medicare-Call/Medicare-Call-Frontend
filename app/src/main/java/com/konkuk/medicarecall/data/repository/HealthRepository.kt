package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.ui.feature.homedetail.statehealth.viewmodel.HealthUiState
import java.time.LocalDate

interface HealthRepository {
    suspend fun getHealthUiState(elderId: Int, date: LocalDate): Result<HealthUiState>
}
