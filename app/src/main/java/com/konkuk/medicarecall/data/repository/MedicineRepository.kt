package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.ui.feature.homedetail.medicine.viewmodel.MedicineUiState
import java.time.LocalDate

interface MedicineRepository {

    suspend fun getMedicineUiStateList(elderId: Long, date: LocalDate): List<MedicineUiState>

    suspend fun getConfiguredMedicineUiList(elderId: Long): List<MedicineUiState>
}
