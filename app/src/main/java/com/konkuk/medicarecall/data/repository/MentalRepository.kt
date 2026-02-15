package com.konkuk.medicarecall.data.repository

import com.konkuk.medicarecall.domain.model.Mental
import kotlinx.datetime.LocalDate

interface MentalRepository {
    suspend fun getMental(elderId: Long, date: LocalDate): Result<Mental>
}
