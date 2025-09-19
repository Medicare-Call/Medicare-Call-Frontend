package com.konkuk.medicarecall.ui.feature.homedetail.medicine.data

import com.konkuk.medicarecall.ui.feature.homedetail.medicine.model.MedicineUiState
import java.time.LocalDate

interface MedicineRepository {

    suspend fun getMedicineUiStateList(elderId: Int, date: LocalDate): List<MedicineUiState>

    suspend fun getConfiguredMedicineUiList(elderId: Int): List<MedicineUiState>
}
