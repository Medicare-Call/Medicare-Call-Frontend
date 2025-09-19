package com.konkuk.medicarecall.ui.feature.home.data

import com.konkuk.medicarecall.ui.feature.home.model.HomeUiState
import java.time.LocalDate

interface HomeRepository {
    suspend fun requestImmediateCareCall(elderId: Int, careCallOption: String): Result<Unit>
    suspend fun getHomeUiState(elderId: Int, date: LocalDate): HomeUiState
}
