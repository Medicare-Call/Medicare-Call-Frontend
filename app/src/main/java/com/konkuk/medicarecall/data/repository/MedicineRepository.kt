package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.domain.model.Medicine
import kotlinx.datetime.LocalDate

interface MedicineRepository {

    suspend fun getMedicines(elderId: Long, date: LocalDate): Result<List<Medicine>>

    suspend fun getConfiguredMedicines(elderId: Long): Result<List<Medicine>>
}
