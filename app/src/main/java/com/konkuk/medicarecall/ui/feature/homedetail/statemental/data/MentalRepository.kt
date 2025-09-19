package com.konkuk.medicarecall.ui.feature.homedetail.statemental.data

import com.konkuk.medicarecall.ui.feature.homedetail.statemental.model.MentalUiState
import java.time.LocalDate

interface MentalRepository {
    suspend fun getMentalUiState(elderId: Int, date: LocalDate): MentalUiState
}
