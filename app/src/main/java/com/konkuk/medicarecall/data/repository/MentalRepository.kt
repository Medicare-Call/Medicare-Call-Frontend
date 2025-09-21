package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.ui.feature.homedetail.statemental.viewmodel.MentalUiState
import java.time.LocalDate

interface MentalRepository {
    suspend fun getMentalUiState(elderId: Int, date: LocalDate): MentalUiState
}
