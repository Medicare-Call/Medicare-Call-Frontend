package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.domain.model.Mental
import java.time.LocalDate

interface MentalRepository {
    suspend fun getMental(elderId: Int, date: LocalDate): Result<Mental>
}
