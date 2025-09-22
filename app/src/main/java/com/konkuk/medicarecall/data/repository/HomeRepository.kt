package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.ui.feature.home.viewmodel.HomeUiState
import java.time.LocalDate

interface HomeRepository {
    suspend fun requestImmediateCareCall(elderId: Int, careCallOption: String): Result<Unit>
    suspend fun getHomeUiState(elderId: Int, date: LocalDate): HomeUiState
}
