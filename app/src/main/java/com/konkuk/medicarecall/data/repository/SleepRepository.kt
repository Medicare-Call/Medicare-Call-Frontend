package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.ui.feature.homedetail.sleep.viewmodel.SleepUiState
import java.time.LocalDate

interface SleepRepository {
    suspend fun getSleepUiState(elderId: Int, date: LocalDate): SleepUiState
}
