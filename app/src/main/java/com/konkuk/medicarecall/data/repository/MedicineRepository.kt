package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.domain.model.Medicine
import java.time.LocalDate

interface MedicineRepository {

    suspend fun getMedicines(elderId: Int, date: LocalDate): Result<List<Medicine>>

    suspend fun getConfiguredMedicines(elderId: Int): Result<List<Medicine>>
}
